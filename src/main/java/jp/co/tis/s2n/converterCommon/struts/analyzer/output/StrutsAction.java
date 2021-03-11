package jp.co.tis.s2n.converterCommon.struts.analyzer.output;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzeResult;
import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.converterCommon.util.XmlUtils;

/**
 * struts-config.xml, validation.xml内にあるActionを構造化したクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class StrutsAction extends AbstractStrutsBean {

    //ActionFormへの参照
    private StrutsForm form;
    //ActionForwardnリスト
    private Map<String, ActionForward> forwardList;
    //本オブジェクトん格納先への相互参照
    private StrutsAnalyzeResult strutsAnalyzeResult;

    public StrutsAction(Element elm, Document strutsConfig, Document validation,
            ArrayList<ActionForward> globalForwardList) {
        this.basisElement = elm;

        //nameの定義がある場合のみフォームを取り込む
        if (getName() != null) {

            Element formElement = XmlUtils.findElementFromDocument("//form-bean[@name='" + getName() + "']",
                    strutsConfig);
            if (formElement != null) {
                if (validation != null) {
                    this.form = new StrutsForm(formElement, this, strutsConfig, validation);
                } else {
                    System.err.println("Actonから指定されたフォーム[" + getName() + "]に対するvalidate.xmlがみつかりません。");

                }
            } else {
                System.err.println("Actonから指定されたフォーム[" + getName() + "]がStrutsConfig.xml中に見つかりません");
            }
        }

        //ActionForward
        this.forwardList = new LinkedHashMap<String, ActionForward>();
        NodeList forwardList = XmlUtils.findNodeListFromElement("forward", this.basisElement);
        for (int i = 0; i < forwardList.getLength(); i++) {
            ActionForward nForward = new ActionForward((Element) forwardList.item(i), this);
            this.forwardList.put(nForward.getName(), nForward);
        }

        //GlobalForwardのマージ
        for (ActionForward gForward : globalForwardList) {
            String gForwardName = gForward.getName();
            if (!this.forwardList.containsKey(gForwardName)) {
                //入ってなければ登録
                ActionForward nForward = gForward.copy(this);
                this.forwardList.put(nForward.getName(), nForward);
            }
        }

    }

    /**
     * form属性の値を取得する。
     * @return form属性の値
     */
    public StrutsForm getForm() {
        return form;
    }

    /**
     * ActionForwardオブジェクトを持つマップを取得する。
     * @return ActionForwardオブジェクトを持つマップ
     */
    public Map<String, ActionForward> getForwardList() {
        return forwardList;
    }

    /**
     * attribute属性の値を取得する。
     * @return attribute属性の値
     */
    public String getAttribute() {
        return StringUtils.normalizeNull(basisElement.getAttribute("attribute"));
    }

    /**
     * forward属性の値を取得する。
     * @return forward属性の値
     */
    public String getForward() {
        return StringUtils.normalizeNull(basisElement.getAttribute("forward"));
    }

    /**
     * input属性の値を取得する。
     * @return input属性の値
     */
    public String getInput() {
        return StringUtils.normalizeNull(basisElement.getAttribute("input"));
    }

    /**
     * name属性の値を取得する。
     * @return name属性の値
     */
    public String getName() {
        return StringUtils.normalizeNull(basisElement.getAttribute("name"));
    }

    /**
     * parameter属性の値を取得する。
     * @return parameter属性の値を取得する。
     */
    public String getParameter() {
        return StringUtils.normalizeNull(basisElement.getAttribute("parameter"));
    }

    /**
     * パス属性の値を取得する。
     * @return  パス属性の値
     */
    public String getPath() {
        return StringUtils.normalizeNull(basisElement.getAttribute("path"));
    }

    /**
     * scope属性の値を取得する。
     * @return scope属性の値
     */
    public String getScope() {
        return StringUtils.normalizeNull(basisElement.getAttribute("scope"));
    }

    /**
     * type属性の値を取得する。
     * @return type属性の値
     */
    public String getType() {
        return StringUtils.normalizeNull(basisElement.getAttribute("type"));
    }

    /**
     * validate属性の値を取得する。
     * @return validate属性の値
     */
    public String getValidate() {
        return StringUtils.normalizeNull(basisElement.getAttribute("validate"));
    }

    /**
     * struts.xmlなどのファイルの解析結果を取得する。
     * @return struts.xmlなどのファイルの解析結果
     */
    public StrutsAnalyzeResult getStrutsAnalyzeResult() {
        return strutsAnalyzeResult;
    }

    /**
     * struts.xmlなどのファイルの解析結果を設定する。
     * @param strutsAnalyzeResult struts.xmlなどのファイルの解析結果
     */
    public void setStrutsAnalyzeResult(StrutsAnalyzeResult strutsAnalyzeResult) {
        this.strutsAnalyzeResult = strutsAnalyzeResult;
    }

    /**
     * input要素を読むときに、指定がActionForwardの名前だった場合、ActionForwardのパスを返す。<br>
     * JSP直接ジャンプのタイプである場合はその先のJSPを応答する。
     * @return ActionForwardのパスもしくはその先のJSPを返す
     */
    public String getInputAndResolveForward() {
        String input = this.getInput();
        if (input != null) {
            Element elm = XmlUtils.findElementFromElement("forward[@name='input']", basisElement);
            if (elm != null) {
                //inputがForwardの名前に一致したらForwardの遷移先を返す
                return StringUtils.normalizeNull(elm.getAttribute("path"));
            } else {
                return input;
            }
        } else {
            return input;
        }
    }

    /**
     * StrutsConfigのこのアクションの部分を返す。
     * @return StrutsConfigのこのアクションの部分
     */
    public String getActionConfig() {
        return XmlUtils.xmlToString(basisElement);
    }

}
