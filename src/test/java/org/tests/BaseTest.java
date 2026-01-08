package org.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);


    protected WebDriver driver;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        String host = System.getProperty("selenium.host", "localhost");
        String port = System.getProperty("selenium.port", "4444");
        String url = String.format("http://%s:%s/wd/hub", host, port);

        logger.info("Connecting to Selenium hub at: {}", url);

        try {
            driver = new RemoteWebDriver(new URL(url), options);
            System.out.println("WebDriver created successfully!");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().window().maximize();

        } catch (Exception e) {
            logger.error("Failed to create WebDriver: {}", e.getMessage());
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver closed successfully!");
            } catch (Exception e) {
                logger.error("Error quitting driver: {}", e.getMessage());
            }
        }
    }
}