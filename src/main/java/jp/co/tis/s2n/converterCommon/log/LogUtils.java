package jp.co.tis.s2n.converterCommon.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 共通ロガー。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class LogUtils {

    private static Logger myLogger;

    /**
     * 初期化処理。
     * @throws Exception 例外
     */
    public static synchronized void init() throws Exception {
        if (myLogger == null) {
            Logger logger = LogManager.getLogger();
            myLogger = logger;
        }
    }

    /**
     * 情報をログに出力する。
     *
     * @param message メッセージ
     */
    public static synchronized void info(String message) {
        myLogger.info(message);
    }

    /**
     * 警告メッセージをログに出力する。
     * @param message メッセージ
     */
    public static synchronized void warn(String message) {
        myLogger.warn(message);
    }

    /**
     * 警告メッセージをログに出力する。
     *
     * @param filename ファイル名
     * @param className クラス名
     * @param message メッセージ
     * @param e 例外
     */
    public static synchronized void warn(String filename, String className, String message, Throwable e) {
        myLogger.warn(filename + "(" + className + "):" + message, e);
    }

    /**
     * 警告メッセージをログに出力する。
     *
     * @param filename ファイル名
     * @param message メッセージ
     * @param e throwable
     */
    public static synchronized void warn(String filename, String message, Throwable e) {
        myLogger.warn(filename + ":" + message, e);
    }

    /**
     * 警告メッセージをログに出力する。
     *
     * @param filename ファイル名
     * @param message メッセージ
     */
    public static synchronized void warn(String filename, String message) {
        myLogger.warn(filename + ":" + message);
    }

    /**
     * 警告メッセージをログに出力する。
     *
     * @param message メッセージ
     * @param e throwable
     */
    public static void warn(String message, Throwable e) {
        myLogger.warn(message, e);
    }

}
