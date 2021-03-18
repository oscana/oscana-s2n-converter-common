package jp.co.tis.s2n.converterCommon.struts.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.converterCommon.statistics.CommonOtherStatistics;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.ActionForward;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.Route;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsAction;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsForm;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.converterCommon.util.XmlUtils;

/**
 * Struts1の設定ファイルを解析してObject化する。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class StrutsAnalyzer {

    private static final String STRUTS_ACTION_METHOD_NAME = "execute";
    private static List<String> configValidationSupportList = new ArrayList<>();

    /**
     * StrutsConfig, Validationのペアを分析してStrusResourceを得る。
     * @param strutsConfigFile struts.xmlファイル対象
     * @param validationFile validation.xmlファイル対象
     * @param module これらのファイルにおけるstrutsのモジュール名
     * @return 分析結果のStrutsResource
     * @throws ParserConfigurationException ParserConfigurationException例外
     * @throws IOException IOException例外
     * @throws SAXException SAXException例外
     * @throws XPathExpressionException XPathExpressionException例外
     */
    public static StrutsAnalyzeResult analyzeStrutsResource(File strutsConfigFile, File validationFile, String module,
            String basePackage)
            throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
        StrutsAnalyzeResult analyzeResult = new StrutsAnalyzeResult();

        Document strutsConfig = XmlUtils.loadXMLFile(strutsConfigFile);
        Document validation = (validationFile != null) ? XmlUtils.loadXMLFile(validationFile) : null;

        ArrayList<StrutsAction> actionList = new ArrayList<StrutsAction>();
        Map<String, StrutsAction> mList = new LinkedHashMap<String, StrutsAction>();
        ArrayList<ActionForward> globalForwardList = new ArrayList<>();

        //GlobalForwardを取得
        NodeList curGlobalForwardList = listGlobalForwards(strutsConfig);
        for (int i = 0; i < curGlobalForwardList.getLength(); i++) {
            Element elm = (Element) curGlobalForwardList.item(i);
            ActionForward nForward = new ActionForward(elm, null);
            globalForwardList.add(nForward);
        }

        //Action一覧を取得
        NodeList curActionList = listActions(strutsConfig);
        for (int i = 0; i < curActionList.getLength(); i++) {
            Element elm = (Element) curActionList.item(i);
            StrutsAction nAction = new StrutsAction(elm, strutsConfig, validation, globalForwardList);
            nAction.setStrutsAnalyzeResult(analyzeResult);
            actionList.add(nAction);

            mList.put(nAction.getPath(), nAction);

        }
        Map<String, Route> routes = createNablarchRoutes(actionList, module, basePackage);

        //Form一覧を作成（キーはクラス名、重複不可）
        Map<String, StrutsForm> formList = new LinkedHashMap<String, StrutsForm>();
        for (Iterator<StrutsAction> iterator = actionList.iterator(); iterator.hasNext();) {
            StrutsAction strutsAction = iterator.next();
            StrutsForm sf = strutsAction.getForm();
            if (sf != null) {
                if (formList.containsKey(sf.getType())) {
                    System.err.println("同じフォームが２つ以上存在しています。" + sf.getType());
                } else {
                    formList.put(sf.getType(), sf);
                }
            }
        }

        analyzeResult.setActionList(actionList);
        analyzeResult.setFormList(formList);
        analyzeResult.setModule(module);
        analyzeResult.setRoutes(routes);

        //未サポートログの出力
        writeUnSupportedLog(strutsConfig);

        // struts-config.xmlログ出力
        TransformerFactory tfFactory = TransformerFactory.newInstance();
        Transformer tf = null;
        File f1 = null;
        try {
            tf = tfFactory.newTransformer();
            tf.setOutputProperty("indent", "yes");
            tf.setOutputProperty("encoding", "UTF-8");
            f1 = new File(strutsConfigFile.getAbsolutePath().replace("xml", "log"));
            tf.transform(new DOMSource(strutsConfig), new StreamResult(f1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (validation != null) {
            writeUnSupportedLog(validation);

            // validation.xmlログ出力
            File f2 = null;
            try {
                tf = tfFactory.newTransformer();
                f2 = new File(validationFile.getAbsolutePath().replace("xml", "log"));
                tf.transform(new DOMSource(validation), new StreamResult(f2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return analyzeResult;
    }

    /**
     * Actionの一覧を返す。
     * @param strutsConfig struts.xmlファイルの解析結果
     * @return ノードリスト
     * @throws XPathExpressionException 例外
     */
    protected static NodeList listActions(Document strutsConfig) throws XPathExpressionException {
        return XmlUtils.findNodeListFromDocument("//action", strutsConfig);

    }

    /**
     * GlobalForward内のForwardを返す。
     * @param strutsConfig struts.xmlファイルの解析結果
     * @return GlobalForward内のForward
     * @throws XPathExpressionException 例外
     */
    protected static NodeList listGlobalForwards(Document strutsConfig) throws XPathExpressionException {
        return XmlUtils.findNodeListFromDocument("//global-forwards/forward", strutsConfig);

    }

    /**
     * StrutsActionからRoutes.xmlのデータを生成する。
     * @param actionList アクションリスト
     * @param module モジュール
     * @param basePackage ベースパッケージ
     * @return Routes.xmlのデータ
     */
    public static Map<String, Route> createNablarchRoutes(List<StrutsAction> actionList, String module,
            String basePackage) {

        Map<String, Route> retList = new LinkedHashMap<>();

        for (Iterator<StrutsAction> itr = actionList.iterator(); itr.hasNext();) {
            StrutsAction curAction = itr.next();

            String sAction = null;
            if (!StringUtils.isEmpty(curAction.getType())) {
                try {
                    sAction = StringUtils.relativePackagePath(basePackage, curAction.getType());
                    sAction = sAction.replaceAll("Action$", "");

                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                    sAction = curAction.getType();
                }
            } else {
                sAction = curAction.getType();
            }

            Route curRoute = new Route(
                    StringUtils.addPath(StringUtils.formatStrutsModulePath(module), curAction.getPath()),
                    (StringUtils.isEmpty(curAction.getForward())) ? curAction.getForward()
                            : StringUtils.addPath(StringUtils.formatStrutsModulePath(module), curAction.getForward()),
                    sAction,
                    STRUTS_ACTION_METHOD_NAME, curAction);
            retList.put(curRoute.getPath(), curRoute);
        }

        return retList;
    }

    /**
     * input要素を読むときに、指定がActionForwardの名前だった場合、ActionForwardのパスを返す。<br>
     * さらにそのパスがActionForward(.do)だった場合は対象となるActionForwardを調べ、それがアクションに紐づかない。<br>
     * JSP直接ジャンプのタイプである場合はその先のJSPを応答する。
     *
     * @param path パス
     * @param strutsAnalyzeResult struts.xmlなどのファイルを解析する結果
     * @return パス
     */
    public static String resolveForward(String path, StrutsAnalyzeResult strutsAnalyzeResult) {
        if ((path != null) && (path.endsWith(".do"))) {
            //そのpathが.doであればActionForwardということになるので先を調べる
            String trgPath = path.replaceAll("\\.do$", ""); //.doを取り除く
            //Action検索
            List<StrutsAction> actionList = strutsAnalyzeResult.getActionList().stream()
                    .filter(action -> trgPath.equals(action.getPath())).collect(Collectors.toList());
            assert actionList.size() < 2;

        }
        return path;
    }

    /**
     * すべてのActionForwardからForwardするJSPファイル名を対象に検索する。
     * @param analyzeResults struts.xmlなどのファイルを解析する結果
     * @param jspPath   /validate/test.jsp のような形式
     * @return StrutsActionリスト
     */
    public static List<StrutsAction> findActionByForwardPath(StrutsAnalyzeResult[] analyzeResults, String jspPath) {
        ArrayList<StrutsAction> retList = new ArrayList<>();
        //Forwardの名前からの検索
        for (StrutsAnalyzeResult r : analyzeResults) {
            for (StrutsAction cact : r.getActionList()) {
                if (!StringUtils.isEmpty(cact.getForward())) {
                    String cPath = StringUtils.addPath(StringUtils.formatStrutsModulePath(r.getModule()),
                            cact.getForward());
                    if (cPath.equals(jspPath)) {
                        retList.add(cact);
                    }
                }
            }
        }
        return retList;
    }

    /**
     * すべてのActionForwardからforward名を対象に検索する。
     * @param analyzeResults struts.xmlなどのファイルを解析する結果
     * @param forwardName forward名
     * @return ActionForwardリスト
     */
    public static List<ActionForward> findForwardByName(StrutsAnalyzeResult[] analyzeResults, String forwardName) {
        ArrayList<ActionForward> retList = new ArrayList<>();
        //Forwardの名前からの検索
        for (StrutsAnalyzeResult r : analyzeResults) {
            for (StrutsAction cact : r.getActionList()) {

                Map<String, ActionForward> l = cact.getForwardList();
                if (!l.containsKey(forwardName)) {
                    continue;
                }
                ActionForward actionForward = l.get(forwardName);
                retList.add(actionForward);
                return retList;
            }
        }
        return retList;
    }

    /**
     * 未サポートノードのログをconfigに書き込む。
     * @param config 対象ノード
     * @throws IOException 例外
     */
    public static void writeUnSupportedLog(Document config) throws IOException {
        Element root = config.getDocumentElement();
        configValidationSupportList = readText2List(
                "jp/co/tis/s2n/converterCommon/struts/analyzer/StrutsSupportTagList.txt");
        writeUnSupportedLogSub(root, "/", config);
    }

    private static void writeUnSupportedLogSub(Node cNode, String xPath, Document config) {
        xPath = xPath + cNode.getNodeName();

        NamedNodeMap attrs = cNode.getAttributes();
        if (attrs != null) {
            StringBuilder commentPeoperty = new StringBuilder("TODO ツールで変換できません  属性：");

            boolean convertFlag = false;
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attrNode = attrs.item(i);
                if (attrNode.getNodeType() == Node.ATTRIBUTE_NODE) {

                    // struts-config.xmlとvalidation.xmlをログ出力する
                    // 未対応タグの場合
                    String xPathTemp = xPath + "/attribute::" + attrNode.getNodeName();
                    if (configValidationSupportList.contains(xPath)
                            && !configValidationSupportList.contains(xPathTemp)) {
                        commentPeoperty.append(attrNode.getNodeName() + " ");
                        convertFlag = true;
                    }

                    writeUnSupportedLogSub(attrNode, xPath + "/attribute::", config);

                }
            }

            if (convertFlag) {
                config.setStrictErrorChecking(false);
                config.insertBefore(config.createComment(commentPeoperty.toString()), cNode);
                config.insertBefore(config.createTextNode("\n"), cNode);
                CommonOtherStatistics.getInstance().noConvertConfig++;
            }
        }

        NodeList children = cNode.getChildNodes();
        if (children != null) {

            for (int i = 0; i < children.getLength(); i++) {
                Node childNode = children.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    // 未対応タグの場合
                    if (!configValidationSupportList.contains(xPath + "/" + childNode.getNodeName())) {
                        StringBuilder commentTag = new StringBuilder("TODO ツールで変換できません  タグ：");
                        commentTag.append(childNode.getNodeName());
                        config.setStrictErrorChecking(false);
                        config.insertBefore(config.createComment(commentTag.toString()), childNode);
                        config.insertBefore(config.createTextNode("\n"), childNode);
                        CommonOtherStatistics.getInstance().noConvertConfig++;
                    }

                    writeUnSupportedLogSub(childNode, xPath + "/", config);
                }
            }

        }
    }

    /**
     * テキストファイルの内容をリストにロードする。
     * @param filePath ファイルパス
     * @return Stringリスト
     * @throws IOException 例外
     */
    public static List<String> readText2List(String filePath) throws IOException {
        BufferedReader buffReader = null;
        List<String> list = new ArrayList<>();
        try {
            buffReader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filePath), "UTF-8"));
            String strTmp = "";
            while ((strTmp = buffReader.readLine()) != null) {
                list.add(strTmp);
            }
        } catch (Exception e) {
            LogUtils.warn(filePath, e);
        } finally {

            try {
                buffReader.close();
            } catch (Exception e) {
            } //ignore
        }
        return list;

    }
}
