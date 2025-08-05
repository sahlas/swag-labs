package com.sahlas.cucumber.stepdefinitions;

import com.sahlas.domain.User;
import com.sahlas.swaglabs.catalog.ProductSummary;
import com.sahlas.swaglabs.catalog.pageobjects.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PlaywrightCucumberStepDefinitions {
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    static final String SHOPPING_CART_PAGE_TITLE = dotenv.get("SHOPPING_CART_PAGE_TITLE", "Your Cart");
    static final String PRODUCT_LIST_PAGE_TITLE = dotenv.get("PRODUCT_LIST_PAGE_TITLE", "Products");
    LoginPage loginPage;
    ProductList productList;
    ShoppingCartPage shoppingCartPage;
    CheckoutInformationPage checkoutInformationPage;
    CheckoutOverviewPage checkoutOverviewPage;
    CheckoutCompletePage checkoutCompletePage;


    @DataTableType
    public static ProductSummary transform(Map<String, String> entry) {
        return new ProductSummary(
                entry.get("Product Name"),
                entry.get("Price")
        );
    }


    @Before
    public void setUp() {
        // Initialize the page objects
        loginPage = new LoginPage(PlaywrightCucumberFixtures.getPage());
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
        checkoutInformationPage = new CheckoutInformationPage(PlaywrightCucumberFixtures.getPage());
        checkoutOverviewPage = new CheckoutOverviewPage(PlaywrightCucumberFixtures.getPage());
        shoppingCartPage = new ShoppingCartPage(PlaywrightCucumberFixtures.getPage());
        checkoutCompletePage = new CheckoutCompletePage(PlaywrightCucumberFixtures.getPage());

    }

    @Given("Sally is on the login page")
    public void sallyIsOnTheLoginPage() {
        // Navigate to the login page
        loginPage.openHomePage();
        assertThat(loginPage.gtetUrl())
                .as("Login page URL should be correct")
                .isTrue();
    }

    @When("Sally enters her {string} and {string}")
    public void she_enters_her_name_and_password(String username, String password) {
        // Write code here that turns the phrase above into concrete actions
        User user = new User(username, password);
        loginPage.loginUser(user);

    }

    @Then("Sally should be logged in successfully")
    public void she_should_be_logged_in_successfully() {
        assertThat(loginPage.title())
                .as("Page title should be 'Products' after login")
                .isEqualTo("Products");
    }


    @Then("the error message should be {string}")
    public void theErrorMessageShouldBe(String errorMessage) {
        assertThat(loginPage.errorMessage())
                .as("Error message should match expected value")
                .containsIgnoringCase(errorMessage);
    }

    // When she sorts by "Price (low to high)" for example
    @When("Sally sorts by {string}")
    public void she_sorts_by(String sortCriteria) {
        // Write code here that turns the phrase above into concrete actions
        productList.sortBy(sortCriteria);
        assertThat(productList.getSortOption(sortCriteria))
                .as("Sort option should match expected value")
                .isTrue();
    }

    // Then the first product displayed should be "Sauce Labs Backpack"
    @Then("the first product displayed should be {string}")
    public void theFirstProductDisplayedShouldBe(String productName) {
        // Write code here that turns the phrase above into concrete actions
        assertThat(productList.getFirstProductName(productName))
                .as("First product name should match expected value")
                .isTrue();
    }

    @Given("Sally opens a browser link to the inventory page")
    public void sallyOpensABrowserLinkToTheInventoryPage() {
        // Navigate to the products page directly
        productList.openProductListPage();
    }

    @When("Sally is redirected to the login page")
    public void redirectToTheLoginPage() {
        // Wait for the page to load
        PlaywrightCucumberFixtures.getPage().waitForLoadState();
        assertThat(PlaywrightCucumberFixtures.getPage().url())
                .as("Login page is loaded with a an error message")
                .isEqualTo("https://www.saucedemo.com/");
    }

    @Then("Sally should be notified of login error {string}")
    public void sheShouldBeNotifiedOfLoginError(String arg0) {
        // Verify that the error message is displayed
        assertThat(loginPage.errorMessage())
                .as("Error message should match expected value")
                .isEqualTo(arg0);
    }

    @Then("the {string} status indicates it has been added to the cart")
    public void theProductShouldBeAddedToHerCart(String productName) {
        // Verify that the state of the product button has changed to "Remove"
        assertThat(productList.getProductButtonState(productName))
                .as("Product button state should be 'Remove' after adding to cart")
                .isTrue();
        // Verify that the product is added to the cart
        int cartCount = productList.getCartCount();
        assertThat(cartCount)
                .as("Cart count should be greater than 0")
                .isGreaterThan(0);
        System.out.println("Cart count: " + cartCount);
    }


    @When("Sally adds a {string} to her cart")
    public void sheAddsAToHerCart(String productName) {
        // Write code here that turns the phrase above into concrete actions
        productList.addProductToCart(productName);
        assertThat(productList.checkPageUrl())
                .as("Should be on the inventory page")
                .isTrue();
        assertThat(productList.getCartCount())
                .as("Cart count should be greater than 0 after adding a product")
                .isGreaterThan(0);
    }

    @When("Sally adds the following products to the cart")
    public void theUserAddsTheFollowingProductsToTheCart(DataTable productTable) {
        assertThat(productList.checkPageUrl())
                .as("Should be on the inventory page")
                .isTrue();
        assertThat(productList.getTitle())
                .as("Product list title equal contain 'Products'")
                .isEqualTo("Products");
        List<Map<String, String>> products = productTable.asMaps(String.class, String.class);

        for (Map<String, String> product : products) {
            String productName = product.get("product");
            productList.addProductToCart(productName);
        }

    }

    @Then("the shopping cart page should indicate all products picked for checkout")
    public void theShoppingCartShouldContainAllAddedProducts(DataTable productTable) {
        boolean dataMatch = shoppingCartPage.verifyCartContents(productTable);
        assertThat(dataMatch)
                .as("All product details should be represented in the cart")
                .isTrue();
    }

    @And("Sally removes the {string} from her cart on inventory page")
    public void removeItemFromTheInventoryPage(String productName) {
        // First, get the current cart count
        int initialCartCount = productList.getCartCount();
        System.out.println("Initial cart count: " + initialCartCount);
        // Next, add the product to the cart
        productList.removeProductFromCart(productName);
        // Verify that the product is removed from the cart by checking the cart count against the initial count
        int finalCartCount = productList.getCartCount();
        System.out.println("Final cart count: " + finalCartCount);
        assertThat(finalCartCount)
                .as("Cart count should be less than initial count after removing a product")
                .isLessThan(initialCartCount);
    }

    @Then("check that only the expected products are in her cart")
    public void checkThatOnlyTheRemainingProductsAreInHerCart() {
        // Verify that the specified product is in the cart
        int cartCount = productList.getCartCount();
        assertThat(cartCount)
                .as("Cart count should be 1 less after removing a product")
                .isEqualTo(1);
        System.out.println("Cart count after removal: " + cartCount);

    }

    @When("Sally views her cart")
    public void sheViewsHerCart() {
        // Navigate to the cart page
        shoppingCartPage.openShoppingCartPage();
        assertThat(shoppingCartPage.checkPageUrl())
                .as("Cart page URL should be correct")
                .isTrue();
        assertThat(shoppingCartPage.checkPageTitle())
                .as("Cart title should be 'Your Cart'")
                .isTrue();
    }

    @Then("Sally should see the following products in her cart")
    public void sheShouldSeeTheFollowingProductsInHerCart() {
    }

    @And("Sally removes all products from her cart")
    public void sallyRemovesAllProductsFromHerCart(DataTable productTable) {
        int initialCartCount = productList.getCartCount();
        System.out.println("Initial cart count: " + initialCartCount);

        List<Map<String, String>> products = productTable.asMaps(String.class, String.class);

        for (Map<String, String> product : products) {
            String productName = product.get("product");
            productList.removeProductFromCart(productName);
        }
    }

    @Given("Sally logs in")
    public void sallyHasLoggedInWithHerAccount() {
        String user_type = dotenv.get("USER_TYPE", "standard_user");
        // Load environment variables
        String username = "";
        String password = "";
        switch (user_type) {
            case "standard_user":
                System.out.println("Logging in as standard user");
                username = dotenv.get("STANDARD_USERNAME", "standard_user");
                password = dotenv.get("STANDARD_PASSWORD", "secret_sauce");
                break;
            case "locked_out_user":
                System.out.println("Logging in as locked out user");
                username = dotenv.get("LOCKED_USERNAME", "locked_out_user");
                password = dotenv.get("LOCKED_PASSWORD", "secret_sauce");
                break;
            case "problem_user":
                System.out.println("Logging in as problem user");
                username = dotenv.get("PROBLEM_USERNAME", "problem_user");
                password = dotenv.get("PROBLEM_PASSWORD", "secret_sauce");
                break;
            case "performance_glitch_user":
                System.out.println("Logging in as performance glitch user");
                username = dotenv.get("PERFORMANCE_USERNAME", "performance_glitch_user");
                password = dotenv.get("PERFORMANCE_PASSWORD", "secret_sauce");
                break;
            case "error_user":
                System.out.println("Logging in as error user");
                username = dotenv.get("ERROR_USERNAME", "error_user");
                password = dotenv.get("ERROR_PASSWORD", "secret_sauce");
                break;
            case "visual_user":
                System.out.println("Logging in as visual user");
                username = dotenv.get("VISUAL_USERNAME", "visual_user");
                password = dotenv.get("VISUAL_PASSWORD", "secret_sauce");
                break;
            default:
                throw new IllegalArgumentException("Unknown user type: " + user_type);
        }

        // Navigate to the login page
        loginPage.openHomePage();
        User currentUser = new User(username, password);
        loginPage.loginUser(currentUser);
    }

    @When("Sally begins the checkout process")
    public void sheProceedsToCheckout() {
        // Click on the checkout button
        productList.clickCheckoutButton();
        // Verify that the checkout page is displayed
        assertThat(checkoutInformationPage.checkPageUrl())
                .as("Checkout overview page URL should be correct")
                .isTrue();

        assertThat(checkoutInformationPage.checkTitle())
                .as("Checkout page title should be 'Checkout: Your Information'")
                .isTrue();
        System.out.println("Checkout page title: " + checkoutInformationPage.getTitle());
    }


    @Then("Sally fills in her personal information")
    public void sheFillsInHerPersonalInformation(DataTable personalInfoTable) {
        checkoutInformationPage.fillInPersonalInformation(personalInfoTable);

        // Verify that the personal information is filled in correctly
        assertThat(checkoutInformationPage.checkTitle())
                .as("Checkout information page title should be 'Checkout: Your Information'")
                .isTrue();
        System.out.println("Checkout information page title: " + checkoutInformationPage.getTitle());
    }

    @And("Sally continues to the overview page for review")
    public void sheShouldBeOnTheOverviewPage(DataTable productsInCart) {
        checkoutInformationPage.buttonClick("continue");
        double actualSubTotal = checkoutOverviewPage.getSubTotalPrice();
        double expectedSubTotal = productsInCart.asMaps(String.class, String.class).stream()
                .mapToDouble(product -> shoppingCartPage.removeDollarSignFromPrice(product.get("total")))
                .sum();

        // Verify that the overview page is displayed
        assertThat(checkoutOverviewPage.checkTitle())
                .as("Checkout overview page title should be " + checkoutOverviewPage.getTitle())
                .isTrue();
        // Check that the product names are present in the overview page description
        assertThat(checkoutOverviewPage.verifyCartContents(productsInCart))
                .as("All product names should be present in the overview page description")
                .isTrue();

        // Verify that the subtotal price is displayed correctly
        assertThat(String.format("%.2f", actualSubTotal))
                .as("Total price should match expected value")
                .isEqualTo(String.format("%.2f", expectedSubTotal));

    }

    @Then("Sally views her cart and checks that all the products have been removed")
    public void sallyViewsHerCartAndChecksThatAllTheProductsHaveBeenRemoved() {
        // Navigate to the cart page
        shoppingCartPage.openShoppingCartPage();
        assertThat(shoppingCartPage.checkPageUrl())
                .as("Cart page URL should be correct")
                .isTrue();
        assertThat(shoppingCartPage.checkPageTitle())
                .as("Cart title should be 'Your Cart'")
                .isTrue();

        // Verify that the specified product is in the cart
        int cartCount = shoppingCartPage.getCartCount();
        assertThat(cartCount)
                .as("Cart count should be 0 after having removed all products.")
                .isEqualTo(0);
        System.out.println("Cart count after removal: " + cartCount);
    }

    @And("Sally checks the price total confirming that it is {double}")
    public void theTotalPriceShouldBe(Double totalPrice) {
        // Verify that the total price is displayed correctly
        String displayedTotalPrice = checkoutOverviewPage.getTotalPrice();
        assertThat(displayedTotalPrice)
                .as("Total price should match expected value")
                .isEqualTo("Total: $" + String.format("%.2f", totalPrice));
        System.out.println(displayedTotalPrice);

    }

    @When("Sally clicks on the finish button")
    public void sheClicksOnTheButton() {
        // Click on the specified button
        checkoutOverviewPage.finishButtonClick();
    }

    @Then("she should see the confirmation message")
    public void sheShouldSeeTheConfirmationMessage() {
        // Verify that the order confirmation message is displayed
        assertThat(checkoutCompletePage.getOrderConfirmationMessage())
                .as("Order confirmation message should match expected value")
                .isTrue();
        // Verify that the checkout complete page title  is correct
        assertThat(checkoutCompletePage.checkPageTitle())
                .as("Checkout complete page title should match expected value")
                .isTrue();
    }

    @And("the title is confirm")
    public void theTitleShouldBe() {
        // Verify that the title is displayed correctly
        assertThat(checkoutCompletePage.checkPageTitle())
                .as("Checkout complete page title should match expected value")
                .isTrue();
    }

    @When("Sally removes the {string} from her cart")
    public void sallyRemovesTheFromHerCart(String productName) {
        // First, get the current cart count
        int initialCartCount = shoppingCartPage.getCartCount();
        System.out.println("Initial cart count: " + initialCartCount);
        // Next, add the product to the cart
        shoppingCartPage.removeProductFromCart(productName);
        // Verify that the product is removed from the cart by checking the cart count against the initial count
        int finalCartCount = shoppingCartPage.getCartCount();
        System.out.println("Final cart count: " + finalCartCount);
        assertThat(finalCartCount)
                .as("Cart count should be less than initial count after removing a product")
                .isLessThan(initialCartCount);

    }

    @Then("Sally continues to the overview page for review only to cancel the order")
    public void sheContinuesToTheOverviewPageForReviewOnlyToCancelTheOrder() {
        // Click on the specified button
        checkoutOverviewPage.cancelButtonClick();
        // Verify that the user is redirected to the product list page
        assertThat(shoppingCartPage.checkPageUrl())
                .as("Cart page URL should be true")
                .isTrue();
        assertThat(shoppingCartPage.getTitle())
                .as("Cart title should be 'Your Cart'")
                .isEqualTo("Your Cart");
//        // Verify that the user is redirected to the cart page
//        assertThat(productList.checkPageUrl())
//                .as("Product page URL should be correct")
//                .isTrue();
//        assertThat(productList.getTitle())
//                .as("Product page title should be 'Products'")
//                .isEqualTo(PRODUCT_LIST_PAGE_TITLE);
//        System.out.println("Cart page title: " + shoppingCartPage.getTitle());
    }

    @Then("Sally continues to the information page for review only to cancel the order")
    public void sheContinuesToTheInformationPageForReviewOnlyToCancelTheOrder() {
        // Fill out the personal information form
        // Check page title
        assertThat(checkoutInformationPage.checkTitle())
                .as("Checkout information page title should be 'Checkout: Your Information'")
                .isTrue();
        System.out.println("Checkout information page title: " + checkoutInformationPage.getTitle());
        // Click on the cancel button
        checkoutInformationPage.cancelButtonClick();
        // Verify that the user is redirected to the product list page
        assertThat(shoppingCartPage.checkPageUrl())
                .as("Cart page URL should be true")
                .isTrue();
        assertThat(shoppingCartPage.getTitle())
                .as("Cart title should be 'Your Cart'")
                .isEqualTo("Your Cart");
        System.out.println("Cart page title: " + productList.getTitle());
    }

//    @And("Sally clicks on the checkout information cancel button")
//    public void sallyClicksOnTheCheckoutInformationCancelButton() {
//
//    }


//    @Then("she should be redirected to the cart page")
//    public void sheShouldBeRedirectedToTheShoppingCartPage() {
//        // Verify that the user is redirected to the product list page
//        assertThat(shoppingCartPage.checkPageUrl())
//                .as("Cart page URL should be true")
//                .isTrue();
//        assertThat(shoppingCartPage.getTitle())
//                .as("Cart title should be 'Your Cart'")
//                .isEqualTo("Your Cart");
//        System.out.println("Cart page title: " + productList.getTitle());
//    }
}
