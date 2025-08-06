package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.sahlas.fixtures.ScreenshotManager;
import com.sahlas.fixtures.TakesFinalScreenshot;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

public class ProductListPage implements TakesFinalScreenshot {
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String PRODUCT_LIST_PAGE_URL = dotenv.get("PRODUCT_LIST_PAGE_URL", "https://www.saucedemo.com/inventory.html");
    private final Page page;

    public ProductListPage(Page page) {
        this.page = page;
    }

    /**
     * Retrieves the title of the product list page.
     * This method captures a screenshot for verification and returns the text content
     * of the title element identified by its test ID.
     *
     * @return String The title of the product list page.
     */
    @Step("Get the title of the product list page")
    public String getTitle() {
        ScreenshotManager.takeScreenshot(page, "title is equal to Product");
        return page.getByTestId("title").textContent();
    }

    /**
     * Verifies if the first product name in the product list matches the given product name.
     * Captures a screenshot for both success and failure cases.
     *
     * @param productName The expected name of the first product.
     * @return boolean true if the first product name matches the given name, false otherwise.
     */
    @Step("value = 'Get product names'")
    public boolean getFirstProductName(String productName) {
        String firstProductName = page.getByTestId("inventory-item").first().getByTestId("inventory-item-name").textContent();
        boolean isEqual = firstProductName.equals(productName);
        if (isEqual) {
            System.out.println("First product name is equal to: " + productName);
            ScreenshotManager.takeScreenshot(page, "first-product-name-" + productName);
            return true;
        } else {
            System.out.println("First product name is not equal to: " + productName);
            ScreenshotManager.takeScreenshot(page, "first-product-name-not-equal-" + firstProductName);
            return false;
        }
    }

    /**
     * Sorts the products in the product list by the given criteria.
     * Captures a screenshot after sorting.
     *
     * @param sortCriteria The criteria to sort the products by (e.g., price, name).
     */
    @Step("value = 'Sort products by {sortCriteria}'")
    public void sortBy(String sortCriteria) {
        System.out.println("Sorted by: " + sortCriteria);
        page.getByTestId("product-sort-container").selectOption(sortCriteria);
        ScreenshotManager.takeScreenshot(page, "sorted-by-" + sortCriteria);
    }

    /**
     * Verifies if the given sort option is currently selected.
     * Captures a screenshot for both success and failure cases.
     *
     * @param sortCriteria The expected sort option.
     * @return boolean true if the given sort option is selected, false otherwise.
     */
    @Step("value = 'Get sort option {sortCriteria}'")
    public boolean getSortOption(String sortCriteria) {
        String selectedOption = page.getByTestId("active-option").textContent();
        System.out.println("Selected sort option: " + selectedOption);
        boolean isSelected = selectedOption.equals(sortCriteria);
        if (isSelected) {
            System.out.println("Sort option is selected: " + selectedOption);
            ScreenshotManager.takeScreenshot(page, "sort-option-" + selectedOption);
            return true;
        } else {
            System.out.println("Sort option is not selected: " + selectedOption);
            ScreenshotManager.takeScreenshot(page, "sort-option-not-selected-" + selectedOption);
            return false;
        }
    }

    /**
     * Navigates to the product list page.
     * Captures a screenshot after navigation.
     */
    @Step("value = 'Open product list page'")
    public void openProductListPage() {
        page.navigate(PRODUCT_LIST_PAGE_URL);
        ScreenshotManager.takeScreenshot(page, "product-list-page");
    }

    /**
     * Adds a product to the shopping cart by its name.
     * Captures a screenshot after the product is added.
     *
     * @param productName The name of the product to add to the cart.
     */
    @Step("add product to cart")
    public void addProductToCart(String productName) {
        Locator product = page.getByTestId("inventory-item");
        Locator addToCartButton = product.getByTestId("add-to-cart-" + productName.toLowerCase().replace(" ", "-"));
        addToCartButton.click();
        ScreenshotManager.takeScreenshot(page, "product-added-to-cart-" + productName);
    }

    /**
     * Removes a product from the shopping cart by its name.
     * Captures a screenshot after the product is removed.
     *
     * @param productName The name of the product to remove from the cart.
     */
    @Step("Sally removes the product from her cart on inventory page")
    public void removeProductFromCart(String productName) {
        Locator product = page.getByTestId("inventory-item");
        Locator removeFromCartButton = product.getByTestId("remove-" + productName.toLowerCase().replace(" ", "-"));
        removeFromCartButton.click();
        ScreenshotManager.takeScreenshot(page, "product-removed-from-cart-" + productName);
    }

    /**
     * Retrieves the number of items currently in the shopping cart.
     * Captures a screenshot for both success and failure cases.
     *
     * @return int The number of items in the cart. Returns 0 if the cart badge is not visible.
     */
    @Step("get product count")
    public int getCartCount() {
        if (!page.getByTestId("shopping-cart-badge").isVisible()) {
            System.out.println("Shopping cart icon is not visible");
            ScreenshotManager.takeScreenshot(page, "shopping-cart-icon-not-visible");
            return 0;
        } else {
            System.out.println("Shopping cart icon is visible");
            int cartCount = Integer.parseInt(page.getByTestId("shopping-cart-badge").textContent());
            ScreenshotManager.takeScreenshot(page, "cart-count");
            return cartCount;
        }
    }

    /**
     * Clicks the checkout button on the shopping cart page and verifies the navigation to the checkout page.
     * This method captures a screenshot after the button is clicked and asserts that the page title
     * matches the expected "Checkout: Your Information" text.
     */
    @Step("click checkout button")
    public void clickCheckoutButton() {
        // Click the checkout button on the cart page
        page.getByTestId("checkout").click();
        ScreenshotManager.takeScreenshot(page, "checkout-button-clicked");
    }


    /**
     * Checks if the current page URL matches the expected product list page URL.
     * This method captures a screenshot if the URL is not correct.
     * @return boolean true if the current URL matches the expected URL, false otherwise.
     */
    @Step("Check if the current page URL is correct")
    public boolean checkPageUrl() {
        String currentUrl = page.url();
        boolean isCorrectUrl = currentUrl.equals(PRODUCT_LIST_PAGE_URL);
        if (isCorrectUrl) {
            System.out.println("Current URL is correct: " + currentUrl);
        } else {
            System.out.println("Current URL is not correct: " + currentUrl);
            ScreenshotManager.takeScreenshot(page, "current-url-not-correct");
        }
        return isCorrectUrl;
    }

    /**
     * Checks if the current page title matches the expected product list page title.
     * This method captures a screenshot if the title is not correct.
     * @return boolean true if the current title matches the expected title, false otherwise.
     */
    @Step("Chck if product button is enabled or marked for removal")
    public boolean getProductButtonState(String productName) {

        Locator productButton = page.getByTestId("remove-" + productName.toLowerCase().replace(" ", "-"));
        boolean isEnabled = productButton.isEnabled();
        if (isEnabled) {
            System.out.println("Product button is enabled.");
            ScreenshotManager.takeScreenshot(page, "product-button-enabled");
        } else {
            System.out.println("Product button is disabled.");
            ScreenshotManager.takeScreenshot(page, "product-button-disabled");
        }
        return isEnabled;
    }

    /**
     * Clicks on the product name to navigate to the product details page.
     * This method captures a screenshot after clicking on the product name.
     * @param productName The name of the product to click on.
     */
    @Step("Navigate to product details page by clicking on product name")
    public void clickOnProductName(String productName) {
        // Click on the product name to navigate to the product details page
        Locator product = page.getByTestId("inventory-item");
        Locator productNameLocator = product.getByTestId("inventory-item-name").filter(new Locator.FilterOptions()
                .setHasText(productName));
        ScreenshotManager.takeScreenshot(page, "product-name-clicked-" + productName);
        productNameLocator.click();
    }

    /**
     * Checks if the page title is correct.
     * This method captures a screenshot if the title is not correct.
     * @return boolean true if the page title matches the expected product list page title, false otherwise.
     */
    @Step("Check if the page title is correct")
    public boolean checkTitle() {
        // Get the title of the page
        String title = getTitle();

        // Check if the title matches the expected product list page title
        boolean isTitleCorrect = title.equals("Swag Labs");
        if (isTitleCorrect) {
            System.out.println("Page title is correct: " + title);
            ScreenshotManager.takeScreenshot(page, "page-title-correct");
        } else {
            System.out.println("Page title is not correct: " + title);
            ScreenshotManager.takeScreenshot(page, "page-title-not-correct");
        }
        return isTitleCorrect;
    }
}
