package com.saucedemo.selenium.login;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumLoginTest {

    protected RemoteWebDriver driver;

    @BeforeMethod
    public void setUp(Method method) throws MalformedURLException {
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("name", method.getName());
        sauceOptions.setCapability("browserVersion", "latest");

        ChromeOptions options = new ChromeOptions();
        options.setCapability("sauce:options", sauceOptions);
        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com/wd/hub");

        driver = new RemoteWebDriver(url, options);
    }

    @Test
    public void testName() {
        driver.navigate().to("https://www.saucedemo.com");
        Assert.assertEquals("Swag Labs", driver.getTitle());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String status = result.isSuccess() ? "passed" : "failed";
        driver.executeScript("sauce:job-result=" + status);
    }
}
