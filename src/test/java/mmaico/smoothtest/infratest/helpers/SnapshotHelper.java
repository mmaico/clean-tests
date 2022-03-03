package mmaico.smoothtest.infratest.helpers;

import org.apache.commons.lang.StringUtils;

import static org.apache.commons.lang.StringEscapeUtils.unescapeJava;
import static org.apache.commons.lang.StringUtils.isBlank;

public class SnapshotHelper {


    public static String getJson(String prefix, String scenario) {
        if (isBlank(scenario)) return "";

        return StringUtils.chop(unescapeJava(scenario.replace(prefix + "[", "")).trim())
                .replaceAll("\"\\{", "{")
                .replaceAll("}\"", "}");
    }
}
