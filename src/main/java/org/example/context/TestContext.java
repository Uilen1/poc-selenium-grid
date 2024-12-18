package org.example.context;

import org.openqa.selenium.WebDriver;

import static org.example.factory.DriverFactory.createDriver;

public class TestContext {
    private WebDriver driver;

    public WebDriver getDriver(String browser) {
        if (driver == null) {
            driver = createDriver(browser);
        }
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
