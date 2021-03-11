package jp.co.tis.s2n.converterCommon.statistics;

/**
 * 統計情報を蓄積するツール。<br>
 *
 * このクラスはシングレトンである、
 * スレッドセーフではないので単一スレッドからの利用を想定する。
 *
 * @author Fumihiko Yamamoto
 */
public class CommonOtherStatistics {

    //シングレトン化
    private static CommonOtherStatistics instance;

    private CommonOtherStatistics() {
    }

    public static CommonOtherStatistics getInstance() {
        if (instance == null) {
            instance = new CommonOtherStatistics();
        }
        return instance;
    }

    // Configファイル変換できませんの件数
    public int noConvertConfig = 0;

}
