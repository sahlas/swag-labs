package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.sahlas.fixtures.ScreenshotManager;
import com.sahlas.fixtures.TakesFinalScreenshot;
import io.cucumber.datatable.DataTable;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckoutOverviewPage {
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String CHECKOUT_OVERVIEW_PAGE_TITLE = dotenv.get("CHECKOUT_OVERVIEW_PAGE_TITLE", "Checkout: Overview");
    private static final String CHECKOUT_OVERVIEW_PAGE_URL = dotenv.get("CHECKOUT_OVERVIEW_PAGE_URL", "https://www.saucedemo.com/checkout-step-two.html");
    private final Page page;

    public CheckoutOverviewPage(Page page) {
        this.page = page;
    }

    /**
     * Check the title of the checkout overview page.
     *
     * @return  boolean true if the title matches "Checkout: Overview", false otherwise.
     * This method captures a screenshot of the title for verification purposes.
     */
    @Step("Check the title of the checkout overview page")
    public boolean checkTitle() {
        // Get the title text from the page element
        String title = page.getByTestId("title").textContent();
        boolean isTitleCorrect = title.equals(CHECKOUT_OVERVIEW_PAGE_TITLE);
        if (isTitleCorrect) {
            System.out.println("Checkout overview page title is correct: " + title);
        } else {
            System.out.println("Checkout overview page title is incorrect: " + title);
            ScreenshotManager.takeScreenshot(page, "checkout-overview-title-" + title);
        }
        return isTitleCorrect;
    }

    /**
     * Get the title of the checkout overview page.
     *
     * @return The title text of the page.
     */
    @Step("Get the title of the checkout overview page")
    public String getTitle() {
        return page.getByTestId("title").textContent();
    }


    /** get the total price of the items in the cart.
     * This method captures a screenshot of the total price for verification purposes.
     *
     * @return The total price as a String.
     */
    @Step("Get the total price of the items in the cart")
    public String getTotalPrice() {
        // Get the total price from the overview page
        ScreenshotManager.takeScreenshot(page, "total-label");
        // The total price is displayed in an element with the test ID "total-label"
        return page.getByTestId("total-label").textContent();
    }

    /**
     * Check if the product names in the cart match the expected product names.
     *
     * @param expectedProductNames An array of expected product names.
     * @return true if all expected product names are present, false otherwise.
     */
    @Step("Check product names")
    public boolean checkProductNames(String[] expectedProductNames) {
        List<Locator> productNames = page.getByTestId("inventory-item-name").all();
        boolean allNamesPresent = true;

        Set<String> actualNamesSet = productNames.stream()
                .map(Locator::textContent)
                .collect(Collectors.toSet());
        allNamesPresent = actualNamesSet.containsAll(Arrays.asList(expectedProductNames));

        return allNamesPresent;
    }

    /**
     * Click the Finish button to complete the checkout process.
     * This method captures a screenshot before and after clicking the button for verification purposes.
     */
    @Step("Click the Finish button")
    public void finishButtonClick() {
        // Click the finish button
        page.getByTestId("finish").click();
        System.out.println("Clicked on the Finish button");
    }

    /**
     * Verify if the product names in the cart match the expected product names.
     *
     * @param productsInCart A DataTable containing the expected product names.
     * @return true if all expected product names are present, false otherwise.
     */
    @Step("Verify cart contents")
    public boolean verifyCartContents(DataTable productsInCart) {
        String[] productNames = productsInCart.asMaps(String.class, String.class).stream()
                .map(product -> product.get("product"))
                .toArray(String[]::new);

        boolean allNamesPresent = checkProductNames(productNames);
        System.out.println(allNamesPresent
                ? "All product names are present in the cart."
                : "Not all product names are present in the cart.");
        return allNamesPresent;
    }

    /**
     * Get the subtotal price of the items in the cart.
     * This method captures a screenshot of the subtotal price for verification purposes.
     *
     * @return The subtotal price as a double.
     */
    @Step("Get the subtotal price of the items in the cart")
    public double getSubTotalPrice() {
        // The subtotal price is displayed in an element with the test ID "subtotal-label"
        return removeDollarSignFromPrice(page.getByTestId("subtotal-label").textContent());
    }

    private double removeDollarSignFromPrice(String priceText) {
        // Remove the dollar sign and parse the price as a double
        return Double.parseDouble(priceText.replace("$", "").trim().replace("Item total: ", ""));
    }


    /**
     * Click the Cancel button to return to the previous page.
     * This method captures a screenshot before and after clicking the button for verification purposes.
     */
    @Step("Click the Cancel button")
    public void cancelButtonClick() {
        // Click the cancel button
        System.out.println("Clicked on the Cancel button from: " + this.getTitle());
        ScreenshotManager.takeScreenshot(page, "cancel-button-before-click");
        page.getByTestId("cancel").click();
        ScreenshotManager.takeScreenshot(page, "cancel-button-clicked");
    }

    public boolean checkPageUrl() {
        // Get the current URL of the page
        String url = page.url();
        // Check if the URL matches the expected checkout overview page URL
        boolean isUrlCorrect = url.equals(CHECKOUT_OVERVIEW_PAGE_URL);
        if (isUrlCorrect) {
            System.out.println("Checkout overview page URL is correct: " + url);
        } else {
            System.out.println("Checkout overview page URL is incorrect: " + url);
            ScreenshotManager.takeScreenshot(page, "checkout-overview-url-" + url);
        }
        return isUrlCorrect;
    }
}
