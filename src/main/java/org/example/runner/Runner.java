package org.example.runner;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
        @ConfigurationParameter(
                key = PLUGIN_PROPERTY_NAME,
                value = "pretty, html:target/cucumber-reports/cucumber.html, " +
                        "junit:target/cucumber-reports/cucumber.xml, " +
                        "json:target/cucumber-reports/cucumber.json"
        ),
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.example"),
        @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@validado and not @failed"),
        @ConfigurationParameter(key = SNIPPET_TYPE_PROPERTY_NAME, value = "camelcase"),
        @ConfigurationParameter(key = ANSI_COLORS_DISABLED_PROPERTY_NAME, value = "true"),
        @ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true"),
        @ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long"),
})
public class Runner {
}