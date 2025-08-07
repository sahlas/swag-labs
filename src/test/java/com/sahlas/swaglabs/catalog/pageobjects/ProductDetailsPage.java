package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.sahlas.fixtures.ScreenshotManager;
import com.sahlas.fixtures.TakesFinalScreenshot;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

public class ProductDetailsPage {
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String PRODUCT_DETAILS_PAGE_URL = dotenv.get("PRODUCT_DETAILS_PAGE_URL", "https://www.saucedemo.com/inventory-details.html");
    private static final String PRODUCT_DETAILS_PAGE_TITLE = dotenv.get("PRODUCT_DETAILS_PAGE_TITLE", "Swag Labs");
    private final Page page;

    public ProductDetailsPage(Page page) {
        this.page = page;
    }

    // Wait for the page to load completely
    public void waitForPageLoad() {
        page.waitForLoadState(); // Waits for the page to load completely
    }

    /**
     * Get the product name from the product details page.
     *
     * @return The name of the product.
     */
    @Step("Get Product Name")
    public String getProductName() {
        // Implementation to retrieve product name from the page
        return page.getByTestId("inventory-item-name").textContent(); // Placeholder return value
    }

    /** Get the product price from the product details page.
     *
     * @return The price of the product.
     */
    @Step("Get Product Price")
    public String getProductPrice() {
        // Implementation to retrieve product price from the page
        return page.getByTestId("inventory-item-price").textContent(); // Placeholder return value
    }

    /**
     *  Get the product description from the product details page.
     *
     * @return The description of the product.
     */
    @Step("Get Product Description")
    public String getProductDescription() {
        // Implementation to retrieve product description from the page
        return page.getByTestId("inventory-item-desc").textContent(); // Placeholder return value
    }

    /**
     * Check if the page URL is correct.
     *
     * @return true if the URL matches the expected product details page URL, false otherwise.
     */
    @Step("Check if the page URL is correct")
    public boolean checkPageUrl() {
        // strip query parameters and fragments before comparison
        String url = page.url().split("[?#]")[0];
        return url.equals(PRODUCT_DETAILS_PAGE_URL);
    }

    /**
     * Check if the product details page title is correct.
     *
     * @return true if the title matches the expected product details page title, false otherwise.
     */
    @Step("Check if the page title is correct")
    public boolean checkTitle() {
        // Check if the page title identifiable by the class .app_log matches the expected product details page value
        String appLogo = page.locator(".app_logo").textContent();
        if (appLogo == null || appLogo.isEmpty()) {
            System.err.println("App logo is not found on the page.");
            return false;
        }

        boolean isTitleCorrect = appLogo.equals(PRODUCT_DETAILS_PAGE_TITLE);
        if (isTitleCorrect) {
            System.out.println("Product details page title is correct: " + appLogo);
        } else {
            System.out.println("Product details page title is incorrect: " + appLogo);
            ScreenshotManager.takeScreenshot(page, "product-details-title-" + appLogo);
        }
        return isTitleCorrect;
    }

    /**
     * Get the product image from the product details page.
     *
     * @param productName The name of the product to retrieve the image for.
     * @return true if the product image is visible, false otherwise.
     */
    @Step("Get Product Image")
    public boolean getProductImage(String productName) {
        // Implementation to retrieve product image from the page
        // First locate the product
        Locator product = page.getByTestId("inventory-item");
        // Then find the image within that product
        Locator productImageLocator = product.getByTestId("product-image");
        Locator addToCartButton = product.getByTestId("item-" + productName.toLowerCase().replace(" ", "-") + "-img");
        ScreenshotManager.takeScreenshot(page, "product-details-" + productName.toLowerCase().replace(" ", "-"));
        return page.getByTestId("product-image").isVisible(); // Placeholder return value
    }

    /**
     * Take a screenshot of the product details page.
     *
     * @param productName The name of the product to include in the screenshot filename.
     * @return true if the screenshot was taken successfully, false otherwise.
     */
    @Step("Take a screenshot of the product details page")
    public boolean takeScreenshot(String productName) {
        try {
            ScreenshotManager.takeScreenshot(page, "product-details-" + productName.toLowerCase().replace(" ", "-"));
            return true;
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the title of the product details page.
     *
     * @return The title of the product details page.
     */
    @Step("Get the title of the product details page")
    public String getTitle() {
        // Get the title text from the page element
        String title = page.locator(".app_logo").textContent();
        if (title == null || title.isEmpty()) {
            System.err.println("App logo is not found on the page.");
            return "";
        }
        return title;
    }
}
