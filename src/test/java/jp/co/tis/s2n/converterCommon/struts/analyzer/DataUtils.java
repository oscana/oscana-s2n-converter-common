package jp.co.tis.s2n.converterCommon.struts.analyzer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import jp.co.tis.s2n.converterCommon.util.StringUtils;

/**
 * {@link StrutsAnalyzer}のテストに関わるユーティリティ。
 *
 */
public class DataUtils {
    public static <E> E findByKeyFromList(String key, String value, List<E> searchList) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        E curData = null;
        for (Iterator<E> iterator = searchList.iterator(); iterator.hasNext();) {
            curData = iterator.next();
            Method getterMethod = curData.getClass().getMethod(StringUtils.mkGetterName(key));
            String retVlue = (String) getterMethod.invoke(curData);
            if (value.equals(retVlue)) {
                return curData;
            }
        }

        return null;

    }
}
