package jp.co.tis.s2n.converterCommon.util;

/**
 * 文字列操作ユーティリティ。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class StringUtils {

    /**
     * 空文字をnullに変換する。
     * @param src パラメータ
     * @return 空文字の場合、nullを返す。そうでもない場合、そのまま返す。
     */
    public static String normalizeNull(String src) {
        if (src == null) {
            return null;
        } else {
            if (src.length() == 0) {
                return null;
            } else {
                return src;
            }
        }
    }

    /**
     * keyに対するGetterメソッドの名前を生成する。
     * @param key
     * @return Getterメソッドの名前
     */
    public static String mkGetterName(String key) {

        return "get" + capitalizeStr(key);
    }

    /**
     * キャピタライズを行う。（1文字目だけ大文字にする）
     * @param srcString パラメータ
     * @return 変更後の結果
     */
    public static String capitalizeStr(String srcString) {
        if (srcString == null) {
            return null;
        } else if (srcString.length() == 0) {
            return srcString;
        } else if (srcString.length() == 1) {
            return srcString.toUpperCase();
        } else {
            return srcString.substring(0, 1).toUpperCase() + srcString.substring(1);
        }
    }

    /**
     * nullチェック付き、小文字に変換する。
     * srcがnullの場合はnullを返す
     * @param src
     * @return
     */
    /**
     * nullチェック付きtoLowerCase
     * @param src パラメータ
     * @return srcがnullの場合はnullを返す。そうでもない場合、小文字にして返す。
     */
    public static String toLowerCase(String src) {
        if (src == null) {
            return src;
        } else {
            return src.toLowerCase();
        }
    }

    /**
     * パッケージ名とクラス名の連結をおこなう。間にピリオドが１つだけ来るように処理する。<br>
     * パッケージが空もしくはnullであればクラス名だけ返す。
     * @param packageName パッケージ名
     * @param className クラス名
     * @return フルクラス名
     */
    public static String addClassPackage(String packageName, String className) {
        if (StringUtils.isEmpty(packageName)) {
            return className;
        }
        if (packageName.endsWith(".")) {
            return packageName + className;
        } else {
            return packageName + "." + className;
        }
    }

    /**
     * parentとchildをスラッシュで結んだパスにする。
     * @param parent
     * @param child
     * @return
     */
    public static String addPath(String parent, String child) {
        if (StringUtils.isEmpty(child)) {
            return parent;
        }
        if (StringUtils.isEmpty(parent)) {
            return child;
        }

        if (parent.endsWith("/")) {
            if (child.startsWith("/")) {
                return parent + child.substring(1);
            } else {
                return parent + child;
            }
        } else {
            if (child.startsWith("/")) {
                return parent + child;
            } else {
                return parent + "/" + child;
            }
        }

    }

    /**
     * parentとchildをスラッシュで結んだパスにする。
     * @param parent 親のパス
     * @param child 子供のパス
     * @return パス
     */
    public static String addPathExt(String parent, String child) {
        return convertPathInternalForward(addPath(parent, child));
    }

    /**
     * Nablarchの内部フォーワードのパス変換を実施する。
     * @param path パス
     * @return srcがAction以外のパスであればそのまま返すが、そうでないならforward://を付与する
     */
    public static String convertPathInternalForward(String path) {
        if ((path != null) && (path.endsWith(".do"))) {
            path = "forward://" + path;
        }
        return path;
    }

    /**
     * 空文字かどうかを判断する。
     * @param src パラメータ
     * @return 文字列が空もしくはnullであればtrueを返す
     */
    public static boolean isEmpty(String src) {
        if (src == null) {
            return true;
        } else if (src.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ディレクトリparentPathからtargetFilePathに対する相対パスを取得する。<br>
     * targetFilePathがparentPathに含まれていない場合はエラー。
     * @param parentPath 親（先祖）のディレクトリパス
     * @param targetFilePath 相対パスを求める対象のファイルパス
     * @return ディレクトリを返す。ファイル名はカットする。
     */
    public static String relativePath(String parentPath, String targetFilePath) {
        if (targetFilePath != null) {
            if (targetFilePath.startsWith(parentPath)) {
                String targetDir = getDirectoryFromFilePath(targetFilePath);

                String ret = targetDir.substring(parentPath.length()).replaceAll("^[\\\\|/]", "").replaceAll("[\\\\/]$",
                        "");

                return ret;
            } else {
                throw new IllegalArgumentException("子として指定したパスが、親と指定したパスの配下にありません。");
            }
        } else {
            throw new IllegalArgumentException("子として指定したパスが、親と指定したパスの配下にありません。");
        }
    }

    /**
     * ディレクトリparentPathからtargetFilePathに対する相対パスを取得する。<br>
     * targetFilePathがparentPathに含まれていない場合はエラー。
     * @param parentPath 親（先祖）のディレクトリパス
     * @param targetFilePath 相対パスを求める対象のファイルパス
     * @return ファイル名まで含めた文字列を返す
     */
    public static String relativeFilePath(String parentPath, String targetFilePath) {
        if (targetFilePath != null) {
            if (targetFilePath.startsWith(parentPath)) {

                String ret = targetFilePath.substring(parentPath.length()).replaceAll("^[\\\\|/]", "").replaceAll(
                        "[\\\\/]$",
                        "");
                return ret;
            } else {
                throw new IllegalArgumentException("子として指定したパスが、親と指定したパスの配下にありません。");
            }
        } else {
            throw new IllegalArgumentException("子として指定したパスが、親と指定したパスの配下にありません。");
        }
    }

    /**
     * パケージparentPackageからtargetClassに対する相対パスを取得する。<br>
     * targetClassがparentPackageに含まれていない場合はエラー。
     * @param parentPackage 親（先祖）のパケージ
     * @param targetFilePath 相対パスを求める対象のファイルパス
     * @return 相対パス
     */
    public static String relativePackagePath(String parentPackage, String targetClass) {
        if (targetClass != null) {
            if (targetClass.startsWith(parentPackage)) {
                String ret = targetClass.substring(parentPackage.length());
                if (ret.startsWith(".")) {
                    return ret.substring(1);
                } else {
                    return ret;
                }
            } else {
                throw new IllegalArgumentException(
                        "子として指定したクラス[" + targetClass + "]が、親と指定したパケージ[" + parentPackage + "]の配下にありません。");
            }
        } else {
            throw new IllegalArgumentException("子として指定したクラス[]が、親と指定したパケージ[" + targetClass + "]の配下にありません。");
        }
    }

    /**
     * フルパスファイルに対して格納ディレクトリを取得する。
     * @param targetFilePath ファイルのパス
     * @return ディレクトリのパス
     */
    public static String getDirectoryFromFilePath(String targetFilePath) {

        int c1 = targetFilePath.lastIndexOf("\\");
        int c2 = targetFilePath.lastIndexOf("/");
        int pos = (c1 != -1) ? c1 : c2;
        if (pos >= 0) {
            return targetFilePath.substring(0, pos);
        } else {
            return "";
        }

    }

    /**
     * フルパスファイルに対してディレクトリ抜きのファイル名を取得する。
     * @param targetFilePath ファイルのパス
     * @return ディレクトリのパス
     */
    public static String getFileNameFromFilePath(String targetFilePath) {

        int c1 = targetFilePath.lastIndexOf("\\");
        int c2 = targetFilePath.lastIndexOf("/");
        int pos = (c1 != -1) ? c1 : c2;
        if (pos >= 0) {
            return targetFilePath.substring(pos + 1);
        } else {
            return targetFilePath;
        }

    }

    /**
     * 渡されるパラメータからQuoteを削除する。
     * @param txt パラメータ
     * @return 削除後の結果
     */
    public static String delQuote(String txt) {
        if (txt != null) {
            if (txt.charAt(0) == '"') {
                return txt.replaceAll("^\"(.*)\"$", "$1");

            } else if (txt.charAt(0) == '\'') {
                return txt.replaceAll("^'(.*)'$", "$1");

            }
        }
        return txt;
    }

    /**
     * strutsmoduleのパス名を調整する。
     * @param moduleName モジュール名
     * @return 変更後のモジュール名
     */
    public static String formatStrutsModulePath(String moduleName) {
        if (moduleName.startsWith("/")) {
            return moduleName;
        } else {
            return "/" + moduleName;
        }
    }

    /**
     * 完全修飾クラス名に対するパッケージ抜きのクラス名を取得する。
     * @param fullClassName 完全修飾クラス名
     * @return パッケージ抜きのクラス名
     */
    public static String getClassShortName(String fullClassName) {
        int lPos = fullClassName.lastIndexOf(".");
        if (lPos > 0) {
            return fullClassName.substring(lPos + 1);
        } else {
            return fullClassName;
        }
    }

    /**
     * 正規表現の置換先として使用するときに副作用が出ないように、\と$をエスケープする。
     * @param value 入力文字列
     * @return エスケープ結果
     */
    public static String escapeForRegexpReplacementValue(String value) {
        value = value.replaceAll("\\\\", "\\\\\\\\");
        value = value.replaceAll("\\$", "\\\\\\$");
        return value;
    }

}
