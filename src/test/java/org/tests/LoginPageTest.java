package org.tests;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.tests.pages.LoginPage;
import org.tests.utils.ConfigReader;


public class LoginPageTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginPageTest.class);


    @Test
    public void testLoginPage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open().login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword(), ConfigReader.getValidUsernameDesc());

        boolean isLoginSuccess = loginPage.isLoginSuccess();
        Assert.assertTrue(isLoginSuccess, "Логин должен быть успешен");
    }

    @Test
    public void testPageElements() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        loginPage.checkElementExists("username", loginPage);
        loginPage.checkElementExists("password", loginPage);
        loginPage.checkElementExists("formly_1_input_username_0", loginPage);

        List<WebElement> allElements = driver.findElements(By.cssSelector("*"));
        System.out.println("Total elements found: " + allElements.size());
        logger.info("Total elements found: {}", allElements.size());

        for (int i = 0; i < Math.min(50, allElements.size()); i++) {
            WebElement element = allElements.get(i);
            String tag = element.getTagName();
            String id = element.getAttribute("id");
            String className = element.getAttribute("class");
            if (id != null && !id.isEmpty()) {
                logger.debug("Element #{}: tag={}, id={}, class={}", i, tag, id, className);
            }
        }
    }
}

