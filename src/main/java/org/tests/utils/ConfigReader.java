package org.tests.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_PATH = "config.properties";

    static {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить config.properties: " + e.getMessage());
        }
    }

    public static String getValidUsername() {
        return properties.getProperty("valid.username");
    }

    public static String getValidPassword() {
        return properties.getProperty("valid.password");
    }

    public static String getValidUsernameDesc() {
        return properties.getProperty("valid.usernamedesc");
    }

    public static String getLoginUrl() {
        return properties.getProperty("login.url");
    }

    public static String getLoginSuccessUrl() {
        return properties.getProperty("login.url.success");
    }
}