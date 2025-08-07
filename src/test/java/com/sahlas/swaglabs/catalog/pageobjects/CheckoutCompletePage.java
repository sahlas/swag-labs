package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Page;
import com.sahlas.fixtures.ScreenshotManager;
import com.sahlas.fixtures.TakesFinalScreenshot;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

public class CheckoutCompletePage {
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String CHECKOUT_COMPLETE_CONFIRMATION_MESSAGE = dotenv.get("CHECKOUT_COMPLETE_CONFIRMATION_MESSAGE", "Checkout: Complete!");
    private static final String CHECKOUT_COMPLETE_PAGE_URL = dotenv.get("CHECKOUT_COMPLETE_PAGE_URL", "https://www.saucedemo.com/checkout-complete.html");
    private static final String CHECKOUT_COMPLETE_PAGE_TITLE = dotenv.get("CHECKOUT_COMPLETE_PAGE_TITLE", "Checkout: Complete!");
    private final Page page;

    public CheckoutCompletePage(Page page) {
        this.page = page;
    }

    /**
     * Get order confirmation message from the checkout complete page.
     */
    @Step("Get Order Confirmation Message")
    public boolean getOrderConfirmationMessage() {
        // Get the order confirmation message from the checkout complete page
        String orderConfirmationMessage = page.getByTestId("complete-header").textContent();

        boolean isMessageCorrect = orderConfirmationMessage.equals(CHECKOUT_COMPLETE_CONFIRMATION_MESSAGE);
        if (isMessageCorrect) {
            System.out.println("Order confirmation message is correct: " + orderConfirmationMessage);
        } else {
            System.out.println("Order confirmation message is incorrect: " + orderConfirmationMessage);
            ScreenshotManager.takeScreenshot(page, "order-confirmation-message-" + orderConfirmationMessage);
        }
        System.out.println("Order confirmation message: expected - " + CHECKOUT_COMPLETE_CONFIRMATION_MESSAGE + "\n actual - " + orderConfirmationMessage);

        return isMessageCorrect;
    }

    /**
     * Check the title of the checkout complete page.
     *
     * @return true if it matches the expected title, false otherwise.
     */
    @Step("Check the title of the checkout complete page")
    public boolean checkPageTitle() {
        // Extract the title text from the page element
        String title = page.getByTestId("title").textContent();
        boolean isTitleCorrect = title.equals(CHECKOUT_COMPLETE_PAGE_TITLE);
        if (isTitleCorrect) {
            System.out.println("Checkout complete page title is correct: " + title);
        } else {
            System.out.println("Checkout complete page title is incorrect: " + title);
            ScreenshotManager.takeScreenshot(page, "checkout-complete-title" + title);
        }
        System.out.println("Checkout complete page title: expected - " + CHECKOUT_COMPLETE_PAGE_TITLE + "\n actual - " + title);
        return isTitleCorrect;
    }

    public boolean checkPageUrl() {
        // Get the current URL of the page
        String currentUrl = page.url();
        boolean isUrlCorrect = currentUrl.equals(CHECKOUT_COMPLETE_PAGE_URL);
        if (isUrlCorrect) {
            System.out.println("Checkout complete page URL is correct: " + currentUrl);
        } else {
            System.out.println("Checkout complete page URL is incorrect: " + currentUrl);
            ScreenshotManager.takeScreenshot(page, "checkout-complete-url-" + currentUrl);
        }
        System.out.println("Checkout complete page URL: expected - " + CHECKOUT_COMPLETE_PAGE_URL + "\n actual - " + currentUrl);
        return isUrlCorrect;
    }
}
