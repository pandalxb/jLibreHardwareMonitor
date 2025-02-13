package io.github.pandalxb.jlibrehardwaremonitor.util;

import java.util.Collection;

/**
 * CollectionUtils
 *
 * @author pandalxb
 */
public class CollectionUtils {
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }
}
