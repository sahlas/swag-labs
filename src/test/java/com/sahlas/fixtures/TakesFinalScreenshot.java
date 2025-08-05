package com.sahlas.fixtures;

import com.sahlas.cucumber.stepdefinitions.PlaywrightCucumberFixtures;
import org.junit.jupiter.api.AfterEach;

public interface TakesFinalScreenshot {

    @AfterEach
    default void takeFinalScreenshot() {
        if (PlaywrightCucumberFixtures.getPage() != null) {
            ScreenshotManager.takeScreenshot(PlaywrightCucumberFixtures.getPage(), "Final Screenshot: ");
        }
    }
}
