package jp.co.tis.s2n.converterCommon.struts.analyzer.output;

import static org.junit.Assert.*;

import org.junit.Test;

/**
*
* {@link ActionForward}のテスト。
*
*/
public class ActionForwardTest {

    /**
     * getParentActionメソッドのテスト
     */
    @Test
    public void testGetParentAction() {
        ActionForward af = new ActionForward(null, null);
        assertNull(af.getParentAction());
    }
}
