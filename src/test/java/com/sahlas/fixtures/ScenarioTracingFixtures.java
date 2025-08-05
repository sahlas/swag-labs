package com.sahlas.fixtures;


import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import com.sahlas.cucumber.stepdefinitions.PlaywrightCucumberFixtures;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.nio.file.Paths;

public class ScenarioTracingFixtures {


    @Before
    public void setupTracing() {
        PlaywrightCucumberFixtures.getBrowserContext().tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @After
    //  npx playwright show-trace ./target/traces/trace-:-the-one-where-sally-logs-in-to-the-catalog.zip
    public void recordTraces(Scenario scenario) {
        BrowserContext context = PlaywrightCucumberFixtures.getBrowserContext();
        String traceName = scenario.getName().replace(" ", "-").toLowerCase();
        context.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/traces/trace-" + traceName + ".zip"))
        );
    }

}