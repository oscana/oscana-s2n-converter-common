package jp.co.tis.s2n.converterCommon.struts.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import jp.co.tis.s2n.converterCommon.struts.analyzer.output.Route;
import jp.co.tis.s2n.converterCommon.util.StringUtils;

/**
 * Nablarchのルート情報(routes.xml)を自動生成するGenerator。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class NablarchRoutingFileGenerator {

    /**
     * Route.xmlファイルを保存する。
     * @param mappedList リスト
     * @param saveFileName ファイル
     * @param convertMode 変換モード
     * @throws IOException 例外
     */
    public static void saveRoutingFile(Map<String, Route> mappedList, File saveFileName, int convertMode)
            throws IOException {
        File outDir = saveFileName.getParentFile();
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        FileWriter fw = new FileWriter(saveFileName);
        createReoutes(mappedList, fw, convertMode);
        fw.close();
    }

    /**
     * routes.vmを読み込んでroute.xmlファイルのデータを作成する。
     * @param mappedList リスト
     * @param sw ライター
     * @param convertMode 変換モード
     * @throws IOException 例外
     */
    public static void createReoutes(Map<String, Route> mappedList, Writer sw, int convertMode) throws IOException {

        //Velocityの初期化
        VelocityEngine engine = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine.init(p);
        //Velocityコンテキストに値を設定
        VelocityContext context = new VelocityContext();
        context.put("routes", mappedList);
        context.put("StringUtils", new StringUtils());
        context.put("convertMode", convertMode);

        //テンプレートの作成
        Template template = engine.getTemplate("templete/routes.vm", "EUC-JP");
        //テンプレートとマージ
        template.merge(context, sw);
        //マージしたデータはWriterオブジェクトであるswが持っているのでそれを文字列として出力
        sw.flush();

    }
}
