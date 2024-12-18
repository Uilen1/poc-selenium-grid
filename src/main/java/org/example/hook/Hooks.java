package org.example.hook;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.example.context.TestContext;

import java.util.concurrent.ConcurrentHashMap;

public class Hooks {

    // Mapa global para associar a URI da feature ao TestContext
    private static final ConcurrentHashMap<String, TestContext> featureContexts = new ConcurrentHashMap<>();

    // ThreadLocal para associar o cenário atual à thread
    public static final ThreadLocal<Scenario> threadLocalScenario = new ThreadLocal<>();

    // Define o navegador padrão
    private final String browser = "chrome";

    // Define se a execução é paralela (default: false)
    private static final boolean isParallelExecution = Boolean.parseBoolean(System.getProperty("parallel.execution"));

    // Contexto único para execução não paralela
    private static TestContext singleTestContext;

    @Before
    public void beforeScenario(Scenario scenario) {
        // Armazena o cenário atual na ThreadLocal
        threadLocalScenario.set(scenario);

        // Obtém o nome da feature associada ao cenário
        String featureName = extractFeatureName(scenario);
        if (isParallelExecution) {
            // Paralelismo: cria um contexto por feature
            featureContexts.computeIfAbsent(featureName, key -> {
                System.out.println("Iniciando WebDriver para feature: " + key);

                // Cria e retorna um novo TestContext para a feature
                TestContext testContext = new TestContext();
                testContext.getDriver(browser);
                return testContext;
            });
        } else {
            // Execução sequencial: cria apenas um contexto compartilhado
            if (singleTestContext == null) {
                System.out.println("Iniciando WebDriver para execução sequencial.");
                singleTestContext = new TestContext();
                singleTestContext.getDriver(browser);
            }
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Nenhuma ação no nível de cenário, pois o driver será encerrado após a execução completa.
        System.out.println("Cenário concluído: " + scenario.getName());
    }

    @AfterAll
    public static void afterAll() {
        if (isParallelExecution) {
            // Finaliza todos os WebDrivers criados para execução paralela
            System.out.println("Finalizando todos os WebDrivers em execução paralela.");
            featureContexts.values().forEach(TestContext::quitDriver);
            featureContexts.clear();
        } else {
            // Finaliza o WebDriver único criado para execução sequencial
            if (singleTestContext != null) {
                System.out.println("Finalizando WebDriver para execução sequencial.");
                singleTestContext.quitDriver();
            }
        }
    }

    // Método para acessar o TestContext da feature atual
    public static TestContext getTestContext(String featureName) {
        if (isParallelExecution) {
            return featureContexts.get(featureName);
        } else {
            return singleTestContext;
        }
    }

    // Extrai o nome da feature de um cenário
    public static String extractFeatureName(Scenario scenario) {
        // Extrai o nome da feature a partir da URI
        String uri = scenario.getUri().toString();
        return uri.substring(uri.lastIndexOf('/') + 1).replace(".feature", "");
    }
}
