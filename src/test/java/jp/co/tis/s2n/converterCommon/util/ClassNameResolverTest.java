package jp.co.tis.s2n.converterCommon.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * {@link ClassNameResolver}のテスト。
 *
 */
public class ClassNameResolverTest {

    /**
     * resolveFullNameメソッドのテスト０１
     */
    @Test
    public void testResolveFullName01() {
        List<String> list = new ArrayList<>();
        list.add("test1.Test01");
        list.add("test2.Test02");
        list.add("test3.*");
        ClassNameResolver cnr = new ClassNameResolver("test", list);
        assertEquals("tt.Test01", cnr.resolveFullName("tt.Test01"));

    }

    /**
     * resolveFullNameメソッドのテスト０２
     */
    @Test
    public void testResolveFullName02() {
        List<String> list = new ArrayList<>();
        list.add("test1.Test01");
        list.add("test2.Test02");
        list.add("test3.*");
        ClassNameResolver cnr = new ClassNameResolver("test", list);
        assertEquals("test2.Test02", cnr.resolveFullName("Test02"));
    }

    /**
     * resolveFullNameメソッドのテスト０３
     */
    @Test
    public void testResolveFullName03() {
        List<String> list = new ArrayList<>();
        list.add("test1.Test01");
        list.add("test2.Test02");
        list.add("test3.Test02");
        list.add("test3.*");
        ClassNameResolver cnr = new ClassNameResolver("test", list);
        try {
            cnr.resolveFullName("Test02");
            fail();
        } catch (RuntimeException e) {
            assertEquals("Test02 - 2つ以上のインポートがあるため、パッケージ名が推定できません。", e.getMessage());
        }
    }

    /**
     * resolveFullNameメソッドのテスト０４
     */
    @Test
    public void testResolveFullName04() {
        List<String> list = new ArrayList<>();
        list.add("test1.Test01");
        list.add("test2.Test02");
        ClassNameResolver cnr = new ClassNameResolver("test", list);
        assertEquals("test.Test03", cnr.resolveFullName("Test03"));
    }

    /**
     * resolveFullNameメソッドのテスト０５
     */
    @Test
    public void testResolveFullName05() {
        List<String> list = new ArrayList<>();
        list.add("test1.Test01");
        list.add("test2.Test02");
        list.add("test3.*");
        ClassNameResolver cnr = new ClassNameResolver("test", list);
        try {
            cnr.resolveFullName("Test03");
            fail();
        } catch (RuntimeException e) {
            assertEquals("ワイルドカードインポートがあるため、パッケージ名が推定できません。", e.getMessage());
        }
    }
}
