package com.sahlas.cucumber.stepdefinitions;

import com.sahlas.domain.User;
import com.sahlas.swaglabs.catalog.pageobjects.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Step definitions for Cucumber tests using Playwright.
 * This class contains the implementation of Gherkin steps for testing the Swag Labs application.
 */
public class PlaywrightCucumberStepDefinitions {
    // Load environment variables using dotenv
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    // Page object instances for different pages in the application
    LoginPage loginPage;
    ProductListPage productListPage;
    ProductDetailsPage productDetailsPage;
    ShoppingCartPage shoppingCartPage;
    CheckoutInformationPage checkoutInformationPage;
    CheckoutOverviewPage checkoutOverviewPage;
    CheckoutCompletePage checkoutCompletePage;

    /**
     * Initializes page objects before each test.
     * This method is executed before every scenario to set up the required page objects.
     */
    @Before
    public void setUp() {
        loginPage = new LoginPage(PlaywrightCucumberFixtures.getPage());
        productListPage = new ProductListPage(PlaywrightCucumberFixtures.getPage());
        productDetailsPage = new ProductDetailsPage(PlaywrightCucumberFixtures.getPage());
        checkoutInformationPage = new CheckoutInformationPage(PlaywrightCucumberFixtures.getPage());
        checkoutOverviewPage = new CheckoutOverviewPage(PlaywrightCucumberFixtures.getPage());
        shoppingCartPage = new ShoppingCartPage(PlaywrightCucumberFixtures.getPage());
        checkoutCompletePage = new CheckoutCompletePage(PlaywrightCucumberFixtures.getPage());
    }

    /**
     * Navigates to the login page and verifies the URL.
     */
    @Given("Sally is on the login page")
    public void sallyIsOnTheLoginPage() {
        loginPage.openHomePage();
        assertThat(loginPage.gtetUrl())
                .as("Login page URL should be correct")
                .isTrue();
    }

    /**
     * Logs in using the provided username and password.
     *
     * @param username The username to log in with.
     * @param password The password to log in with.
     */
    @When("Sally enters her {string} and {string}")
    public void she_enters_her_name_and_password(String username, String password) {
        User user = new User(username, password);
        loginPage.loginUser(user);
    }

    /**
     * Verifies that Sally is logged in successfully by checking the page title.
     */
    @Then("Sally should be logged in successfully")
    public void she_should_be_logged_in_successfully() {
        assertThat(loginPage.title())
                .as("Page title should be 'Products' after login")
                .isEqualTo("Products");
    }

    /**
     * Verifies that the error message displayed matches the expected value.
     *
     * @param errorMessage The expected error message.
     */
    @Then("the error message should be {string}")
    public void theErrorMessageShouldBe(String errorMessage) {
        assertThat(loginPage.errorMessage())
                .as("Error message should match expected value")
                .containsIgnoringCase(errorMessage);
    }

    /**
     * Sorts the product list by the specified criteria and verifies the sort option.
     *
     * @param sortCriteria The criteria to sort by (e.g., "Price (low to high)").
     */
    @When("Sally sorts by {string}")
    public void she_sorts_by(String sortCriteria) {
        productListPage.sortBy(sortCriteria);
        assertThat(productListPage.getSortOption(sortCriteria))
                .as("Sort option should match expected value")
                .isTrue();
    }

    /**
     * Verifies that the first product displayed matches the expected product name.
     *
     * @param productName The expected name of the first product.
     */
    @Then("the first product displayed should be {string}")
    public void theFirstProductDisplayedShouldBe(String productName) {
        assertThat(productListPage.getFirstProductName(productName))
                .as("First product name should match expected value")
                .isTrue();
    }

    /**
     * Navigate directly to the inventory page.
     */
    @Given("Sally opens a browser link to the inventory page")
    public void sallyOpensABrowserLinkToTheInventoryPage() {
        productListPage.openProductListPage();
    }

    /**
     * Verifies that Sally is redirected to the login page.
     */
    @When("Sally is redirected to the login page")
    public void redirectToTheLoginPage() {
        PlaywrightCucumberFixtures.getPage().waitForLoadState();
        assertThat(PlaywrightCucumberFixtures.getPage().url())
                .as("Login page is loaded with an error message")
                .isEqualTo("https://www.saucedemo.com/");
    }

    /**
     * Verifies that Sally is notified of a login error with the specified message.
     *
     * @param errorMessage The expected error message.
     */
    @Then("Sally should be notified of login error {string}")
    public void sheShouldBeNotifiedOfLoginError(String errorMessage) {
        assertThat(loginPage.errorMessage())
                .as("Error message should match expected value")
                .isEqualTo(errorMessage);
    }

    /**
     * Verifies that the product's status indicates it has been added to the cart.
     *
     * @param productName The name of the product to verify.
     */
    @Then("the {string} status indicates it has been added to the cart")
    public void theProductShouldBeAddedToHerCart(String productName) {
        assertThat(productListPage.getProductButtonState(productName))
                .as("Product button state should be 'Remove' after adding to cart")
                .isTrue();
        int cartCount = productListPage.getCartCount();
        assertThat(cartCount)
                .as("Cart count should be greater than 0")
                .isGreaterThan(0);
        System.out.println("Cart count: " + cartCount);
    }

    /**
     * Adds a product to Sally's cart and verifies the cart count.
     *
     * @param productName The name of the product to add.
     */
    @When("Sally adds a {string} to her cart")
    public void sheAddsAToHerCart(String productName) {
        productListPage.addProductToCart(productName);
        assertThat(productListPage.checkPageUrl())
                .as("Should be on the inventory page")
                .isTrue();
        assertThat(productListPage.getCartCount())
                .as("Cart count should be greater than 0 after adding a product")
                .isGreaterThan(0);
    }


    /**
     * Adds multiple products to Sally's cart based on the provided DataTable.
     *
     * @param productTable A DataTable containing product names to add.
     */
    @When("Sally adds the following products to the cart")
    public void theUserAddsTheFollowingProductsToTheCart(DataTable productTable) {
        assertThat(productListPage.checkPageUrl())
                .as("Should be on the inventory page")
                .isTrue();
        assertThat(productListPage.checkTitle())
                .as("Product list title should contain 'Products'")
                .isTrue();
        List<Map<String, String>> products = productTable.asMaps(String.class, String.class);

        for (Map<String, String> product : products) {
            String productName = product.get("product");
            productListPage.addProductToCart(productName);
        }
    }

    /**
     * Verifies that the shopping cart contains all products added for checkout.
     *
     * @param productTable A DataTable containing expected product details.
     */
    @Then("the shopping cart page should indicate all products picked for checkout")
    public void theShoppingCartShouldContainAllAddedProducts(DataTable productTable) {
        boolean dataMatch = shoppingCartPage.verifyCartContents(productTable);
        assertThat(dataMatch)
                .as("All product details should be represented in the cart")
                .isTrue();
    }

    /**
     * Removes a specific product from Sally's cart on the inventory page.
     *
     * @param productName The name of the product to remove.
     */
    @And("Sally removes the {string} from her cart on inventory page")
    public void removeItemFromTheInventoryPage(String productName) {
        int initialCartCount = productListPage.getCartCount();
        System.out.println("Initial cart count: " + initialCartCount);
        productListPage.removeProductFromCart(productName);
        int finalCartCount = productListPage.getCartCount();
        System.out.println("Final cart count: " + finalCartCount);
        assertThat(finalCartCount)
                .as("Cart count should be less than initial count after removing a product")
                .isLessThan(initialCartCount);
    }

    /**
     * Verifies that only the expected products remain in Sally's cart.
     */
    @Then("check that only the expected products are in her cart")
    public void checkThatOnlyTheRemainingProductsAreInHerCart() {
        int cartCount = productListPage.getCartCount();
        assertThat(cartCount)
                .as("Cart count should be 1 less after removing a product")
                .isEqualTo(1);
        System.out.println("Cart count after removal: " + cartCount);
    }

    /**
     * Navigates to the cart page and verifies the page title and URL.
     */
    @When("Sally views her cart")
    public void sheViewsHerCart() {
        shoppingCartPage.openShoppingCartPage();
        assertThat(shoppingCartPage.checkPageUrl())
                .as("Cart page URL should be correct")
                .isTrue();
        assertThat(shoppingCartPage.checkPageTitle())
                .as("Cart title should be 'Your Cart'")
                .isTrue();
    }

    /**
     * Verifies that Sally sees the expected products in her cart.
     */
    @Then("Sally should see the following products in her cart")
    public void sheShouldSeeTheFollowingProductsInHerCart() {
        // Implementation to verify products in the cart can be added here.
    }

    /**
     * Removes all products from Sally's cart based on the provided DataTable.
     *
     * @param productTable A DataTable containing product names to remove.
     */
    @And("Sally removes all products from her cart")
    public void sallyRemovesAllProductsFromHerCart(DataTable productTable) {
        int initialCartCount = productListPage.getCartCount();
        System.out.println("Initial cart count: " + initialCartCount);

        List<Map<String, String>> products = productTable.asMaps(String.class, String.class);

        for (Map<String, String> product : products) {
            String productName = product.get("product");
            productListPage.removeProductFromCart(productName);
        }
    }

    /**
     * Logs in Sally using credentials based on the user type specified in environment variables.
     * The method retrieves the user type from the environment, determines the corresponding
     * username and password, and logs in Sally by navigating to the login page and submitting the credentials.
     */
    @Given("Sally logs in")
    public void sallyHasLoggedInWithHerAccount() {
        // Retrieve the user type from environment variables, defaulting to "standard_user"
        String user_type = dotenv.get("USER_TYPE", "standard_user");

        // Initialize username and password variables
        String username;
        String password;

        // Determine the username and password based on the user type
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
                // Throw an exception if the user type is unknown
                throw new IllegalArgumentException("Unknown user type: " + user_type);
        }

        // Navigate to the login page
        loginPage.openHomePage();

        // Create a User object with the determined credentials and log in
        User currentUser = new User(username, password);
        loginPage.loginUser(currentUser);
    }

    /**
     * Begins the checkout process by clicking the checkout button and verifying the checkout page.
     */
    @When("Sally begins the checkout process")
    public void sheProceedsToCheckout() {
        // Click on the checkout button
        productListPage.clickCheckoutButton();
        // Verify that the checkout page is displayed
        assertThat(checkoutInformationPage.checkPageUrl())
                .as("Checkout overview page URL should be correct")
                .isTrue();

        assertThat(checkoutInformationPage.checkTitle())
                .as("Checkout page title should be 'Checkout: Your Information'")
                .isTrue();
    }

    /**
     * Fills in Sally's personal information on the checkout page.
     *
     * @param personalInfoTable A DataTable containing Sally's personal information.
     */
    @Then("Sally fills in her personal information")
    public void sheFillsInHerPersonalInformation(DataTable personalInfoTable) {
        checkoutInformationPage.fillInPersonalInformation(personalInfoTable);

        // Verify that the personal information is filled in correctly
        assertThat(checkoutInformationPage.checkTitle())
                .as("Checkout information page title should be 'Checkout: Your Information'")
                .isTrue();
    }

    /**
     * Continues to the overview page for review and verifies the subtotal and product details.
     *
     * @param productsInCart A DataTable containing the products in the cart.
     */
    @And("Sally continues to the overview page for review")
    public void sheShouldBeOnTheOverviewPage(DataTable productsInCart) {
        checkoutInformationPage.buttonClick("continue");
        double actualSubTotal = checkoutOverviewPage.getSubTotalPrice();
        double expectedSubTotal = productsInCart.asMaps(String.class, String.class).stream()
                .mapToDouble(product ->
                        shoppingCartPage.removeDollarSignFromPrice(product.get("total")))
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

    /**
     * Views the cart and verifies that all products have been removed.
     */
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

    /**
     * Verifies that the total price displayed matches the expected value.
     *
     * @param totalPrice The expected total price.
     */
    @And("Sally checks the price total confirming that it is {double}")
    public void theTotalPriceShouldBe(Double totalPrice) {
        // Verify that the total price is displayed correctly
        String displayedTotalPrice = checkoutOverviewPage.getTotalPrice();
        assertThat(displayedTotalPrice)
                .as("Total price should match expected value")
                .isEqualTo("Total: $" + String.format("%.2f", totalPrice));
        System.out.println(displayedTotalPrice);
    }

    /**
     * Completes the checkout process by clicking the finish button on the overview page.
     */
    @When("Sally clicks on the finish button")
    public void sheClicksOnTheButton() {
        // Click on the specified button
        checkoutOverviewPage.finishButtonClick();
    }

    /**
     * Verifies that the order confirmation message and the checkout complete page title are displayed correctly.
     */
    @Then("she should see the confirmation message")
    public void sheShouldSeeTheConfirmationMessage() {
        // Verify the URL of the checkout complete page
        assertThat(checkoutCompletePage.checkPageUrl())
                .as("Checkout complete page URL should be correct" )
                .isTrue();
        // Verify that the order confirmation message is displayed
        assertThat(checkoutCompletePage.getOrderConfirmationMessage())
                .as("Order confirmation message should match expected value")
                .isTrue();
        // Verify that the checkout complete page title is correct
        assertThat(checkoutCompletePage.checkPageTitle())
                .as("Checkout complete page title should match expected value")
                .isTrue();
    }

    /**
     * Verifies that the title of the checkout complete page is displayed correctly.
     */
    @And("the title is confirm")
    public void theTitleShouldBe() {
        // Verify that the title is displayed correctly
        assertThat(checkoutCompletePage.checkPageTitle())
                .as("Checkout complete page title should match expected value")
                .isTrue();
    }

    /**
     * Removes a specific product from Sally's cart and verifies the cart count is updated correctly.
     *
     * @param productName The name of the product to remove.
     */
    @When("Sally removes the {string} from her cart")
    public void sallyRemovesTheFromHerCart(String productName) {
        // First, get the current cart count
        int initialCartCount = shoppingCartPage.getCartCount();
        System.out.println("Initial cart count: " + initialCartCount);
        // Remove the product from the cart
        shoppingCartPage.removeProductFromCart(productName);
        // Verify that the product is removed from the cart by checking the cart count against the initial count
        int finalCartCount = shoppingCartPage.getCartCount();
        System.out.println("Final cart count: " + finalCartCount);
        assertThat(finalCartCount)
                .as("Cart count should be less than initial count after removing a product")
                .isLessThan(initialCartCount);
    }

    /**
     * Cancels the order from the overview page and verifies that Sally is redirected to the cart page.
     */
    @Then("Sally continues to the overview page for review only to cancel the order")
    public void sheContinuesToTheOverviewPageForReviewOnlyToCancelTheOrder() {
        // Click on the cancel button
        checkoutOverviewPage.cancelButtonClick();
        // Verify that the user is redirected to the product list page
        assertThat(shoppingCartPage.checkPageUrl())
                .as("Cart page URL should be true")
                .isTrue();
        // Check the cart page title
        assertThat(shoppingCartPage.checkPageTitle())
                .as("Cart title should be " + shoppingCartPage.getTitle())
                .isTrue();
    }

    /**
     * Cancels the order from the information page and verifies that Sally is redirected to the cart page.
     */
    @Then("Sally continues to the information page for review only to cancel the order")
    public void sheContinuesToTheInformationPageForReviewOnlyToCancelTheOrder() {
        // Check page title
        assertThat(checkoutInformationPage.checkTitle())
                .as("Checkout information page title should be 'Checkout: Your Information'")
                .isTrue();
        assertThat(checkoutOverviewPage.checkPageUrl())
                .as("Checkout overview page URL should be correct")
                .isTrue();
        // Click on the cancel button
        checkoutInformationPage.cancelButtonClick();
        // Verify that the user is redirected to the product list page
        assertThat(shoppingCartPage.checkPageUrl())
                .as("Cart page URL should be true")
                .isTrue();
        assertThat(shoppingCartPage.checkPageTitle())
                .as("Cart title should be " + shoppingCartPage.getTitle())
                .isTrue();
    }

    /**
     * Sally views the details of a specific product by clicking on its name.
     *
     * @param productName The name of the product to view details for.
     */
    @When("Sally views the details of the {string} by clicking on the product name")
    public void sallyViewsTheDetailsOfTheByClickingOnTheProductName(String productName) {
        // Click on the product name to view details
        productListPage.clickOnProductName(productName);
        // Wait for the product details page to load
        productDetailsPage.waitForPageLoad();
        // Verify that the product details page is displayed
        assertThat(productDetailsPage.checkPageUrl())
                .as("Product details page URL should be correct")
                .isTrue();
        assertThat(productDetailsPage.checkTitle())
                .as("Product details page title should be" + productDetailsPage.getTitle())
                .isTrue();
    }

    /**
     * Verifies that the product details page displays the expected information for the specified products.
     *
     * @param productTable A DataTable containing product details to verify.
     */
    @Then("the product details page should display the following information")
    public void theProductDetailsPageShouldDisplayTheFollowingInformation(DataTable productTable) {
        // Verify that the product details page displays the expected information
        List<Map<String, String>> products = productTable.asMaps(String.class, String.class);
        for (Map<String, String> product : products) {
            String productName = product.get("product_name");
            String productDescription = product.get("description");
            String productPrice = product.get("price");
            boolean takeScreenshot = productDetailsPage.takeScreenshot(productName);
            assertThat(takeScreenshot)
                    .as("Screenshot should be taken successfully")
                    .isTrue();
            assertThat(productDetailsPage.getProductName())
                    .as("Product name should match expected value")
                    .isEqualTo(productName);
            assertThat(productDetailsPage.getProductPrice())
                    .as("Product price should match expected value")
                    .isEqualTo(productPrice);
            assertThat(productDetailsPage.getProductDescription())
                    .as("Product description should match expected value")
                    .isEqualTo(productDescription);
            System.out.println("Product name: " + productDetailsPage.getProductName());
            System.out.println("Product description: " + productDetailsPage.getProductDescription());
            System.out.println("Product price: " + productDetailsPage.getProductPrice());
        }
    }

    /**
     * Verifies that the product image for the specified product is displayed on the product details page.
     *
     * @param productName The name of the product to check for its image.
     */
    @And("the product image for {string} should be displayed")
    public void theProductImageShouldBeDisplayed(String productName) {
        // Verify that the product image is displayed on the product details page

        assertThat(productDetailsPage.getProductImage(productName))
                .as("Product image should be displayed")
                .isNotNull();
        System.out.println("Product image is displayed.");
    }

    /**
     * Verifies that the product price displayed on the product details page matches the expected value.
     *
     * @param price The expected price of the product.
     */
    @And("the price should be {string}")
    public void thePriceShouldBe(String price) {
        // Verify that the product price is displayed correctly
        assertThat(productDetailsPage.getProductPrice())
                .as("Product price should match expected value")
                .isEqualTo(price);
        System.out.println("Product price: " + productDetailsPage.getProductPrice());
    }

    /**
     * Sally continues shopping after adding products to her cart by navigating back to the product list page.
     */
    @When("Sally continues shopping after adding products to her cart")
    public void sallyContinuesShoppingAfterAddingProductsToHerCart() {
        // Proceed to the shopping cart page
        shoppingCartPage.openShoppingCartPage();
        // Verify that the cart page is displayed
        assertThat(shoppingCartPage.checkPageUrl())
                .as("Cart page URL should be correct")
                .isTrue();
        assertThat(shoppingCartPage.checkPageTitle())
                .as("Cart title should be 'Your Cart'")
                .isTrue();
        // Go back to the product list page by clicking the continue shopping button
        shoppingCartPage.continueShopping();

    }

    @Then("she should be able to view the inventory page and add more products")
    public void sheShouldBeAbleToViewTheInventoryPageAndAddMoreProducts() {
        // Verify that the product list page is displayed
        assertThat(productListPage.checkPageUrl())
                .as("Product list page URL should be correct")
                .isTrue();
        assertThat(productListPage.checkTitle())
                .as("Product list title should be 'Products'")
                .isTrue();
    }
}
