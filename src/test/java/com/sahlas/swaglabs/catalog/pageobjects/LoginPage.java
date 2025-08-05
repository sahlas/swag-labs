package com.sahlas.swaglabs.catalog.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.sahlas.domain.User;
import com.sahlas.fixtures.ScreenshotManager;
import com.sahlas.fixtures.TakesFinalScreenshot;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

/**
 * Represents the Login Page of the application.
 * Provides methods to interact with the login page, such as logging in, retrieving the page title,
 * handling error messages, and navigating to the home page.
 */
public class LoginPage implements TakesFinalScreenshot {
    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String URL = dotenv.get("URL", "https://www.saucedemo.com/");
    private final Page page;

    /**
     * Constructor for the LoginPage.
     *
     * @param page The Playwright Page object used to interact with the browser.
     */
    public LoginPage(Page page) {
        this.page = page;
    }

    /**
     * Logs in a user using the provided username and password.
     * Captures a screenshot of the login page before clicking the login button.
     *
     * @param user The User object containing the username and password.
     */
    @Step("Login user with username {user.userName} and password {user.password}")
    public void loginUser(User user) {
        page.getByPlaceholder("Username").fill(user.userName());
        page.getByPlaceholder("Password").fill(user.password());
        ScreenshotManager.takeScreenshot(page, "login-page");
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login")).click(null);
    }

    /**
     * Retrieves the title of the current page.
     * Captures a screenshot for verification purposes.
     *
     * @return String The text content of the title element.
     */
    @Step("Get the title of the page")
    public String title() {
        ScreenshotManager.takeScreenshot(page, "title is equal to Product");
        return page.getByTestId("title").textContent();
    }

    /**
     * Retrieves the error message displayed on the login page.
     * Waits for the error message to appear and captures a screenshot.
     *
     * @return String The text content of the error message element.
     */
    @Step("Get error message")
    public String errorMessage() {
        ScreenshotManager.takeScreenshot(page, "error-message");
        // Wait for the error message to appear
        page.getByTestId("error").waitFor();
        return page.getByTestId("error").textContent();
    }

    /**
     * Navigates to the home page of the application.
     * Captures a screenshot after navigation.
     */
    @Step("Open the home page")
    public void openHomePage() {
        page.navigate(URL);
        System.out.println(URL);
        ScreenshotManager.takeScreenshot(page, "home-page");
    }

    public boolean gtetUrl() {
        String currentUrl = page.url();
        return currentUrl.equals(URL);
    }
}