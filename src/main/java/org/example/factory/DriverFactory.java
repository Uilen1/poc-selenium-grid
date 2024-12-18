package org.example.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    private static final String GRID_URL = "http://localhost:4444/wd/hub"; // URL do Selenium Grid

    private DriverFactory() {
        // Evita instanciação
    }

    public static WebDriver createDriver(String browser) {
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless"); // Opcional: modo headless
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    return new RemoteWebDriver(new URL(GRID_URL), chromeOptions);

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    return new RemoteWebDriver(new URL(GRID_URL), firefoxOptions);

                default:
                    throw new IllegalArgumentException("Navegador não suportado: " + browser);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL do Selenium Grid é inválida!", e);
        }
    }
}
