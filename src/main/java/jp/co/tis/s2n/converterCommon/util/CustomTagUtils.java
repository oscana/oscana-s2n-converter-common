package jp.co.tis.s2n.converterCommon.util;

/**
 * カスタムタグについての情報定義。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class CustomTagUtils {
    /**
     *
     * カスタムタグについての情報定義
     *
     */
    public enum CustomTagType {
        /** * JSPカスタムタグではない*/
        NotCustomTag,
        /** * JSPカスタムタグの開始タグ*/
        StartTag,
        /** * JSPカスタムタグの終了タグ*/
        EndTag,
        /** * JSPカスタムタグの空要素タグ*/
        EmptyTag;
    }

    /**
     * 文字列がJSPカスタムタグであるかをチェックする。
     * @param paramsString パラメータ
     * @return CustomTagType
     */
    public static CustomTagType evalAsJSPTag(String paramsString) {

        if (paramsString.matches("^<\\w+:\\w+.*\\/>$")) {
            return CustomTagType.EmptyTag;
        } else if (paramsString.matches("^<\\/\\w+:\\w+.*>$")) {
            return CustomTagType.EndTag;
        } else if (paramsString.matches("^<\\w+:\\w+.*>$")) {
            return CustomTagType.StartTag;
        } else {
            return CustomTagType.NotCustomTag;
        }
    }
}
