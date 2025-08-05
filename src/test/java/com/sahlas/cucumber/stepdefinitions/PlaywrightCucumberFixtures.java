package com.sahlas.cucumber.stepdefinitions;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;

import java.util.Arrays;

//BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
//                launchOptions.setChannel("chrome"); // Specify the Chrome channel
//                launchOptions.setHeadless(false); // Optional: Set to true for headless mode
//
//Browser browser = playwright.chromium().launch(launchOptions);
//Page page = browser.newPage();
//                page.navigate("https://www.google.com");
//                System.out.println(page.title());
//        browser.close();


public class PlaywrightCucumberFixtures {
    private static final ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    private static final ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
//                            .setChannel("chrome")
                            .setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();

    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    @AfterAll
    public static void tearDown() {
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }

    public static Page getPage() {
        return page.get();
    }

    public static BrowserContext getBrowserContext() {
        return browserContext.get();
    }

    @Before(order = 100)
    public void setUpBrowserContext() {
        browserContext.set(browser.get().newContext());
        page.set(browserContext.get().newPage());
    }

    @After(order = 100)
    public void closeContext() {
        browserContext.get().close();
    }

}

