package jp.co.tis.s2n.converterCommon.struts.analyzer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.ActionForward;
import jp.co.tis.s2n.converterCommon.struts.analyzer.output.StrutsAction;

/**
 * {@link StrutsAnalyzer}のテスト。
 *
 */
public class StrutsAnalyzerTest {

    File strutsConfig;
    File validation;
    StrutsAnalyzeResult result;

    /**
     * 初期化。
     * @throws Exception 例外
     */
    @Before
    public void setUp() throws Exception {

        LogUtils.init();

        strutsConfig = new File("src/test/resources/strutsExample/validator/struts-config.xml");
        validation = new File("src/test/resources/strutsExample/validator/validation.xml");
        result = StrutsAnalyzer.analyzeStrutsResource(strutsConfig, validation, "validator", "");
    }

    /**
     * StrutsAnalyzerクラスの一部メソッドのテスト。
     * @throws XPathExpressionException 例外
     * @throws SAXException 例外
     * @throws IOException 例外
     * @throws ParserConfigurationException 例外
     * @throws NoSuchMethodException 例外
     * @throws SecurityException 例外
     * @throws IllegalAccessException 例外
     * @throws IllegalArgumentException 例外
     * @throws InvocationTargetException 例外
     */
    @Test
    public void testAnalyze() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException,
            NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        List<StrutsAction> r = result.getActionList();
        assertEquals(r.size(), 9);

        //Action
        StrutsAction r1 = r.get(1);

        assertNull(r1.getType());
        assertEquals("/registration", r1.getPath());
        assertEquals("/registration.jsp", r1.getForward());

        StrutsAction r2 = r.get(2);
        assertEquals("registrationForm", r2.getName());
        assertEquals("org.apache.struts.webapp.validator.RegistrationAction", r2.getType());

        StrutsAction r5 = r.get(5);
        assertEquals("request", r5.getScope());
        assertEquals("false", r5.getValidate());

        StrutsAction r6 = r.get(6);
        assertEquals("localeForm", r6.getName());

        //ActionForward
        assertEquals(3, r2.getForwardList().size());
        assertEquals("/registration.do", r2.getForwardList().get("input").getPath());
        assertEquals("/index.jsp", r2.getForwardList().get("success").getPath());
        assertEquals(4, r5.getForwardList().size());
        assertEquals("/multiRegistration2.jsp", r5.getForwardList().get("input2").getPath());
        assertEquals("/welcome.do", r5.getForwardList().get("success").getPath());
        assertEquals("/multiRegistration2.jsp", r5.getForwardList().get("input2").getPath());

        //Form
        assertEquals("registrationForm", r2.getForm().getName());
        assertEquals("org.apache.struts.webapp.validator.RegistrationForm", r2.getForm().getType());
        assertEquals("org.apache.struts.action.DynaActionForm", r6.getForm().getType());

        //Validate
        assertEquals(8, r2.getForm().getFieldList().size());

        //Field
        assertEquals("lastName", r2.getForm().getFieldList().get("lastName").getProperty());
        assertEquals("required", r2.getForm().getFieldList().get("addr").getDepends());

        //Field-Arg
        assertEquals(2, r2.getForm().getFieldList().get("firstName").getArgList().size());
        assertEquals("registrationForm.firstname.displayname", r2.getForm().getFieldList().get("firstName").getArgList()
                .get("registrationForm.firstname.displayname").getKey());
        assertEquals("minlength",
                r2.getForm().getFieldList().get("firstName").getArgList().get("${var:minlength}").getName());
        assertEquals("${var:minlength}",
                r2.getForm().getFieldList().get("firstName").getArgList().get("${var:minlength}").getKey());
        assertEquals("0", r2.getForm().getFieldList().get("firstName").getArgList()
                .get("registrationForm.firstname.displayname").getPosition());
        assertEquals(null, r2.getForm().getFieldList().get("firstName").getArgList()
                .get("registrationForm.firstname.displayname").getResource());

        //Field-Var
        assertEquals(2, r2.getForm().getFieldList().get("lastName").getVarList().size());
        assertEquals(0, r2.getForm().getFieldList().get("addr").getVarList().size());
        assertEquals("mask", r2.getForm().getFieldList().get("lastName").getVarList().get("mask").getVarName());
        assertEquals("^[a-zA-Z]*$", r2.getForm().getFieldList().get("lastName").getVarList().get("mask").getVarValue());

    }

    /**
     * StrutsAnalyzerクラスの一部メソッドのテスト。
     * @throws NoSuchMethodException 例外
     * @throws SecurityException 例外
     * @throws IllegalAccessException 例外
     * @throws IllegalArgumentException 例外
     * @throws InvocationTargetException 例外
     */
    @Test
    public void testAdvanced() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals("/registration-submit",
                DataUtils.findByKeyFromList("name", "registrationForm", result.getActionList()).getPath());
        assertEquals("registrationForm",
                result.getFormList().get("org.apache.struts.webapp.validator.RegistrationForm").getName());
    }

    /**
     * resolveForwardメソッドのテスト
     */
    @Test
    public void testResolveForward() {
        String actual01 = StrutsAnalyzer.resolveForward("test.do", result);
        assertEquals("test.do", actual01);

        String actual02 = StrutsAnalyzer.resolveForward(null, result);
        assertEquals(null, actual02);

        String actual03 = StrutsAnalyzer.resolveForward("test", result);
        assertEquals("test", actual03);
    }

    /**
     * findForwardByNameメソッドのテスト
     */
    @Test
    public void testFindForwardByName() {
        StrutsAnalyzeResult[] analyzeResults = new StrutsAnalyzeResult[1];
        analyzeResults[0] = result;
        List<ActionForward> actual01 = StrutsAnalyzer.findForwardByName(analyzeResults, "test");
        assertEquals(0, actual01.size());

        List<ActionForward> actual02 = StrutsAnalyzer.findForwardByName(analyzeResults, "home");
        assertEquals(1, actual02.size());

    }

    /**
     * findActionByForwardPathメソッドのテスト
     */
    @Test
    public void testFindActionByForwardPath() {
        StrutsAnalyzeResult[] analyzeResults = new StrutsAnalyzeResult[1];
        analyzeResults[0] = result;
        List<StrutsAction> actual01 = StrutsAnalyzer.findActionByForwardPath(analyzeResults, "test.jsp");
        assertEquals(0, actual01.size());
        List<StrutsAction> actual02 = StrutsAnalyzer.findActionByForwardPath(analyzeResults, "/validator/index.jsp");
        assertEquals(1, actual02.size());

    }
}
