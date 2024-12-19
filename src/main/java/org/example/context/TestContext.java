package org.example.context;

import org.example.factory.DriverFactory;
import org.openqa.selenium.WebDriver;


public class TestContext {
    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    public void quitDriver() {
        DriverFactory.quitDriver();
    }
}
