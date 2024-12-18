package org.example.steps;

import io.cucumber.java.en.Given;
import org.example.context.TestContext;
import org.example.hook.Hooks;
import org.openqa.selenium.WebDriver;

public class BaseStep {
    private final WebDriver driver;

    public BaseStep() {
        TestContext testContext = Hooks.getTestContext();
        this.driver = testContext.getDriver("chrome"); // Pega o driver compartilhado
    }

    @Given("eu acesso a pagina inicial")
    public void eu_acesso_a_pagina_inicial() {
        driver.get("https://www.google.com");
        System.out.println("Página atual: " + driver.getTitle());
    }
}
