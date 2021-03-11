package jp.co.tis.s2n.converterCommon.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@link CustomTagUtils}のテスト。
 *
 */
public class CustomTagUtilsTest {

    @Test
    public void testIsJspTag() {
        assertEquals(CustomTagUtils.CustomTagType.StartTag, CustomTagUtils.evalAsJSPTag("<jsp:bean a=\"aa\">"));
        assertEquals(CustomTagUtils.CustomTagType.StartTag, CustomTagUtils.evalAsJSPTag("<jsp:bean>"));
        assertEquals(CustomTagUtils.CustomTagType.EndTag, CustomTagUtils.evalAsJSPTag("</jsp:bean a=\"aa\">"));
        assertEquals(CustomTagUtils.CustomTagType.EndTag, CustomTagUtils.evalAsJSPTag("</jsp:bean>"));
        assertEquals(CustomTagUtils.CustomTagType.EmptyTag, CustomTagUtils.evalAsJSPTag("<jsp:bean a=\"aa\"/>"));
        assertEquals(CustomTagUtils.CustomTagType.EmptyTag, CustomTagUtils.evalAsJSPTag("<jsp:bean/>"));
        assertEquals(CustomTagUtils.CustomTagType.EmptyTag, CustomTagUtils.evalAsJSPTag("<jsp:bean a=\"aa\" />"));
        assertEquals(CustomTagUtils.CustomTagType.EmptyTag, CustomTagUtils.evalAsJSPTag("<jsp:bean />"));

        assertEquals(CustomTagUtils.CustomTagType.NotCustomTag, CustomTagUtils.evalAsJSPTag("<jsp:bean"));
        assertEquals(CustomTagUtils.CustomTagType.NotCustomTag, CustomTagUtils.evalAsJSPTag("jsp:bean"));
        assertEquals(CustomTagUtils.CustomTagType.NotCustomTag, CustomTagUtils.evalAsJSPTag("</br>"));
        assertEquals(CustomTagUtils.CustomTagType.NotCustomTag, CustomTagUtils.evalAsJSPTag("<bean a=\\\"aa\\\" >"));
        assertEquals(CustomTagUtils.CustomTagType.NotCustomTag, CustomTagUtils.evalAsJSPTag("<//jsp:bean>"));

    }

}
