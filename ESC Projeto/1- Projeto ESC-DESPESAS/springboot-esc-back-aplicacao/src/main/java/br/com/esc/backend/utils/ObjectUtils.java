package br.com.esc.backend.utils;

import org.springframework.lang.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ObjectUtils {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static String toString(Object obj) {
        if (isNull(obj)) {
            return null;
        }
        return obj.toString();
    }

    public static String pularLinha(int i) {
        if (i <= 0) {
            i = 1;
        }

        String texto = "";
        for (int j = 0; j < i; j++) {
            texto += "\n";
        }

        return texto;
    }

    public static String pularLinha() {
        return "\n";
    }

    public static boolean isEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        } else if (obj == "") {
            return true;
        } else if (obj.equals("null")) {
            return true;
        } else if (obj instanceof Optional) {
            return !((Optional)obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map)obj).isEmpty() : false;
        }
    }
}
