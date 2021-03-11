package jp.co.tis.s2n.converterCommon.statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.opencsv.CSVWriter;

/**
 * 統計情報出力処理の共通部位。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class StatisticsBase {

    /**
     * データをCSVに出力する。<br>
     * 出力の際には、dataの全publicメンバが自動的に抽出される。
     * @param file ファイル名
     * @param data 出力データ
     * @throws UnsupportedEncodingException unsupportedEncodingException例外
     * @throws FileNotFoundException fileNotFoundException例外
     * @throws IllegalAccessException illegalAccessException例外
     * @throws IOException iOException例外
     */
    public static void exportObject2Csv(File file, Object data)
            throws UnsupportedEncodingException, FileNotFoundException, IllegalAccessException, IOException {
        ArrayList<Object> l = new ArrayList<>();
        l.add(data);

        exportCollection2Csv(file, l);
    }

    /**
     * データリストをCSVに出力する。<br>
     * Collection datasに含まれるすべてのdataを出力する。（1行１data)
     * 出力の際には、dataの全publicメンバが自動的に抽出される。
     * @param file ファイル名
     * @param datas 出力するデータが格納されたCollection
     * @throws UnsupportedEncodingException unsupportedEncodingException例外
     * @throws FileNotFoundException fileNotFoundException例外
     * @throws IllegalAccessException illegalAccessException例外
     * @throws IOException iOException例外
     */
    @SuppressWarnings("rawtypes")
    public static void exportCollection2Csv(File file, Collection datas)
            throws UnsupportedEncodingException, FileNotFoundException, IllegalAccessException, IOException {

        try (CSVWriter cpw = new CSVWriter(
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "MS932")))) {

            List<String[]> outStrList = new ArrayList<String[]>();
            int line = 0;
            for (Object instance : datas) {

                List<String> keyList = new ArrayList<>();
                List<String> valueList = new ArrayList<>();

                Field[] fields = instance.getClass().getFields();
                for (Field field : fields) {
                    //Publicフィールドだけ出力
                    if (Modifier.isPublic(field.getModifiers())) {
                        keyList.add(field.getName());
                        valueList.add(String.valueOf(field.get(instance)));
                    }
                }
                //先頭行にはラベルを出力
                if (line == 0) {
                    outStrList.add(keyList.toArray(new String[0]));
                }
                outStrList.add(valueList.toArray(new String[0]));

                line++;
            }

            cpw.writeAll(outStrList);
        }
    }

}
