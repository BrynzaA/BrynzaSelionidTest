package org.tests.pages;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    protected void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected WebElement waitForElementVisible(WebElement element, int seconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return customWait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForPageLoad() {
        wait.until(webDriver ->
                ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    public void takeScreenshot(String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            System.out.println("[DEBUG] Screenshot '" + name + "' taken, size: " + screenshot.length + " bytes");
        } catch (Exception e) {
            System.out.println("[WARN] Could not take screenshot: " + e.getMessage());
        }
    }
}