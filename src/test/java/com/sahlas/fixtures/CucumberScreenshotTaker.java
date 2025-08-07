package com.sahlas.fixtures;

import com.sahlas.cucumber.stepdefinitions.PlaywrightCucumberFixtures;
import io.cucumber.java.Scenario;

/**
 * CucumberScreenshotTaker is a utility class that implements TakesFinalScreenshot interface.
 * It captures screenshots at the end of a Cucumber scenario.
 * This class is used to ensure that screenshots are taken for debugging purposes.
 */
public class CucumberScreenshotTaker implements TakesFinalScreenshot {
    @Override
    public boolean takeScreenshot(Scenario scenario) {
        // ... (implementation to capture and save the screenshot)
        System.out.println("Screenshot taken: " + scenario.getName());
        if (PlaywrightCucumberFixtures.getPage() != null) {
            ScreenshotManager.takeScreenshot(PlaywrightCucumberFixtures.getPage(), "Final Screenshot: ");
            return true;
        }
        System.out.println("No page available for screenshot.");
        return false;
    }
}
