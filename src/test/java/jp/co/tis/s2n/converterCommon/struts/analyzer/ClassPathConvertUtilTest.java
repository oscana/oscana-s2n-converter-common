package jp.co.tis.s2n.converterCommon.struts.analyzer;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.opencsv.exceptions.CsvException;

/**
*
* {@link ClassPathConvertUtil}のテスト。
*
*/
public class ClassPathConvertUtilTest {

    /**
     * addImprtメソッドのテスト
     * @throws CsvException
     * @throws IOException
     */
    @Test
    public void testAddImprt() throws IOException, CsvException {
        ClassPathConvertUtil.loadMappingFile("src/main/resources/ClassPathConvert.csv");
        ClassPathConvertUtil cpc = ClassPathConvertUtil.getInstance();
        cpc.resetMap();
        cpc.addImprt("org.seasar.framework.beans.BeanDesc");
        cpc.getAdditionalImport();
        assertEquals("oscana.s2n.seasar.framework.beans.BeanDesc", cpc.getAdditionalImport().get(0));

        cpc.addImprt("javax.persistence.MappedSuperclass");
        assertEquals("javax.persistence.MappedSuperclass", cpc.getAdditionalImport().get(1));

        cpc.addImprt("test");
        assertEquals("test", cpc.getAdditionalImport().get(2));

        cpc.addImprt("javax.persistence.MappedSuperclass");
        assertEquals(3, cpc.getAdditionalImport().size());
    }
}
