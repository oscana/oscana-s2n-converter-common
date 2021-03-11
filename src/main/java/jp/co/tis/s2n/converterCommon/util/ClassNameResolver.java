package jp.co.tis.s2n.converterCommon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ソースコード中に登場するクラス名に対する完全修飾クラス名を求めるためのツール。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ClassNameResolver {
    private String currentPackage;

    private Map<String, List<String>> classNames = null;

    /**
     * java.lang.* のようなワイルドカード付きがインポートされていることを示す
     */
    boolean includeWildcardImport = false;

    public ClassNameResolver(String currentPackage, List<String> importList) {
        this.classNames = new HashMap<String, List<String>>();
        this.currentPackage = currentPackage;
        for (Iterator<String> itr = importList.iterator(); itr.hasNext();) {
            String fullPackageName = itr.next();

            int o = fullPackageName.lastIndexOf(".");
            String packageName = fullPackageName.substring(0, o);
            String className = fullPackageName.substring(o + 1, fullPackageName.length());
            if (className.equals("*")) {
                //ワイルドカードインポート文は取り込まずフラグだけ立てておく
                this.includeWildcardImport = true;
            } else {
                if (this.classNames.containsKey(className)) {
                    List<String> packages = this.classNames.get(className);
                    packages.add(packageName);
                } else {
                    List<String> packages = new ArrayList<>();
                    packages.add(packageName);
                    this.classNames.put(className, packages);
                }
            }
        }

    }

    /**
     * クラス名を完全修飾クラス名に変換する。<br>
     * 完全修飾クラス名が与えられた場合はそのまま返す。<br>
     * そうでない場合は、与えられたインポートリストとカレントパッケージから完全修飾クラス名を推定する
     * @param className クラス名
     * @return フルクラス名
     */
    public String resolveFullName(String className) {

        if (className.indexOf(".") != -1) {
            return className;
        }

        List<String> packages = this.classNames.get(className);
        if (packages != null) {
            if (packages.size() == 1) {
                //１つだけあるならＯＫ
                String packageName = packages.get(0);
                return StringUtils.addClassPackage(packageName, className);

            } else {
                //２つ以上なら特定不能ＮＧ
                throw new RuntimeException(className + " - 2つ以上のインポートがあるため、パッケージ名が推定できません。");
            }
        } else {
            if (this.includeWildcardImport) {
                throw new RuntimeException("ワイルドカードインポートがあるため、パッケージ名が推定できません。");
            } else {
                return StringUtils.addClassPackage(currentPackage, className);
            }

        }
    }

}
