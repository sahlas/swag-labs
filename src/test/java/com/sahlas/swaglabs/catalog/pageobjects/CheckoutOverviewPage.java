package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.sahlas.fixtures.ScreenshotManager;
import io.cucumber.datatable.DataTable;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

import java.util.List;

public class CheckoutOverviewPage {
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String CHECKOUT_OVERVIEW_PAGE_TITLE = dotenv.get("CHECKOUT_OVERVIEW_PAGE_TITLE", "Checkout: Overview");
    private static final String URL = dotenv.get("URL", "https://www.saucedemo.com/checkout-step-two.html");
    private final Page page;

    public CheckoutOverviewPage(Page page) {
        this.page = page;
    }

    @Step("Check the title of the checkout overview page")
    public boolean checkTitle() {
        // Get the title text from the page element
        String title = page.getByTestId("title").textContent();
        boolean isTitleCorrect = title.equals(CHECKOUT_OVERVIEW_PAGE_TITLE);
        if (isTitleCorrect) {
            System.out.println("Checkout overview page title is correct: " + title);
            ScreenshotManager.takeScreenshot(page, "checkout-overview-title-" + title);
        } else {
            System.out.println("Checkout overview page title is incorrect: " + title);
            ScreenshotManager.takeScreenshot(page, "checkout-overview-title-" + title);
        }
        return isTitleCorrect;
    }

    @Step("value = 'Get the title of the checkout overview page'")
    public String getTitle() {
        return page.getByTestId("title").textContent();
    }

    @Step("value = 'Get shipping information'")
    public String getShippingInformation() {
        // Get the shipping information value from the overview page
        return page.getByTestId("shipping-info-value").textContent();
    }

    @Step("value = 'Get the total price'")
    public String getTotalPrice() {
        // Get the total price from the overview page
        ScreenshotManager.takeScreenshot(page, "total-label");
        // The total price is displayed in an element with the test ID "total-label"
        return page.getByTestId("total-label").textContent();
    }

    // Check that the product names in the cart
    @Step("value = 'Check product names'")
    public boolean checkProductNames(String[] expectedProductNames) {
        List<Locator> productNames = page.getByTestId("inventory-item-name").all();
        boolean allNamesPresent = true;

        // loop through the expected product names
        // in the inner loop check if the product name is present in the actual product names
        for (Locator productNameLocator : productNames) {
            String productName = productNameLocator.textContent();
            allNamesPresent = false;

            for (String expectedName : expectedProductNames) {
                if (productName.contains(expectedName)) {
                    System.out.println("Product name found in description: " + expectedName);
                    allNamesPresent = true;
                    break;
                }
            }
        }

        return allNamesPresent;
    }

    @Step("value = 'Click the Finish button'")
    public void finishButtonClick() {
        ScreenshotManager.takeScreenshot(page, "finish-button-click");
        // Click the finish button
        page.getByTestId("finish").click();
        System.out.println("Clicked on the Finish button");
    }

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

    public boolean getUrl() {
        String currentUrl = page.url();
        boolean isUrlCorrect = currentUrl.equals(URL);
        if (isUrlCorrect) {
            System.out.println("Current URL is correct: " + currentUrl);
        } else {
            System.out.println("Current URL is incorrect: " + currentUrl);
        }
        return isUrlCorrect;
    }


    public double getSubTotalPrice() {
        // Get the subtotal price from the overview page
        ScreenshotManager.takeScreenshot(page, "subtotal-label");
        // The subtotal price is displayed in an element with the test ID "subtotal-label"
        return removeDollarSignFromPrice(page.getByTestId("subtotal-label").textContent());
    }

    private double removeDollarSignFromPrice(String priceText) {
        // Remove the dollar sign and parse the price as a double
        return Double.parseDouble(priceText.replace("$", "").trim().replace("Item total: ", ""));
    }


    public void cancelButtonClick() {
        // Click the cancel button
        System.out.println("Clicked on the Cancel button from: " + this.getTitle());
        ScreenshotManager.takeScreenshot(page, "cancel-button-before-click");
        page.getByTestId("cancel").click();
        ScreenshotManager.takeScreenshot(page, "cancel-button-clicked");
    }
}
