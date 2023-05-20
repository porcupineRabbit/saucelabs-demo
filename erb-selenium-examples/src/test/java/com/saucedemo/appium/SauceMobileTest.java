package com.saucedemo.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class SauceMobileTest {

    @AndroidFindBy(id = "welcomeEnglishTextView")
    private WebElement welcomeEnglishTextView;

    // The class resource is usually  not needed: @AndroidFindBy(id = "gr.mobile.eurobank.development:id/continueGreekTextView")
    @AndroidFindBy(id = "continueGreekTextView")
    private WebElement continueGreekBtn;

    // private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private AndroidDriver driver;
    WebDriverWait elementWait;

    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        /*
        System.getProperties().put("http.proxyHost", "192.168.51.16");
        System.getProperties().put("http.proxyPort", "8080");
        System.getProperties().put("https.proxyHost", "192.168.51.16");
        System.getProperties().put("https.proxyPort", "8080");
        */

        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com/wd/hub");

        MutableCapabilities caps = new MutableCapabilities();
        MutableCapabilities sauceOptions = new MutableCapabilities();

        // caps.setCapability("appium:app", "storage:filename=erb-app-3-9-8.apk");
        caps.setCapability("appium:app", "storage:8badd97e-3d9d-4088-920e-3c3c94f93780");
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appium:deviceName", "Google.Pixel.*");
        caps.setCapability("appium:platformVersion", "12");
        caps.setCapability("appium:appPackage", "gr.mobile.eurobank.development");
        caps.setCapability("appium:appActivity", "gr.mobile.eurobank.ui.activity.launch.LaunchActivity");
        caps.setCapability("appium:appWaitDuration", "60000");

        // caps.setCapability("cacheId", "android_123");

        sauceOptions.setCapability("username", "cpanagio");
        sauceOptions.setCapability("accessKey", "94a7b28c-5ab4-45aa-8b23-39dc1fc296af");
        // sauceOptions.setCapability("username", "porcupineRabbit");
        // sauceOptions.setCapability("accessKey", "a7c6ca9e-b947-48ea-88ae-fcbf052e1cc5");
        sauceOptions.setCapability("name", method.getName());
        sauceOptions.setCapability("tunnelName", "tcauto_tunnel");


        caps.setCapability("sauce:options", sauceOptions);

        try {
            // driver.set(new AppiumDriver(url, caps));
            driver = new AndroidDriver(url, caps);
            elementWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        } catch (Exception e) {
            System.out.println("*** BOOM ***");
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

    @Test
    public void correctTitle() {
        /* This works!
            WebElement btnElement = elementWait.until(ExpectedConditions.presenceOfElementLocated (By.id("continueGreekTextView")));
         */
        WebElement btnElement = elementWait.until(ExpectedConditions.visibilityOf(continueGreekBtn));
        Assert.assertTrue(btnElement.isDisplayed());
        // Assert.assertEquals(welcomeEnglishTextView.getText(), "Welcome");
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        System.out.println("AfterMethod hook");

        if (driver != null) {
            try {
                boolean bSuccess = result.isSuccess();
                ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + (bSuccess ? "passed" : "failed"));
                if (!bSuccess)
                    ((JavascriptExecutor) driver).executeScript("sauce:context=" + result.getThrowable().getMessage());

                // Print the report execution URL:
                System.out.println("Execution URL: " + driver.getCapabilities().getCapability("appium:testobject_test_report_url"));
            } finally {
                System.out.println("Release driver");
                driver.quit();
            }
        } else {
            System.out.println("AfterMethod - Driver is null");
        }
    }

}
