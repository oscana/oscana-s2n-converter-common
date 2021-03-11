package jp.co.tis.s2n.converterCommon.struts.analyzer.output;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jp.co.tis.s2n.converterCommon.log.LogUtils;
import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzeResult;
import jp.co.tis.s2n.converterCommon.struts.analyzer.StrutsAnalyzer;

/**
*
* {@link StrutsAction}のテスト。
*
*/
public class StrutsActionTest {

    File strutsConfig;
    File validation;
    StrutsAnalyzeResult result;

    @Before
    public void setUp() throws Exception {

        LogUtils.init();

        strutsConfig = new File("src/test/resources/strutsExample/validator/struts-config.xml");
        validation = new File("src/test/resources/strutsExample/validator/validation.xml");
        result = StrutsAnalyzer.analyzeStrutsResource(strutsConfig, validation, "validator", "");
    }

    /**
     * inliningConstメソッドのテスト
     */
    @Test
    public void testInliningConst() {
        List<StrutsAction> actionList = result.getActionList();
        assertEquals("test", actionList.get(0).inliningConst("test"));
        assertEquals("${test}", actionList.get(0).inliningConst("${test}"));
    }
}
