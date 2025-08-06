package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.sahlas.fixtures.ScreenshotManager;
import com.sahlas.fixtures.TakesFinalScreenshot;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

public class ProductDetailsPage implements TakesFinalScreenshot {
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
     * Get the URL of the product details page.
     *
     * @return The URL of the product details page.
     */
    @Step("Get Product Name")
    public String getProductName() {
        // Implementation to retrieve product name from the page
        return page.getByTestId("inventory-item-name").textContent(); // Placeholder return value
    }

    /**
     * Get the URL of the product details page.
     *
     * @return The URL of the product details page.
     */
    @Step("Get Product Price")
    public String getProductPrice() {
        // Implementation to retrieve product price from the page
        return page.getByTestId("inventory-item-price").textContent(); // Placeholder return value
    }

    /**
     * Get the URL of the product details page.
     *
     * @return The URL of the product details page.
     */
    @Step("Get Product Description")
    public String getProductDescription() {
        // Implementation to retrieve product description from the page
        return page.getByTestId("inventory-item-desc").textContent(); // Placeholder return value
    }

    /**
     * Get the URL of the product details page.
     *
     * @return The URL of the product details page.
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
        // Get the title of the page
        String title = getTitle();
        // Check if the title matches the expected product details page title
        return title.equals(PRODUCT_DETAILS_PAGE_TITLE);
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
     * Get the URL of the product details page.
     *
     * @return The URL of the product details page.
     */
    @Step("Get the title of the product details page")
    public String getTitle() {
        // Get the title text from the page element
        String title = page.getByTestId("title").textContent();
        boolean isTitleCorrect = title.equals(PRODUCT_DETAILS_PAGE_TITLE);
        if (isTitleCorrect) {
            System.out.println("Product details page title is correct: " + title);
            ScreenshotManager.takeScreenshot(page, "product-details-title-" + title);
        } else {
            System.out.println("Product details page title is incorrect: " + title);
            ScreenshotManager.takeScreenshot(page, "product-details-title-" + title);
        }
        return title;
    }
}
