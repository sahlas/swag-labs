package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.sahlas.fixtures.ScreenshotManager;
import com.sahlas.fixtures.TakesFinalScreenshot;
import io.cucumber.datatable.DataTable;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

import java.util.List;

public class ShoppingCartPage {

    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String SHOPPING_CART_PAGE_URL = dotenv.get("SHOPPING_CART_PAGE_URL", "https://www.saucedemo.com/cart.html");
    private static final String SHOPPING_CART_PAGE_TITLE = dotenv.get("SHOPPING_CART_TITLE", "Your Cart");
    private final Page page;


    public ShoppingCartPage(Page page) {
        this.page = page;
    }

    /**
     * Navigates to the shopping cart page using the configured URL.
     * This method uses Playwright's page.navigate() to load the shopping cart page.
     */
    @Step("Open shopping cart page")
    public void openShoppingCartPage() {
        // Navigate to the shopping cart page
        page.navigate(SHOPPING_CART_PAGE_URL);
    }

    @Step("Get the title of the shopping cart page")
    public String getTitle() {
        // Get the title text from the page element
        String title = page.getByTestId("title").textContent();
        // Log the title for debugging purposes
        System.out.println("Shopping cart page title: " + title);
        // Take a screenshot of the title for verification
        ScreenshotManager.takeScreenshot(page, "shopping-cart-title");
        return title;
    }

    /**
     * Retrieves the shopping cart page URL from the environment configuration.
     * The URL is loaded from environment variables with a fallback to the default SauceDemo URL.
     *
     * @return String The shopping cart page URL configured in SHOPPING_CART_PAGE_URL
     */
    @Step("Get shopping cart page URL")
    public boolean checkPageUrl() {

        // Log the URL for debugging purposes
        boolean isUrlCorrect = page.url().equals(SHOPPING_CART_PAGE_URL);
        System.out.println("Shopping cart page URL: " + page.url());
        if (isUrlCorrect) {
            System.out.println("Shopping cart page URL is correct: " + SHOPPING_CART_PAGE_URL);
        } else {
            System.out.println("Shopping cart page URL is incorrect: " + SHOPPING_CART_PAGE_URL);
            ScreenshotManager.takeScreenshot(page, "shopping-cart-page-url-incorrect");
        }
        return isUrlCorrect;
    }

    /**
     * Verifies that the shopping cart page displays the correct title.
     * This method extracts the page title from the UI and compares it against the expected
     * "Your Cart" text. A screenshot is taken for verification purposes regardless of the result.
     *
     * @return boolean true if the page title matches "Your Cart", false otherwise
     */
    @Step("Check cart title")
    public boolean checkPageTitle() {
        // Extract the title text from the page element
        String title = page.getByTestId("title").textContent();
        // Check if the title matches the expected "Your Cart" text
        boolean isTitleCorrect = title.equals(SHOPPING_CART_PAGE_TITLE);
        // Log the verification result
        if (isTitleCorrect) {
            System.out.println("Checkout page title is correct: " + title);
        } else {
            // Take screenshot for test evidence
            ScreenshotManager.takeScreenshot(page, "shopping-cart-page-title");
            System.out.println("Checkout page title is incorrect: " + title);
        }
        return isTitleCorrect;
    }

    /**
     * Retrieves the number of items currently in the shopping cart by reading the cart badge.
     * This method checks for the visibility of the shopping cart badge element and extracts
     * the item count from its text content. Screenshots are taken for both success and failure cases.
     *
     * @return int The number of items in the cart. Returns 0 if the cart badge is not visible
     * (indicating an empty cart or UI issue).
     */
    @Step("Get cart count")
    public int getCartCount() {
        // First check to see if the shopping cart badge is present
        if (!page.getByTestId("shopping-cart-badge").isVisible()) {
            // Cart badge not visible - likely means cart is empty or there's a UI issue
            System.out.println("Shopping cart icon is not visible");
            ScreenshotManager.takeScreenshot(page, "shopping-cart-icon-not-visible");
            return 0; // Return "0" if the cart icon is not visible
        } else {
            // Cart badge is visible - extract and parse the item count
            System.out.println("Shopping cart icon is visible");
            return Integer.parseInt(page.getByTestId("shopping-cart-badge").textContent());
        }
    }

    /**
     * Verifies that the shopping cart contents match the expected products from the DataTable.
     * This method compares each product in the cart with the expected products, checking
     * product name, quantity, and total price for exact matches.
     *
     * @param productTable DataTable containing expected product information with columns:
     *                     "product" (product name), "quantity" (item quantity), "total" (total price)
     * @return true if all expected products are found in the cart with matching details, false otherwise
     * @throws IllegalArgumentException if productTable is null or empty
     */
    @Step("verify cart contents")
    public boolean verifyCartContents(DataTable productTable) {
        // Get the expected products from the DataTable
        if (productTable == null || productTable.isEmpty()) {
            throw new IllegalArgumentException("Product table is empty or null");
        }

        // Locate the cart list container and get all inventory items
        Locator cartList = page.getByTestId("cart-list");
        List<Locator> subItems = cartList.all().get(0).getByTestId("inventory-item").all();

        // Check if cart is empty
        if (subItems.isEmpty()) {
            System.out.println("Cart is empty.");
            return false;
        }

        System.out.println("There were: " + subItems.size() + " items found in the cart.");

        // Verify that the number of expected products matches the number of items in cart
        if (productTable.asMaps(String.class, String.class).size() != subItems.size()) {
            System.out.println("Product count mismatch: expected " + productTable.asMaps(String.class, String.class).size() + ", found " + subItems.size());
            return false;
        }

        // Check if all expected products are present in the cart with correct details
        return productTable.asMaps(String.class, String.class).stream().allMatch(product -> {
            // Extract expected product details from DataTable
            String productName = product.get("product");
            String quantity = product.get("quantity");
            String total = product.get("total");

            // Search for matching product in cart items
            boolean productFound = subItems.stream().anyMatch(item -> {
                // Extract actual product details from cart item
                String itemName = trimmedProductTitle(item.getByTestId("inventory-item-name").textContent());
                String itemQuantity = item.getByTestId("item-quantity").textContent();
                String itemTotal = item.getByTestId("inventory-item-price").textContent();

                // Log comparison details for debugging
                System.out.println("Checking expected: " + itemName + ", Quantity: " + itemQuantity + ", Total: " + itemTotal);
                System.out.println("Checking actual item: " + productName + ", Quantity: " + quantity + ", Total: " + total);

                // Compare all product attributes for exact match
                return itemName.equals(productName) && itemQuantity.equals(quantity) && itemTotal.equals(total);
            });

            // Log verification result for each product
            if (!productFound) {
                System.out.println("Product not found in cart: " + productName);
            } else {
                System.out.println("Product found in cart: " + productName);
            }

            return productFound;
        });
    }



    private String trimmedProductTitle(String value) {
        return value.strip().replaceAll("\u00A0", "");
    }

    public double removeDollarSignFromPrice(String priceText) {
        // Remove the dollar sign and parse the price as a double
        return Double.parseDouble(priceText.replace("$", "").trim().replace("Item total: ", ""));
    }

    /**
     * Removes a product to the shopping cart by its name.
     * This method locates the product in the cart and clicks the "Remove" button.
     * A screenshot is taken after the product is added for verification purposes.
     *
     * @param productName The name of the product to add to the cart
     */
    @Step("Sally removes the product from her cart by product name")
    public void removeProductFromCart(String productName) {
        // Take a screenshot before removing the product
        ScreenshotManager.takeScreenshot(page, "product-before-removal-" + productName);
        // Click the remove button for the specified product
        Locator removeFromCartButton = page.getByTestId("remove-" + productName.toLowerCase().replace(" ", "-"));
        removeFromCartButton.click();
        // Take a screenshot after removing the product
        ScreenshotManager.takeScreenshot(page, "product-removed-from-cart-" + productName);
    }

    /**
     * Navigates back to the product list page by clicking the "Continue Shopping" button.
     * This method uses Playwright's Locator to find the button by its test ID and clicks it.
     * A screenshot is taken after clicking the continue shopping button for verification purposes.
     */
    @Step("Continue shopping")
    public void continueShopping() {
        // Click the continue shopping button to navigate back to the product list page
        Locator continueShoppingButton = page.getByTestId("continue-shopping");
        continueShoppingButton.click();
        // Take a screenshot after clicking the continue shopping button
        ScreenshotManager.takeScreenshot(page, "continue-shopping-button-clicked");
    }

    /**
     * Navigates back to the product list page by clicking the "Back to Products" button.
     * This method uses Playwright's Locator to find the button by its test ID and clicks it.
     * A screenshot is taken after clicking the back to products button for verification purposes.
     */
    @Step("Back to products")
    public void backToProducts() {
        // Click the back to products button to navigate back to the product list page
        Locator backToProductsButton = page.getByTestId("back-to-products");
        backToProductsButton.click();
        // Take a screenshot after clicking the back to products button
        ScreenshotManager.takeScreenshot(page, "back-to-products-button-clicked");
    }
}
