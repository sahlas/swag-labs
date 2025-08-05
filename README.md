# swag-labs

A sample **Playwright Java Test** project managed with Maven.

This project demonstrates how to set up and run Playwright tests using Java and Maven.  
It includes a basic test suite for the Swag Labs application, a web app used for testing purposes.

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
