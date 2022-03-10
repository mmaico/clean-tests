package mmaico.lazytests.infratest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import mmaico.lazytests.infratest.helpers.StringTemplateHelper;
import mmaico.lazytests.infratest.helpers.template.MathHelper;
import mmaico.lazytests.infratest.helpers.template.UUIDHelper;
import mmaico.lazytests.infratest.scenarios.ScenarioLoaderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {

    protected static ScenarioLoaderHelper scenarios = new ScenarioLoaderHelper();
    private static WireMockServer server;

    @Autowired
    protected MockMvc mockMvc;

    static {
        final int PORT = 8181;
        server = new WireMockServer(WireMockConfiguration.wireMockConfig().port(PORT));
        server.start();
        WireMock.configureFor("localhost", PORT);
    }

    protected String s(String scenarioName) {
        return this.scenarios.getScenario(scenarioName).getJson();
    }

    protected void stub(String scenario, Object... params) {
        Map<String, Object> helpers = toMap(params);
        helpers.put("uuid", UUIDHelper.getInstance());
        helpers.put("math", MathHelper.getInstance());


        String jsonResult = this.scenarios.getScenario(scenario).getJson();
        String jsonRender = StringTemplateHelper.render(jsonResult, helpers);
        server.addStubMapping(StubMapping.buildFrom(jsonRender));
    }

    private Map<String, Object> toMap(Object[] params) {
        List<Object> paramsList = Arrays.asList(params);
        Map<String, Object> helpers = new HashMap<>();

        for (int i = 0; i < paramsList.size() ; i = i + 2) {
            if ((i + 1) > (paramsList.size() - 1)) {
                helpers.put(paramsList.get(i).toString(), null);
                continue;
            }
            helpers.put(paramsList.get(i).toString(), paramsList.get(i + 1));
        }
        return helpers;
    }

}
