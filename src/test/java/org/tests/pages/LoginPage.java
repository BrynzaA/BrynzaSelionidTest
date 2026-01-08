package org.tests.pages;

import java.time.Duration;
import java.util.Set;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.tests.utils.ConfigReader;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public Set<Cookie> cookies;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "formly_1_input_username_0")
    private WebElement usernameFieldDesc;

    @FindBy(css = "body > div.jumbotron > div > div > div > form > div.form-actions > button")
    private WebElement loginButton;

    @FindBy(css = "body > div.jumbotron > div > div > div > div.alert.alert-danger.ng-binding.ng-scope")
    private WebElement failedLoginAlert;

    public void enterUsername(String username) {
        waitForElementVisible(usernameField, 20);
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        waitForElementVisible(passwordField, 20);
        passwordField.sendKeys(password);
    }

    public void enterUsernameDesc(String usernameDesc) {
        waitForElementVisible(usernameFieldDesc, 20);
        usernameFieldDesc.sendKeys(usernameDesc);
    }

    public void clickLoginButton() {
        waitForElementVisible(loginButton, 20);
        loginButton.click();
    }

    public LoginPage login(String username, String password, String usernameDesc) {
        enterUsername(username);
        enterPassword(password);
        enterUsernameDesc(usernameDesc);
        clickLoginButton();
        return this;
    }

    public boolean isLoginSuccess() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlToBe(ConfigReader.getLoginSuccessUrl()));

            cookies = driver.manage().getCookies();

            return  new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions
                    .invisibilityOf(usernameField));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isPageIsPageAfterLogin() {
        return driver.getCurrentUrl().equals(ConfigReader.getLoginSuccessUrl());
    }

    public boolean isButtonActive() {
        return loginButton.isEnabled();
    }

    public boolean isLoginFailed() {
        try {

            return  waitForElementVisible(failedLoginAlert, 1).isDisplayed();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public LoginPage open() {
        try {
            System.out.println("[DEBUG] Step 1: Opening URL: " + ConfigReader.getLoginUrl());
            driver.get(ConfigReader.getLoginUrl());

            waitForPageLoad();

            waitForAngular();

            System.out.println("[DEBUG] Current URL: " + driver.getCurrentUrl());
            System.out.println("[DEBUG] Page title: " + driver.getTitle());
            System.out.println("[DEBUG] Page source length: " + driver.getPageSource().length());

            takeScreenshot("after_page_load");

        } catch (Exception e) {
            System.out.println("[ERROR] Error opening page: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    protected void waitForAngular() {
        try {
            ((JavascriptExecutor) driver).executeAsyncScript(
                    "var callback = arguments[arguments.length - 1];" +
                            "if (window.angular) {" +
                            "  var rootEl = document.querySelector('[ng-app]') || document.querySelector('body');" +
                            "  angular.element(rootEl).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);" +
                            "} else {" +
                            "  callback();" +
                            "}"
            );
        } catch (Exception e) {
            System.out.println("[INFO] Angular not detected, continuing...");
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void openSuccessLoginPageWithCookie(Set<Cookie> fileCookies) {
        fileCookies.forEach(cookie -> driver.manage().addCookie(cookie));
        driver.get(ConfigReader.getLoginSuccessUrl());
        waitForPageLoad();
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public void removeFocusFromInput(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        if (element != null) {
            js.executeScript("arguments[0].blur();", element);
        } else {
            js.executeScript("if(document.activeElement) { document.activeElement.blur(); }");
        }
    }

    public void removeFocusFromUsernameField() {
        removeFocusFromInput(usernameField);
    }

    public boolean hasFocus(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript(
                "return document.activeElement === arguments[0];",
                element
        );
    }

    public boolean hasFocusOnUsernameField() {
        return hasFocus(usernameField);
    }

    public boolean hasVerticalScroll() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript(
                "return document.documentElement.scrollHeight > document.documentElement.clientHeight;"
        );
    }

    public boolean hasHorizontalScroll() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript(
                "return document.documentElement.scrollWidth > document.documentElement.clientWidth;"
        );
    }

    public void checkElementExists(String elementName, LoginPage loginPage) {
        try {
            WebElement element = driver.findElement(By.id(elementName));
            System.out.println("✓ Element '" + elementName + "' FOUND!");
            System.out.println("  - Displayed: " + element.isDisplayed());
            System.out.println("  - Enabled: " + element.isEnabled());
            System.out.println("  - Location: " + element.getLocation());
        } catch (Exception e) {
            System.out.println("✗ Element '" + elementName + "' NOT FOUND!");
        }
    }
}

