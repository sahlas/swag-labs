package com.sahlas.fixtures;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import io.github.cdimascio.dotenv.Dotenv;

public class BrowserOptionsFactory implements OptionsFactory {

    static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    @Override
    public Options getOptions() {
        return new Options()
                .setBrowserName(dotenv.get("DEFAULT_BROWSER", "chromium"))
                .setHeadless(dotenv.get("HEADLESS", "false").equals("true"))
                .setLaunchOptions(
                        new BrowserType.LaunchOptions()
                                .setArgs(java.util.Arrays.stream(dotenv.get("BROWSER_ARGS", "").split(","))
                                        .filter(arg -> !arg.isBlank())
                                        .toList())
                )
                .setHeadless(dotenv.get("HEADLESS", "false").equals("true"))
                .setTestIdAttribute("data-test");
    }

}
