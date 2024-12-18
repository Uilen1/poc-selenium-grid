package org.example.hook;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.AfterAll;
import org.example.context.TestContext;

import java.util.concurrent.ConcurrentHashMap;

public class Hooks {

    // Mapa global para associar a URI da feature ao TestContext
    private static final ConcurrentHashMap<String, TestContext> featureContexts = new ConcurrentHashMap<>();

    // ThreadLocal para associar o cenário atual à thread
    public static final ThreadLocal<Scenario> threadLocalScenario = new ThreadLocal<>();

    // Define o navegador padrão
    private final String browser = "chrome";

    @Before
    public void beforeScenario(Scenario scenario) {
        // Armazena o cenário atual na ThreadLocal
        threadLocalScenario.set(scenario);

        // Obtém o nome da feature associada ao cenário
        String featureName = extractFeatureName(scenario);

        // Verifica se já existe um contexto para essa feature
        featureContexts.computeIfAbsent(featureName, key -> {
            System.out.println("Iniciando WebDriver para feature: " + featureName);

            // Cria e retorna um novo TestContext para a feature
            TestContext testContext = new TestContext();
            testContext.getDriver(browser).get("https://www.example.com");
            return testContext;
        });
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Nenhuma ação no nível de cenário, pois o driver será encerrado após a feature.
        System.out.println("Cenário concluído: " + scenario.getName());
    }

    @AfterAll
    public static void afterAll() {
        // Finaliza todos os WebDrivers restantes no mapa
        System.out.println("Finalizando todos os WebDrivers");
        featureContexts.values().forEach(TestContext::quitDriver);
        featureContexts.clear();
    }

    // Método para acessar o TestContext da feature atual
    public static TestContext getTestContext(String featureName) {
        return featureContexts.get(featureName);
    }

    // Extrai o nome da feature de um cenário
    public static String extractFeatureName(Scenario scenario) {
        // Extrai o nome da feature a partir da URI
        String uri = scenario.getUri().toString();
        return uri.substring(uri.lastIndexOf('/') + 1).replace(".feature", "");
    }
}
