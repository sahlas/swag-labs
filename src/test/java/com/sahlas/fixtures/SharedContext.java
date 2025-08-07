package com.sahlas.fixtures;

/**
 * SharedContext is a utility class that holds shared context for Cucumber tests.
 * It provides access to a CucumberScreenshotTaker instance, which is used to take screenshots during test execution.
 * This class can be extended or modified to include additional shared resources as needed.
 */
public class SharedContext {
    private final CucumberScreenshotTaker screenshotTaker;

    public SharedContext(CucumberScreenshotTaker screenshotTaker) {
        this.screenshotTaker = screenshotTaker;
    }

    public SharedContext() {
        this.screenshotTaker = new CucumberScreenshotTaker();
    }

    public CucumberScreenshotTaker getScreenshotTaker() {
        return screenshotTaker;
    }
}
