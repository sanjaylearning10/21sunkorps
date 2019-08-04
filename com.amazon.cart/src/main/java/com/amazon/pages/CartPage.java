package com.amazon.pages;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class CartPage {

	public WebDriver driver;

	public CartPage(WebDriver driver) {
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
		this.driver = driver;
	}

	@FindBy(css = "input#twotabsearchtextbox")
	private WebElement searchBox;

	@FindBy(css = "[name='submit.add-to-cart']")
	private WebElement addToCart;

	@FindBy(css = "span#nav-cart-count")
	private WebElement cartItemCount;

	public int addProductToCart() throws InterruptedException {

		Actions action = new Actions(driver);
		
		//Below step will be hitting enter button
		action.sendKeys(Keys.RETURN).build().perform();

		//We will collect all the items which are marked as bestseller
		List<WebElement> list = driver.findElements(By.xpath(
				"//*[contains(text(),'Best Seller')]/parent::span/parent::span/parent::span/parent::div/parent::span/parent::a/parent::div/parent::div/following-sibling::div/div/following-sibling::div/div/div/div/div/div/h2/a"));

		
		for (int i = 0; i < list.size(); i++) {
		
			//We will be open each bestseller marked item in a new window
			action.keyDown(Keys.SHIFT).click(list.get(i)).keyUp(Keys.SHIFT).build().perform();
		}

		//This will be having the window handle of the parent window
		String parentWinHandle = driver.getWindowHandle();

		//This will be having the window handle of all windows
		Set<String> winHandles = driver.getWindowHandles();

		for (String handle : winHandles) {
			if (!handle.equals(parentWinHandle)) {
				driver.switchTo().window(handle);
				Thread.sleep(3000);
				addToCart.click();
				Thread.sleep(3000);
				driver.close();

			}
		}
		// Switching the control back to parent window
		driver.switchTo().window(parentWinHandle);
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		
		// Here we are scrolling to the top of page
		js.executeScript("window.scrollTo(0, 0)");
		
		// Here we are refreshing the page
		driver.navigate().refresh();

		// This will return the number of elements which are marked as bestseller 
		return list.size();

	}

	// With the help of this method, we will search the desired product 
	public void searchProduct(String item) {
		searchBox.sendKeys(item);
	}

	// With the help of this method, we will get the number of items added in cart.
	public int numberOfItemInCart() {
		return Integer.parseInt(cartItemCount.getText());
	}
}
