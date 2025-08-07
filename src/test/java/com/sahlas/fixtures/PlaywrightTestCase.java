package com.sahlas.fixtures;

import com.microsoft.playwright.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Paths;

// @Execution(ExecutionMode.CONCURRENT)
// @UsePlaywright(PlaywrightTestCase.MyOptions.class)
public abstract class PlaywrightTestCase {

    static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    static final String DEFAULT_BROWSER = dotenv.get("DEFAULT_BROWSER", "chromium");

    static final String URL = dotenv.get("URL", "https://example.com");
    static final String HEADLESS_STRING = dotenv.get("HEADLESS", "false");
    static final boolean HEADLESS = HEADLESS_STRING.equalsIgnoreCase("true");
    static final String BROWSER_ARGS_STRING = dotenv.get("BROWSER_ARGS", "");
    static final String[] BROWSER_ARGS = BROWSER_ARGS_STRING.isBlank() ? new String[0] : BROWSER_ARGS_STRING.split(",");
    protected static ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );
    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() -> {
                if (DEFAULT_BROWSER.equalsIgnoreCase("firefox")) {
                    return playwright.get().firefox().launch(
                            new BrowserType.LaunchOptions()
                                    .setHeadless(HEADLESS)
                                    .setArgs(java.util.Arrays.stream(BROWSER_ARGS)
                                            .filter(arg -> !arg.isBlank())
                                            .toList())
                    );
                } else if (DEFAULT_BROWSER.equalsIgnoreCase("webkit")) {
                    return playwright.get().webkit().launch(
                            new BrowserType.LaunchOptions()
                                    .setHeadless(HEADLESS)
                                    .setArgs(java.util.Arrays.stream(BROWSER_ARGS)
                                            .filter(arg -> !arg.isBlank())
                                            .toList())
                    );
                } else if (DEFAULT_BROWSER.equalsIgnoreCase("chromium")) {
                    return playwright.get().chromium().launch(
                            new BrowserType.LaunchOptions()
                                    .setHeadless(HEADLESS)
                                    .setArgs(java.util.Arrays.stream(BROWSER_ARGS)
                                            .filter(arg -> !arg.isBlank())
                                            .toList())
                    );
                } else {
                    return playwright.get().chromium().launch(
                            new BrowserType.LaunchOptions()
                                    .setHeadless(dotenv.get("HEADLESS", "false").equals("true"))
                                    .setArgs(java.util.Arrays.stream(dotenv.get("BROWSER_ARGS", "").split(","))
                                            .filter(arg -> !arg.isBlank())
                                            .toList())
                    );
                }
            }
    );
    static final int TIMEOUT = Integer.parseInt(dotenv.get("TIMEOUT", "30000"));
    static final int RETRY_COUNT = Integer.parseInt(dotenv.get("RETRY_COUNT", "3"));
    static final int RETRY_DELAY = Integer.parseInt(dotenv.get("RETRY_DELAY", "1000"));
    static final int NAVIGATION_TIMEOUT = Integer.parseInt(dotenv.get("NAVIGATION_TIMEOUT", "30000"));

    protected BrowserContext browserContext;

    protected Page page;

    @AfterAll
    static void tearDown() {
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }

    // Helper method to convert a string to snake_case
    @SuppressWarnings("unused")
    private static String toSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .replaceAll("[^a-zA-Z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "")
                .toLowerCase();
    }

    // Helper method to convert a string to camelCase
    private static String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (char c : input.toCharArray()) {
            if (c == '-' || c == '_' || c == ' ') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    @BeforeEach
    void setUpBrowserContext(TestInfo testInfo) {
        browserContext = browser.get().newContext();
        browserContext.setDefaultTimeout(TIMEOUT);
        browserContext.setDefaultNavigationTimeout(
                NAVIGATION_TIMEOUT
        );

        page = browserContext.newPage();
        if (dotenv.get("RECORD_TRACE", "false").equalsIgnoreCase("true")) {
            browserContext.tracing().start(
                    new Tracing.StartOptions()
                            .setScreenshots(true)
                            .setSnapshots(true)
                            .setSources(true)
            );
        }
    }

    @AfterEach
    void closeBrowserContext(TestInfo testInfo) {
        if (dotenv.get("RECORD_TRACE", "false").equalsIgnoreCase("true")) {
            String testName = toCamelCase(testInfo.getDisplayName().replace(" ", "-"));
            System.out.println("Recording trace for test: " + testName);
            browserContext.tracing().stop(
                    new Tracing.StopOptions()
                            .setPath(Paths.get("target/traces/trace-" + testName + ".zip"))
            );
        }
        ScreenshotManager.takeScreenshot(page, "End of " + testInfo.getDisplayName());
        browserContext.close();
    }
}
