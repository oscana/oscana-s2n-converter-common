package jp.co.tis.s2n.converterCommon.struts.analyzer.output;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.co.tis.s2n.converterCommon.util.StringUtils;
import jp.co.tis.s2n.converterCommon.util.XmlUtils;

/**
 * struts-config.xml, validation.xml内にあるFormを構造化したクラス。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class StrutsForm {

    private Element srcStrutsConfigFormBean;

    private StrutsAction srcStrutsAtion;
    private Document srcValidationConfig;

    //所属メンバ
    private Map<String, Field> fieldList;

    public StrutsForm(Element strutsConfigFormBean, StrutsAction strutsAction, Document strutsConfig,
            Document validation) {
        this.srcStrutsConfigFormBean = strutsConfigFormBean;

        this.srcStrutsAtion = strutsAction;
        this.srcValidationConfig = validation;

        importFields(getName());

    }

    /**
     * validate.xml中のFieldを取り込む（extednsの再起読み込み対応）。
     * @param formName form名
     */
    private void importFields(String formName) {
        Element validateFormElement = XmlUtils.findElementFromDocument("//form[@name='" + formName + "']",
                this.srcValidationConfig);
        Element srcValidateForm = validateFormElement;

        if (srcValidateForm != null) {
            //ValidatorがあるときはField解析する
            this.fieldList = new LinkedHashMap<String, StrutsForm.Field>();
            NodeList fieldList1 = XmlUtils.findNodeListFromElement("field", srcValidateForm);
            for (int i = 0; i < fieldList1.getLength(); i++) {
                Element elm = (Element) fieldList1.item(i);
                Field nField = new Field(elm);
                this.fieldList.put(nField.getProperty(), nField);
            }

            //extednsがあれば再帰的に読み込む
            String frmExtend = srcValidateForm.getAttribute("extends");
            if (!StringUtils.isEmpty(frmExtend)) {
                importFields(frmExtend);
            }

        }
    }

    /**
     * StrutsActionオブジェクトを取得する。
     * @return StrutsActionオブジェクト
     */
    public StrutsAction getAction() {
        return this.srcStrutsAtion;
    }

    /**
     * フィールドリストを取得する。
     * @return フィールドリスト
     */
    public Map<String, Field> getFieldList() {
        return fieldList;
    }

    /**
     * name属性の値を取得する。
     * @return name属性の値
     */
    public String getName() {
        return StringUtils.normalizeNull(srcStrutsConfigFormBean.getAttribute("name"));
    }

    /**
     * type属性の値を取得する。
     * @return type属性の値
     */
    public String getType() {
        return StringUtils.normalizeNull(srcStrutsConfigFormBean.getAttribute("type"));
    }

    public class Field extends AbstractStrutsBean {

        private Map<String, Msg> msgList;
        private Map<String, Arg> argList;
        private Map<String, Var> varList;

        public Field(Element formFieldElemant) {
            this.basisElement = formFieldElemant;

            this.msgList = new LinkedHashMap<String, StrutsForm.Field.Msg>();
            this.argList = new LinkedHashMap<String, StrutsForm.Field.Arg>();
            this.varList = new LinkedHashMap<String, StrutsForm.Field.Var>();
            NodeList msgList = XmlUtils.findNodeListFromElement("msg", this.basisElement);
            NodeList argList = XmlUtils.findNodeListFromElement("arg", this.basisElement);
            NodeList varList = XmlUtils.findNodeListFromElement("var", this.basisElement);
            for (int i = 0; i < msgList.getLength(); i++) {
                Element elm = (Element) msgList.item(i);
                Msg nMsg = new Msg(elm);
                this.msgList.put(nMsg.getName(), nMsg);
            }
            for (int i = 0; i < argList.getLength(); i++) {
                Element elm = (Element) argList.item(i);
                Arg nArg = new Arg(elm);
                this.argList.put(nArg.getKey(), nArg);
            }
            for (int i = 0; i < varList.getLength(); i++) {
                Element elm = (Element) varList.item(i);
                Var nVar = new Var(elm);
                this.varList.put(nVar.getVarName(), nVar);
            }

        }

        /**
         * Arg属性の値を持つマップを取得する。
         * @return Arg属性の値を持つマップ
         */
        public Map<String, Arg> getArgList() {
            return argList;
        }

        /**
         * Var属性の値を持つマップを取得する。
         * @return Var属性の値を持つマップ
         */
        public Map<String, Var> getVarList() {
            return varList;
        }

        /**
         * property属性の値を取得する。
         * @return property属性の値
         */
        public String getProperty() {
            return StringUtils.normalizeNull(basisElement.getAttribute("property"));
        }

        /**
         * depends属性の値を取得する。
         * @return depends属性の値
         */
        public String getDepends() {
            return StringUtils.normalizeNull(basisElement.getAttribute("depends"));
        }

        /**
         * depends属性値のリストを取得する。
         * @return depends属性の値のリスト
         */
        public List<String> getDependList() {
            ArrayList<String> retList = new ArrayList<String>();
            if (getDepends() != null) {
                String[] retAr = getDepends().split(",");
                for (int i = 0; i < retAr.length; i++) {
                    retList.add(retAr[i]);
                }
            } else {
                //空のリストを返す
            }
            return retList;
        }

        public class Msg extends AbstractStrutsBean {

            public Msg(Element elm) {
                this.basisElement = elm;
            }

            /**
             * name属性の値を取得する。
             * @return name属性の値
             */
            public String getName() {
                return StringUtils.normalizeNull(basisElement.getAttribute("name"));
            }

        }

        public class Arg extends AbstractStrutsBean {
            public Arg(Element elm) {
                this.basisElement = elm;
            }

            /**
             * key属性の値を取得する。
             * @return key属性の値
             */
            public String getKey() {
                return StringUtils.normalizeNull(basisElement.getAttribute("key"));
            }

            /**
             * name属性の値を取得する。
             * @return name属性の値
             */
            public String getName() {
                return StringUtils.normalizeNull(basisElement.getAttribute("name"));
            }

            /**
             * resource属性の値を取得する。
             * @return resource属性の値
             */
            public String getResource() {
                return StringUtils.normalizeNull(basisElement.getAttribute("resource"));
            }

            /**
             * position属性の値を取得する。
             * @return position属性の値
             */
            public String getPosition() {
                return StringUtils.normalizeNull(basisElement.getAttribute("position"));
            }
        }

        public class Var extends AbstractStrutsBean {
            public Var(Element elm) {
                this.basisElement = elm;
            }

            /**
             * var-name属性の値を取得する。
             * @return var-name属性の値
             */
            public String getVarName() {
                return StringUtils.normalizeNull(XmlUtils.findStringFromElement("var-name", basisElement));
            }

            /**
             * var-value属性の値を取得する。
             * @return var-value属性の値
             */
            public String getVarValue() {
                String retVar = StringUtils.normalizeNull(XmlUtils.findStringFromElement("var-value", basisElement));
                retVar = inliningConst(retVar);
                return retVar;
            }

        }

    }

}
