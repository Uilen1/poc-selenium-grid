package org.example.factory;

import org.example.configuration.Properties;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    static Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private static final ThreadLocal<WebDriver> threadLocalDriver = ThreadLocal.withInitial(() -> {
        log.info("Inicializando WebDriver para thread: {}", Thread.currentThread().getName());
        return createDriverInstance();
    });

    private static final String GRID_URL = "http://localhost:4444/wd/hub"; // URL do Selenium Grid

    private static final boolean isParallelExecution = Boolean.parseBoolean(System.getProperty("parallel.execution"));

    private DriverFactory() {
        // Impedir instâncias externas
    }

    private static WebDriver createDriverInstance() {
        switch (Properties.BROWNSER) {
            case FIREFOX:
                FirefoxOptions firefoxOptions = getFirefoxOptions();
                return isParallelExecution
                        ? createRemoteWebDriver(firefoxOptions)
                        : new FirefoxDriver(firefoxOptions);
            case CHROME:
                ChromeOptions chromeOptions = getChromeOptions();
                return isParallelExecution
                        ? createRemoteWebDriver(chromeOptions)
                        : new ChromeDriver(chromeOptions);
            default:
                throw new IllegalStateException("Browser não suportado: " + Properties.BROWNSER);
        }
    }

    private static WebDriver createRemoteWebDriver(Capabilities capabilities) {
        try {
            return new RemoteWebDriver(new URL(GRID_URL), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao conectar ao Selenium Grid", e);
        }
    }

    public static WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    public static void quitDriver() {
        WebDriver driver = threadLocalDriver.get();
        if (driver != null) {
            log.info("Finalizando WebDriver para thread: {}", Thread.currentThread().getName());
            driver.quit();
            threadLocalDriver.remove();
        }
    }
    private static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--window-size=1580,1280");
        return chromeOptions;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setAcceptInsecureCerts(true);
        firefoxOptions.setHeadless(true);
        firefoxOptions.setBinary("/opt/firefox/firefox-bin");
        firefoxOptions.addArguments("--no-sandbox");
        firefoxOptions.addArguments("start-maximized");
        firefoxOptions.addArguments("--disable-dev-shm-usage");
        firefoxOptions.addArguments("--window-size=1580,1280");
        return firefoxOptions;
    }
}
