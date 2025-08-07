package com.sahlas.fixtures;

import io.cucumber.java.Scenario;

/**
 * TakesFinalScreenshot is an interface that defines a method for taking screenshots at the end of a Cucumber scenario.
 * Implementing classes should provide the logic to capture and save screenshots for debugging purposes.
 */
public interface TakesFinalScreenshot {
    /**
     * Takes a screenshot at the end of a Cucumber scenario.
     *
     * @param scenario The Cucumber scenario for which the screenshot is taken.
     * @return true if the screenshot was successfully taken, false otherwise.
     */
    boolean takeScreenshot(Scenario scenario);
}

