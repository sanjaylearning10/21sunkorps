package com.amazon.basetest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class BaseTest {

	public WebDriver driver = null;
	public File file;
	public FileInputStream fis;
	public Properties pro;
	public String TestCaseName;
	ExtentTest extentTest;
	ExtentReports extentReport;

	// Here we will be launching the desired browser
	@BeforeSuite
	public void launchBrowser() throws IOException {
		file = new File(System.getProperty("user.dir") + "\\config.properties");
		fis = new FileInputStream(file);
		pro = new Properties();
		pro.load(fis);

		if (pro.getProperty("Browser").equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "\\Resources\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (pro.getProperty("Browser").equalsIgnoreCase("ff")
				|| pro.getProperty("Browser").equalsIgnoreCase("firefox")) {

			System.setProperty("webdriver.gecko.driver",
					System.getProperty("user.dir") + "\\Resources\\geckodriver.exe.exe");
			driver = new FirefoxDriver();
		}

		else if (pro.getProperty("Browser").equalsIgnoreCase("ie")
				|| pro.getProperty("Browser").equalsIgnoreCase("internet explorer")) {
			System.setProperty("webdriver.ie.driver",
					System.getProperty("user.dir") + "\\Resources\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(pro.getProperty("Url"));
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	// Here we are initialing the HtmlReporter
	@BeforeTest
	public void setup() {
		
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(
				System.getProperty("user.dir") + "/test-output/extent.html");

		extentReport = new ExtentReports();
		extentReport.attachReporter(htmlReporter);

		extentTest = extentReport.createTest("Amazon Test Suite", "Extent Report");

	}

	// Here we are marking the test status
	@AfterMethod
	public void tearDown(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.fail(result.getThrowable().getMessage());
			extentTest.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + "failed", ExtentColor.RED));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest.pass("Pass");
			extentTest.log(Status.PASS, MarkupHelper.createLabel(result.getName() + "Success", ExtentColor.GREEN));
		}

		extentReport.flush();
	}

	// Here we will be closing the browser after the test 
	@AfterSuite
	public void closeBrowser() {
		if (driver != null) {
			driver.quit();
		}

	}

}
