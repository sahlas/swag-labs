package com.sahlas.swaglabs.catalog;

import com.microsoft.playwright.junit.UsePlaywright;
import com.sahlas.fixtures.BrowserOptionsFactory;
import com.sahlas.fixtures.TakesFinalScreenshot;
import com.sahlas.swaglabs.catalog.pageobjects.LoginPage;
import com.sahlas.swaglabs.catalog.pageobjects.ProductList;


@UsePlaywright(BrowserOptionsFactory.class)
public class AddToCartCheckoutTest implements TakesFinalScreenshot {
    LoginPage loginPage;
    ProductList productList;

//    @BeforeEach
//    void setUp(Page page) {
//        loginPage = new LoginPage(page);
//        productList = new ProductList(page);
////        productDetails = new ProductDetails(page);
////        checkoutPage = new CheckoutInformationPage(page);
//    }
//
//    @BeforeEach
//    void openHomePage() {
//        loginPage.openHomePage();
//        User user = new User("standard_user", "secret_sauce");
//        loginPage.loginUser(user);
//    }
//
//    @Disabled("Disabled until the product list page is implemented")
//    @Test
//    @DisplayName("Should show the page title")
//    void shouldShowThePageTitle(Page page) {
//        productList = new ProductList(page);
//        Assertions.assertThat(productList.getTitle()).contains("Products")
//                .as("Page title should contain 'Products'");
//    }
}
