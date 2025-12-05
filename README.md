## Falcon Web Automation Framework

Maven-based Selenium + TestNG framework that implements Page Object Model (POM) for both desktop web (dWeb) and mobile web (mWeb) executions with Allure reporting.

### Prerequisites
- Java 17+
- Maven 3.9+
- Browsers/Drivers (managed automatically through WebDriverManager)
- Allure Commandline (optional, for report viewing)

### Key Features
- **Dual platform support:** switch between dWeb and mWeb via `property.config`.
- **Config-driven:** override any key with `-D` flags (e.g., `-Dplatform=mweb`).
- **Allure-ready:** screenshots + page source attached on failures.
- **Data-driven tests:** JSON-backed TestNG data providers.

### Project Structure
See `docs/framework-structure.md` for detailed module descriptions.

### Running Tests
```bash
# Default desktop Chrome
mvn clean test

# Mobile web emulation
mvn clean test -Dplatform=mweb -Dmobile.device.name="Pixel 7"

# Selenium Grid
mvn clean test -Dremote=true -Dgrid.url=http://localhost:4444/wd/hub
```

### Allure Report
```bash
allure serve target/allure-results
```

### Configuration
Edit `src/test/resources/property.config` or pass `-Dconfig.file=custom.config` to point to another environment file.

