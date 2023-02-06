package mmaico.clean_tests.infratest.helpers;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public class StringTemplateHelper {


    public static String render(String templateStr, Map<String, Object> params) {
        try {
            RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
            runtimeServices.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
            StringReader reader = new StringReader(templateStr);
            SimpleNode node = runtimeServices.parse(reader, "template");

            StringWriter result = new StringWriter();
            VelocityContext context = new VelocityContext(params);

            Template template = new Template();
            template.setRuntimeServices(runtimeServices);
            template.setData(node);
            template.initDocument();

            template.merge(context, result);

            return result.toString();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }

    }
}
