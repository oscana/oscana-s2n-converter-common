package jp.co.tis.s2n.converterCommon.log;

import org.junit.Before;
import org.junit.Test;

/**
*
* {@link LogUtils}のテスト。
*
*/
public class LogUtilsTest {

    /**
     * 初期化
     */
    @Before
    public void setUp() throws Exception {
        LogUtils.init();
    }

    /**
     * LogUtils.warn(String, Throwable)メソッドのテスト
     */
    @Test
    public void testWarn01() {
        LogUtils.warn("test", new RuntimeException("test"));
    }

    /**
    * LogUtils.warn(String, String, Throwable)メソッドのテスト
    */
    @Test
    public void testWarn02() {
        LogUtils.warn("test.txt", "test", new RuntimeException("test"));
    }

    /**
     * LogUtils.warn(String)メソッドのテスト
     */
    @Test
    public void testWarn03() {
        LogUtils.warn("test.txt");
    }

    /**
     * LogUtils.warn(String, String)メソッドのテスト
     */
    @Test
    public void testWarn04() {
        LogUtils.warn("test.txt", "text");
    }

    /**
    * LogUtils.warn(String, String, String, Throwable)メソッドのテスト
    */
    @Test
    public void testWarn05() {
        LogUtils.warn("test.txt", "test", "test", new RuntimeException("test"));
    }

}
