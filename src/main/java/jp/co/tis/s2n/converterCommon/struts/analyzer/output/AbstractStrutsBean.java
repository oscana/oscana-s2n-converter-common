package jp.co.tis.s2n.converterCommon.struts.analyzer.output;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.converterCommon.util.XmlUtils;

/**
 * struts-config.xml, validation.xmlの情報を構造化するクラス群の共通部位。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class AbstractStrutsBean {
    protected Element basisElement;

    /**
     * 定数値のインライン展開。<br>
     * ${定数名}があれば、以下のXPathで定数値を取得して、インライン化する。<br>
     * 例：xPath = //constant/constant-name[text()='" + param + "']/parent::constant/constant-value[text()]
     * @param inputVal 入力値
     * @return 変換後の入力値
     */
    protected String inliningConst(String inputVal) {
        //validate.xml中の定数展開を実施
        Pattern p = Pattern.compile("\\$\\{([^\\}]*)\\}");
        Matcher m = p.matcher(inputVal);
        if (m.matches()) {
            String param = m.group(1);
            String value = XmlUtils.findStringFromElement(
                    "//constant/constant-name[text()='" + param + "']/parent::constant/constant-value[text()]",
                    this.basisElement);
            if (!StringUtils.isEmpty(value)) {
                //定数あり
                inputVal = m.replaceAll(StringUtils.escapeForRegexpReplacementValue(value));
            }
        }
        return inputVal;
    }

}
