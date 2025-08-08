# swag-labs

A sample **Playwright Java Test** project managed with Maven.

This project demonstrates how to set up and run Playwright tests using Java and Maven.  
It includes a basic test suite for the Sauce Labs 'Swag Labs' site, an ecommerce web app used for testing purposes. For a comprehensive list of practice sites check out [the list](https://www.linkedin.com/pulse/best-test-demo-sites-practicing-software-automation-mark-nicoll-bjsme/).

## Features

- Automated UI testing with Playwright for Java
- Easy build and test execution with Maven
- Example test suite for Swag Labs
- Allure reporting integration (optional)
- GitHub Actions workflow for CI

## Prerequisites

- Java 17 or higher
- Maven 3.8.x or higher
- Node.js (for Playwright setup)

## Setup

1. Clone the repository:
   ```sh
   git clone git@github.com:sahlas/swag-labs.git
   cd swag-labs
    ```

## Run Tests and Generate Reports

To run the tests and generate reports, execute:

```sh
mvn clean verify
```

To view the Allure report, you can run:

```sh
mvn io.qameta.allure:allure-maven:serve 
```

## Project Structure

```plaintext
src/test/java — Test source code
.github/workflows — CI/CD workflows
pom.xml — Maven configuration file
README.md — Project documentation
```
## Continuous Integration (CI) with GitHub Actions

This project includes a GitHub Actions workflow for automated testing and reporting. The workflow file is located at `.github/workflows/swag-site-tests-with-allure.yml`.

### Workflow Features

- **Runs on every push and pull request** to the `main` branch (and all PR branches).
- **Sets up Java 17 and Maven 3.9.9** for the build environment.
- **Caches Maven dependencies** to speed up builds.
- **Executes all tests** using `mvn clean verify`.
- **Generates Allure test reports** for enhanced test result visualization.
- **Publishes Allure reports** to the `gh-pages` branch for easy access.
  - **Find reports at**: [swag-labs-java-playwright-cucumber/reports](https://sahlas.github.io/swag-labs-java-playwright-cucumber)
- **Archives trace files** from test runs for debugging.

### Manual Trigger

You can also manually trigger the workflow using the "Run workflow" button in the GitHub Actions tab.

### Artifacts and Reports

- **Allure results** are generated in the `target/allure-results` directory.
- **Test reports** are published to GitHub Pages via the `gh-pages` branch.
- **Trace files** are archived as workflow artifacts for download.

For more details, see the workflow file at `.github/workflows/swag-site-tests-with-allure.yml`.