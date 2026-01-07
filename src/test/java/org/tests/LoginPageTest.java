package org.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tests.pages.LoginPage;
import org.tests.utils.ConfigReader;

public class LoginPageTest extends BaseTest {

    @Test
    public void testLoginPage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open().login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword(), ConfigReader.getValidUsernameDesc());

        boolean isLoginSuccess = loginPage.isLoginSuccess();
        Assert.assertTrue(isLoginSuccess, "Логин должен быть успешен");
    }
}

