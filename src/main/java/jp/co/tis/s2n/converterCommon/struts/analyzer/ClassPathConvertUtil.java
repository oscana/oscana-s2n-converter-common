package jp.co.tis.s2n.converterCommon.struts.analyzer;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import jp.co.tis.s2n.converterCommon.util.StringUtils;

/**
 * クラスパスを変換するユーティリティ。<br>
 *
 * このクラスはシングレトンである、スレッドセーフではないので単一スレッドからの利用を想定する。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ClassPathConvertUtil {

    //変換テーブル
    private static Map<String, String> strutsImportConvertMap;

    public static Map<String, String> BASE_CLASSNAME_CONVERT_MAP;

    public static Map<String, String> SERVLET_CLASSNAME_CONVERT_MAP;

    private static String DEFAULT_MAPPING_FILE = "ClassPathConvert.csv";

    /**
     * StrutsにおけるFormのBaseクラス、置換前と置換後Map。
     * @param fileName ファイル名
     * @throws IOException IOException例外
     * @throws CsvException CsvException例外
     */
    public static void loadBaseClassnameConvertMap(String fileName) throws IOException, CsvException {
        Reader fileReader = null;
        CSVReader csvReader = null;
        try {
            fileReader = new InputStreamReader( ClassLoader.getSystemResourceAsStream(fileName));
            csvReader = new CSVReader(fileReader);
            List<String[]> records = csvReader.readAll();

            BASE_CLASSNAME_CONVERT_MAP = new HashMap<String, String>();
            for (String[] rec : records) {
                BASE_CLASSNAME_CONVERT_MAP.put(rec[0], rec[1]);
            }

        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
            if (csvReader != null) {
                csvReader.close();
            }
        }
    }

    /**
     * StrutsにおけるFormのBaseクラス、置換前と置換後Map。
     * @param fileName ファイル名
     * @throws IOException IOException例外
     * @throws CsvException CsvException例外
     */
    public static void loadServletClassnameConvertMap(String fileName) throws IOException, CsvException {
        Reader fileReader = null;
        CSVReader csvReader = null;
        try {
            fileReader = new InputStreamReader( ClassLoader.getSystemResourceAsStream(fileName));
            csvReader = new CSVReader(fileReader);
            List<String[]> records = csvReader.readAll();

            SERVLET_CLASSNAME_CONVERT_MAP = new HashMap<String, String>();
            for (String[] rec : records) {
                SERVLET_CLASSNAME_CONVERT_MAP.put(rec[0], rec[1]);
            }

        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
            if (csvReader != null) {
                csvReader.close();
            }
        }
    }

    /**
     * マッピングファイルを読み込み、変換テーブルを構築する。
     * @param fileName ファイル名
     * @throws IOException IOException例外
     * @throws CsvException CsvException例外
     */
    public static void loadMappingFile(String fileName) throws IOException, CsvException {
        Reader reader = null;
        CSVReader csvReader = null;
        try {
            if (fileName != null) {
                reader = new FileReader(fileName);
            } else {
                reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(DEFAULT_MAPPING_FILE));
            }

            csvReader = new CSVReader(reader);

            List<String[]> records = csvReader.readAll();

            strutsImportConvertMap = new HashMap<String, String>();
            for (String[] rec : records) {
                if (StringUtils.isEmpty(rec[1]) || "null".equals(rec[1].trim())) {
                    rec[1] = null;
                }
                strutsImportConvertMap.put(rec[0], rec[1]);
            }

        } finally {
            if (reader != null) {
                reader.close();
            }

            if (csvReader != null) {
                csvReader.close();
            }
        }

    }

    private static ClassPathConvertUtil instance;

    private Map<String, String> convertRules;
    private List<String> additionalImport = new ArrayList<>();

    private ClassPathConvertUtil() {
        this.convertRules = strutsImportConvertMap;
    }

    /**
     * 唯一のインスタンスを返す。
     * @return ClassPathConvertUtilのオブジェクト
     */
    public static ClassPathConvertUtil getInstance() {
        if (instance == null) {
            instance = new ClassPathConvertUtil();
        }
        return instance;
    }

    /**
     * マップをリセットする。
     */
    public void resetMap() {
        this.convertRules = strutsImportConvertMap;
        this.additionalImport.clear();

    }

    /**
     * 変換用import文のマップを取得する。
     * @return 変換用import文のマップ
     */
    public Map<String, String> getConverterRules() {
        return this.convertRules;
    }

    /**
     * import文リストを取得する。
     * @return import文リスト
     */
    public List<String> getAdditionalImport() {
        return this.additionalImport;
    }

    /**
     * インポートへ追加したいクラス名を登録する。
     * @param additional 登録したいクラスの完全修飾クラス名
     */
    public void addImprt(String additional) {

        if (this.convertRules.containsKey(additional)) {
            String convertToString = this.convertRules.get(additional);
            if (!StringUtils.isEmpty(convertToString)) {
                additional = convertToString;
            }
        }

        if (!this.additionalImport.contains(additional)) {
            this.additionalImport.add(additional);
        }
    }

}
