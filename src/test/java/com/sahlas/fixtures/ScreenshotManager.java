package com.sahlas.fixtures;


import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;

public class ScreenshotManager {
    public static void takeScreenshot(Page page, String name) {
        var screenshot = page.screenshot(
                new Page.ScreenshotOptions()
                        .setPath(Paths.get("target/screenshots/" + name + ".png"))
                        .setFullPage(true)
        );

        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
    }
}
