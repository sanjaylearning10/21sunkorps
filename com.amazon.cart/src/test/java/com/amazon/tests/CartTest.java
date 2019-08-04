package com.amazon.tests;

import org.testng.annotations.Test;

import com.amazon.basetest.BaseTest;
import com.amazon.pages.CartPage;

import junit.framework.Assert;

public class CartTest extends BaseTest {

	CartPage cart_Page;

	@Test
	public void addProductToCart() throws InterruptedException {

		cart_Page = new CartPage(driver);
		
		// Here we will search the Headphones
		cart_Page.searchProduct("Headphones");
		
		// Here we will add the items which are marked as bestseller 
		int itemAdded= cart_Page.addProductToCart();
		
		// Here we are validating that the number of items which are marked as bestseller should be equal to the number of items added in cart 
		Assert.assertEquals(cart_Page.numberOfItemInCart(), itemAdded);

	}

}
