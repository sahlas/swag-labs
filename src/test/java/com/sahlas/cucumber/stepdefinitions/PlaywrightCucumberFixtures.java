package com.sahlas.cucumber.stepdefinitions;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;

import java.util.Arrays;

/**
 * Playwright Cucumber Fixtures for managing Playwright lifecycle in Cucumber tests.
 * This class initializes Playwright, creates a browser context, and manages page instances.
 */
public class PlaywrightCucumberFixtures {

    /**
     * Thread-local instance of Playwright.
     * Ensures each thread has its own Playwright instance.
     * The test ID attribute is set to "data-test" for easier selector targeting.
     */
    private static final ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    /**
     * Thread-local instance of Browser.
     * Launches a Chromium browser in headless mode with specific arguments.
     */
    private static final ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    /**
     * Thread-local instance of BrowserContext.
     * Represents an isolated browser session.
     */
    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();

    /**
     * Thread-local instance of Page.
     * Represents a single tab or page in the browser.
     */
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    /**
     * Tears down the Playwright and Browser instances after all tests are executed.
     * Closes the browser and Playwright instances and removes them from thread-local storage.
     */
    @AfterAll
    public static void tearDown() {
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }

    /**
     * Retrieves the current Page instance.
     *
     * @return The current Page instance.
     */
    public static Page getPage() {
        return page.get();
    }

    /**
     * Retrieves the current BrowserContext instance.
     *
     * @return The current BrowserContext instance.
     */
    public static BrowserContext getBrowserContext() {
        return browserContext.get();
    }

    /**
     * Sets up a new BrowserContext and Page before each test.
     * This method is executed with a high priority (order = 100).
     */
    @Before(order = 100)
    public void setUpBrowserContext() {
        browserContext.set(browser.get().newContext());
        page.set(browserContext.get().newPage());
    }

    /**
     * Closes the current BrowserContext after each test.
     * This method is executed with a high priority (order = 100).
     */
    @After(order = 100)
    public void closeContext() {
        browserContext.get().close();
    }
}