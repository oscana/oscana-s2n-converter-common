package jp.co.tis.s2n.converterCommon.util;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * {@link StringUtils}のテスト。
 *
 */
public class StringUtilsTest {

    @Test
    public void testCaplitalize() {
        assertEquals(null, StringUtils.capitalizeStr(null));
        assertEquals("", StringUtils.capitalizeStr(""));
        assertEquals("A", StringUtils.capitalizeStr("a"));
        assertEquals("A", StringUtils.capitalizeStr("A"));
        assertEquals("Is", StringUtils.capitalizeStr("is"));
        assertEquals("Is", StringUtils.capitalizeStr("Is"));
        assertEquals("IS", StringUtils.capitalizeStr("IS"));
        assertEquals("IS", StringUtils.capitalizeStr("iS"));
    }

    @Test
    public void testMkGetterName() {
        assertEquals("getUser", StringUtils.mkGetterName("user"));
        assertEquals("getClassName", StringUtils.mkGetterName("className"));

    }

    @Test
    public void testAddClassPackage() {
        assertEquals("java.lang.String", StringUtils.addClassPackage("java.lang", "String"));
        assertEquals("java.lang.String", StringUtils.addClassPackage("java.lang.", "String"));
        assertEquals("String", StringUtils.addClassPackage("", "String"));
        assertEquals("String", StringUtils.addClassPackage(null, "String"));
    }

    @Test
    public void testClassNameResolver() {
        ArrayList<String> lst = new ArrayList<>();
        lst.add("java.lang.String");
        lst.add("java.util.Hashtable");
        lst.add("jp.co.tis.sample.TestApp");

        ClassNameResolver cnr = new ClassNameResolver("jp.co.tis.test", lst);

        assertEquals("java.lang.String", cnr.resolveFullName("String"));
        assertEquals("jp.co.tis.sample.TestApp", cnr.resolveFullName("TestApp"));
        assertEquals("jp.co.tis.sample.B1", cnr.resolveFullName("jp.co.tis.sample.B1"));
        assertEquals("jp.co.tis.test.A1", cnr.resolveFullName("A1"));
    }

    @Test
    public void testRelativePath() {
        assertEquals("data", StringUtils.relativePath("C:\\ROOT\\Service\\", "C:\\ROOT\\Service\\data\\abc.java"));
        assertEquals("data", StringUtils.relativePath("C:\\ROOT\\Service", "C:\\ROOT\\Service\\data\\abc.java"));
        assertEquals("data\\windows",
                StringUtils.relativePath("C:\\ROOT\\Service\\", "C:\\ROOT\\Service\\data\\windows\\abc.java"));
        assertEquals("data", StringUtils.relativePath("/ROOT/Service/", "/ROOT/Service/data/abc.java"));
        assertEquals("data", StringUtils.relativePath("/ROOT/Service", "/ROOT/Service/data/abc.java"));

    }

    @Test
    public void testRelativeFilePath() {
        assertEquals("data\\abc.java",
                StringUtils.relativeFilePath("C:\\ROOT\\Service\\", "C:\\ROOT\\Service\\data\\abc.java"));
        assertEquals("data\\abc.java",
                StringUtils.relativeFilePath("C:\\ROOT\\Service", "C:\\ROOT\\Service\\data\\abc.java"));
        assertEquals("data\\windows\\abc.java",
                StringUtils.relativeFilePath("C:\\ROOT\\Service\\", "C:\\ROOT\\Service\\data\\windows\\abc.java"));
        assertEquals("data/abc.java", StringUtils.relativeFilePath("/ROOT/Service/", "/ROOT/Service/data/abc.java"));
        assertEquals("data/abc.java", StringUtils.relativeFilePath("/ROOT/Service", "/ROOT/Service/data/abc.java"));

    }

    @Test
    public void testDirectoryFromFilePath() {
        assertEquals("C:\\ROOT\\Service\\data",
                StringUtils.getDirectoryFromFilePath("C:\\ROOT\\Service\\data\\abc.java"));
        assertEquals("C:\\ROOT\\Service\\data", StringUtils.getDirectoryFromFilePath("C:\\ROOT\\Service\\data\\exec"));
        assertEquals("", StringUtils.getDirectoryFromFilePath("abc.java"));
        assertEquals("/home", StringUtils.getDirectoryFromFilePath("/home/root.sh"));
        assertEquals("", StringUtils.getDirectoryFromFilePath("root.sh"));
    }

    @Test
    public void testGetFileNameFromFilePath() {
        assertEquals("abc.java", StringUtils.getFileNameFromFilePath("C:\\ROOT\\Service\\data\\abc.java"));
        assertEquals("abc.java", StringUtils.getFileNameFromFilePath("\\abc.java"));
        assertEquals("abc.java", StringUtils.getFileNameFromFilePath("abc.java"));
        assertEquals("exec", StringUtils.getFileNameFromFilePath("C:\\ROOT\\Service\\data\\exec"));
        assertEquals("root.sh", StringUtils.getFileNameFromFilePath("/home/root.sh"));
        assertEquals("root.sh", StringUtils.getFileNameFromFilePath("/root.sh"));
        assertEquals("root.sh", StringUtils.getFileNameFromFilePath("root.sh"));

    }

    @Test
    public void testAddPath() {
        assertEquals("/parent/child", StringUtils.addPath("/parent", "child"));
        assertEquals("/parent/child", StringUtils.addPath("/parent", "/child"));
        assertEquals("/parent/child", StringUtils.addPath("/parent/", "child"));
        assertEquals("/parent/child", StringUtils.addPath("/parent/", "/child"));
        assertEquals("parent/child", StringUtils.addPath("parent", "child"));
        assertEquals("child", StringUtils.addPath(null, "child"));
    }

    @Test
    public void testDelQuote() {
        assertEquals("test", StringUtils.delQuote("\"test\""));
        assertEquals("test", StringUtils.delQuote("'test'"));
        assertEquals("test'", StringUtils.delQuote("\"test'\""));
        assertEquals("test\"", StringUtils.delQuote("'test\"'"));
        assertEquals("test", StringUtils.delQuote("test"));
        assertEquals(null, StringUtils.delQuote(null));
    }

    @Test
    public void testGetClassShortName() {
        assertEquals("TestDt", StringUtils.getClassShortName("jp.co.tis.TestDt"));
        assertEquals("TestABC", StringUtils.getClassShortName("TestABC"));
        assertEquals("RegistrationForm",
                StringUtils.getClassShortName("org.apache.struts.webapp.validator.RegistrationForm"));

    }

    @Test
    public void testEscapeRegexpValue() {
        assertEquals("aaa\\\\bbb\\$c", StringUtils.escapeForRegexpReplacementValue("aaa\\bbb$c"));
        assertEquals("aaa\\\\bbb", StringUtils.escapeForRegexpReplacementValue("aaa\\bbb"));
        assertEquals("aaabbb\\$c", StringUtils.escapeForRegexpReplacementValue("aaabbb$c"));
    }

    /**
     * toLowerCaseメソッドのテスト
     */
    @Test
    public void testToLowerCase() {
        assertEquals("aaa", StringUtils.toLowerCase("AAA"));
        assertEquals(null, StringUtils.toLowerCase(null));
    }

    /**
     * relativePathメソッドのテスト02
     */
    @Test
    public void testRelativePath02() {
        try {
            StringUtils.relativePath(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("子として指定したパスが、親と指定したパスの配下にありません。", e.getMessage());
        }
    }

    /**
     * relativePathメソッドのテスト0３
     */
    @Test
    public void testRelativePath03() {
        try {
            StringUtils.relativePath("test", "ds");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("子として指定したパスが、親と指定したパスの配下にありません。", e.getMessage());
        }
    }

    /**
     * relativePackagePathメソッドのテスト０１
     */
    @Test
    public void testRelativePackagePath01() {
        try {
            StringUtils.relativePackagePath(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("子として指定したクラス[]が、親と指定したパケージ[" + "null" + "]の配下にありません。", e.getMessage());
        }
    }

    /**
     * relativePackagePathメソッドのテスト０２
     */
    @Test
    public void testRelativePackagePath02() {
        try {
            StringUtils.relativePackagePath("test", "xx");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("子として指定したクラス[" + "xx" + "]が、親と指定したパケージ[" + "test" + "]の配下にありません。", e.getMessage());
        }
    }

    /**
     * relativePackagePathメソッドのテスト０３
     */
    @Test
    public void testRelativePackagePath03() {
        assertEquals("test", StringUtils.relativePackagePath("test", "test.test"));
        assertEquals("", StringUtils.relativePackagePath("test", "test"));
    }

    /**
     * relativeFilePathメソッドのテスト０１
     */
    @Test
    public void testRelativeFilePath01() {
        try {
            StringUtils.relativeFilePath(null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("子として指定したパスが、親と指定したパスの配下にありません。", e.getMessage());
        }
    }

    /**
     * relativeFilePathメソッドのテスト０２
     */
    @Test
    public void testRelativeFilePath02() {
        try {
            StringUtils.relativeFilePath("test", "xx");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("子として指定したパスが、親と指定したパスの配下にありません。", e.getMessage());
        }
    }

    /**
     * formatStrutsModulePathメソッドのテスト
     */
    @Test
    public void testFormatStrutsModulePath() {
        assertEquals("/AAA", StringUtils.formatStrutsModulePath("/AAA"));
        assertEquals("/AAA", StringUtils.formatStrutsModulePath("AAA"));
    }

    /**
     * addPathExtメソッドのテスト
     */
    @Test
    public void testAddPathExt() {
        assertEquals("TEST/AAA", StringUtils.addPathExt("TEST", "/AAA"));
    }

    /**
     * addPathメソッドのテスト０２
     */
    @Test
    public void testAddPath02() {
        assertEquals("TEST", StringUtils.addPath("TEST", ""));
    }

    /**
     * convertPathInternalForwardメソッドのテスト
     */
    @Test
    public void testConvertPathInternalForward() {
        assertEquals(null, StringUtils.convertPathInternalForward(null));
        assertEquals("test", StringUtils.convertPathInternalForward("test"));
        assertEquals("forward://test.do", StringUtils.convertPathInternalForward("test.do"));
    }

}
