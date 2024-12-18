package org.example.hook;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.example.context.TestContext;

public class Hooks {
    // Cada thread terá seu próprio TestContext para isolar WebDriver por feature
    private static final ThreadLocal<TestContext> threadLocalContext = new ThreadLocal<>();
    private String browser = "chrome"; // Define o navegador padrão

    @Before
    public void beforeFeature(Scenario scenario) {
        if (threadLocalContext.get() == null) {
            // Cria um novo TestContext para a thread atual (feature)
            TestContext testContext = new TestContext();
            threadLocalContext.set(testContext);

            // Inicia o WebDriver e abre a URL inicial
            System.out.println("Iniciando WebDriver para feature: " + scenario.getUri());
        }
    }

    @After
    public void afterFeature(Scenario scenario) {
        // Finaliza o WebDriver ao término da execução da feature
        TestContext testContext = threadLocalContext.get();
        if (testContext != null) {
            System.out.println("Finalizando WebDriver para feature: " + scenario.getUri());
            testContext.quitDriver();
            threadLocalContext.remove(); // Remove o TestContext da thread atual
        }
    }

    // Método para acessar o TestContext da thread atual
    public static TestContext getTestContext() {
        return threadLocalContext.get();
    }
}
