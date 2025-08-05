package com.sahlas.cucumber.runners;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("/features")
//@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.sahlas.stepdefinitions")

//@ConfigurationParameter(
//        key=PLUGIN_PROPERTY_NAME,
//        value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm," +
//                // "pretty," +
//                "html:target/cucumber-reports/cucumber.html,"
//)
public class CucumberTests {
    // This class remains empty, it is used only as a holder for the above annotations
}