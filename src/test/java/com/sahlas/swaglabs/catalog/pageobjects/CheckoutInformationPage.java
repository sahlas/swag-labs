package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Page;
import com.sahlas.fixtures.ScreenshotManager;
import com.sahlas.fixtures.TakesFinalScreenshot;
import io.cucumber.datatable.DataTable;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

import java.util.List;
import java.util.Map;

/**
 * Represents the Checkout Information Page in the application.
 * Provides methods to interact with and verify elements on the page, such as the title,
 * checkout button, and personal information form.
 */
public class CheckoutInformationPage {
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String CHECK_OUT_INFORMATION_PAGE_TITLE = dotenv.get("CHECKOUT_INFORMATION_PAGE_TITLE", "Checkout: Your Information");
    private static final String CHECKOUT_INFORMATION_PAGE_URL = dotenv.get("CHECKOUT_INFORMATION_PAGE_URL", "https://www.saucedemo.com/");
    private final Page page;

    /**
     * Constructor for the CheckoutInformationPage.
     *
     * @param page The Playwright Page object used to interact with the browser.
     */
    public CheckoutInformationPage(Page page) {
        this.page = page;
    }

    /**
     * Verifies if the title of the checkout information page is correct.
     *
     * @return boolean true if the title matches "Checkout: Your Information", false otherwise.
     */

    @Step("Check the title of the checkout information page")
    public boolean checkTitle() {
        // Get the title text from the page element
        String title = page.getByTestId("title").textContent();
        boolean isTitleCorrect = title.equals(CHECK_OUT_INFORMATION_PAGE_TITLE);
        if (isTitleCorrect) {
            System.out.println("Checkout information page title is correct: " + title);
        } else {
            System.out.println("Checkout information page title is incorrect: " + title);
            ScreenshotManager.takeScreenshot(page, "checkout-information-title-" + title);
        }
        return isTitleCorrect;
    }

    /**
     * Gets the title of the checkout information page.
     *
     * @return The title text of the page.
     */
    @Step("Get the title of the checkout information page")
    public String getTitle() {
        ScreenshotManager.takeScreenshot(page, "title is equal to Checkout: Your Information");
        return page.getByTestId("title").textContent();
    }

    /**
     * Clicks a checkout button identified by its test ID.
     *
     * @param buttonName The test ID of the button to click.
     */
    @Step("Click the checkout button")
    public void buttonClick(String buttonName) {
        // Click the checkout button with the specified name
        System.out.println("Clicked on the " + buttonName + " button");
        page.getByTestId(buttonName).click();
    }

    /**
     * Fills in the personal information form on the checkout page.
     *
     * @param personalInfoTable A DataTable containing personal information with keys:
     *                          "first_name", "last_name", and "postal_code".
     * @throws IllegalArgumentException if the DataTable is empty.
     */
    @Step("Fill in personal information")
    public void fillInPersonalInformation(DataTable personalInfoTable) {
        // Get the personal information from the DataTable
        List<Map<String, String>> personalInfo = personalInfoTable.asMaps(String.class, String.class);
        if (personalInfo.isEmpty()) {
            throw new IllegalArgumentException("Personal information table is empty");
        }
        Map<String, String> info = personalInfo.get(0);

        // Fill in the personal information fields
        page.getByPlaceholder("First Name").fill(info.get("first_name"));
        page.getByPlaceholder("Last Name").fill(info.get("last_name"));
        page.getByPlaceholder("Zip/Postal Code").fill(info.get("postal_code"));
    }

    /**
     * Checks if the current URL of the page matches the expected checkout step one URL.
     *
     * @return boolean true if the current URL is "checkout-step-one.html", false otherwise.
     */
    @Step("Check the current URL of the checkout information page")
    public boolean checkPageUrl() {
        String currentUrl = page.url();
        boolean isUrlCorrect = currentUrl.equals(CHECKOUT_INFORMATION_PAGE_URL);
        if (isUrlCorrect) {
            System.out.println("Current URL is correct: " + currentUrl);
        } else {
            ScreenshotManager.takeScreenshot(page, "Current URL is incorrect");
            System.out.println("Current URL is incorrect: " + currentUrl);
        }
        return isUrlCorrect;
    }

    /**
     * Clicks the cancel button on the checkout information page.
     * This action navigates back to the inventory page.
     */
    @Step("Click the cancel button on the checkout information page")
    public void cancelButtonClick() {
        // Find and click the cancel button
        System.out.println("Clicked on the Cancel button from: " + this.getTitle());
        ScreenshotManager.takeScreenshot(page, "cancel-button-before-click");
        page.getByTestId("cancel").click();
        ScreenshotManager.takeScreenshot(page, "cancel-button-clicked");
    }
}