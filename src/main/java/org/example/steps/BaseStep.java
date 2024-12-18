package org.example.steps;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import org.example.context.TestContext;
import org.example.hook.Hooks;
import org.openqa.selenium.WebDriver;

public class BaseStep {

    private final WebDriver driver;

    public BaseStep() {
        // Obtém o cenário atual na thread
        Scenario scenario = Hooks.threadLocalScenario.get();

        // Obtém o nome da feature atual
        String featureName = Hooks.extractFeatureName(scenario);

        // Acessa o TestContext da feature atual
        TestContext testContext = Hooks.getTestContext(featureName);

        // Obtém o driver da feature
        this.driver = testContext.getDriver("chrome");
    }

    @Given("eu acesso a pagina inicial")
    public void eu_acesso_a_pagina_inicial() {
        driver.get("https://www.google.com");
        System.out.println("Página atual: " + driver.getTitle());
    }
}
