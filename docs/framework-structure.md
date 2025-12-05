## Falcon Web Automation Framework

### 1. Overview
The Falcon framework is a Maven + TestNG starter kit that follows the Page Object Model (POM) design pattern and supports both desktop web (dWeb) and mobile web (mWeb) executions through a single codebase. All configuration knobs reside in `src/test/resources/property.config`, so no code changes are required to target a different browser, device profile, or Selenium Grid.

### 2. Modules & Responsibilities
| Module | Package | Responsibility |
|--------|---------|----------------|
| Configuration | `com.liftofftech.falcon.core.config` | Loads typed settings (browser, platform, timeouts, mobile overrides, module URLs) from property files or `-D` overrides. |
| Driver | `com.liftofftech.falcon.core.driver` | Thread-safe driver lifecycle, WebDriverFactory that applies desktop/mobile capabilities, optional Grid execution. Includes `ModuleType` enum for type-safe module references. |
| Navigation | `com.liftofftech.falcon.core.navigation` | `ModuleNavigator` utility for constructing module-specific URLs with subdomain support and environment awareness. |
| Base | `com.liftofftech.falcon.core.base` | Common test/page super-classes that wire waits, navigation, and teardown hooks with Allure attachments. Includes module navigation helpers. |
| Reporting | `com.liftofftech.falcon.core.reporting` | Allure helpers for screenshots and page sources. |
| Utilities | `com.liftofftech.falcon.core.utils` | Shared helpers such as `JsonUtils` for test data loading. |
| Pages | `com.liftofftech.falcon.pages.{module}` | Page Object classes organized by module (e.g., `pages.image.AiImageGenerator`, `pages.common.LoginPage`). |
| Tests | `com.liftofftech.falcon.tests.{module}` | TestNG test classes organized by module (e.g., `tests.image.ImageGeneratorTests`, `tests.common.LoginTests`). |
| Resources | `src/test/resources` | Runtime config files (property.config, property.stage.config, property.prod.config), TestNG suite files, JSON test data, Allure output directory (generated). |

### 3. Configuration-First Design
Environment-specific config files govern execution: `property.config` (local), `property.stage.config` (stage), `property.prod.config` (production). Each property can be overridden via `-Dkey=value` when invoking Maven.

| Key | Description |
|-----|-------------|
| `environment` | Current environment: `local`, `stage`, or `prod`. |
| `base.url` | Application under test root URL. |
| `base.domain` | Base domain for module URLs (e.g., `galaxy.ai` for prod, `rework.vip` for stage). |
| `browser` | `chrome` or `edge`. |
| `platform` | `dweb` (desktop) or `mweb` (mobile emulation). |
| `remote` | Toggle Selenium Grid usage. |
| `grid.url` | Hub URL when `remote=true`. |
| `headless` | Enables Chromium headless mode. |
| `implicit.wait`, `page.load.timeout` | Timeouts in seconds. |
| `mobile.*` | Device metrics and user-agent used for mWeb runs. |
| `module.{name}.subdomain` | Subdomain for module (e.g., `module.image.subdomain=image`). |
| `module.{name}.base.path` | Base path for module (e.g., `module.image.base.path=/ai-image-generator`). |

### 4. Execution Matrix

#### Environment-Based Execution
| Scenario | Command | Notes |
|----------|---------|-------|
| Local (default) | `mvn clean test` | Uses `property.config`, runs `testng.xml`. |
| Stage environment | `mvn clean test -Pstage` | Uses `property.stage.config`, runs `testng-stage.xml`. |
| Production environment | `mvn clean test -Pprod` | Uses `property.prod.config`, runs `testng-prod.xml`. |
| Custom config file | `mvn clean test -Dconfig.file=property.custom.config` | Override config file. |

#### Module-Based Execution
| Scenario | Command | Notes |
|----------|---------|-------|
| Image module | `mvn clean test -Pimage` | Runs `testng-image.xml` with all image module tests. |
| Video module | `mvn clean test -Pvideo` | Runs `testng-video.xml` with all video module tests. |
| Audio module | `mvn clean test -Paudio` | Runs `testng-audio.xml` with all audio module tests. |
| All modules | `mvn clean test -Pall` | Runs `testng-all.xml` with all module tests. |

#### Combined Execution (Module + Environment)
| Scenario | Command | Notes |
|----------|---------|-------|
| Image on Stage | `mvn clean test -Pstage -Pimage` | Runs image tests against stage environment. |
| Image on Prod | `mvn clean test -Pprod -Pimage` | Runs image tests against production environment. |
| All modules on Stage | `mvn clean test -Pstage -Pall` | Runs all module tests against stage. |

#### Other Execution Options
| Scenario | Command | Notes |
|----------|---------|-------|
| Desktop Chrome (default) | `mvn clean test` | Uses local ChromeDriver via WebDriverManager. |
| Headless desktop | `mvn clean test -Dheadless=true` | Keeps screenshots enabled. |
| Mobile web emulation | `mvn clean test -Dplatform=mweb -Dmobile.device.name=\"Pixel 7\"` | Applies responsive viewport + UA overrides. |
| Remote Grid | `mvn clean test -Dremote=true -Dgrid.url=http://grid:4444/wd/hub` | Sends desired capabilities to remote hub. |

### 5. Reporting
Allure results land in `target/allure-results`.
- Serve report: `allure serve target/allure-results`
- Generate static report: `allure generate target/allure-results --clean`

Each failed test automatically attaches a screenshot and HTML source via `AllureAttachments`.

### 6. Test Data Strategy
Structured data is stored under `src/test/resources/testdata`. JSON is deserialized into typed records (see `UserCredential`). This keeps the framework extensible for additional personas, locales, or environments without duplicating code.

### 7. Module Organization

The framework is organized by modules to avoid messy flat directories:

**Page Objects Structure:**
- `com.liftofftech.falcon.pages.image/` - Image module page objects (e.g., `AiImageGenerator`)
- `com.liftofftech.falcon.pages.video/` - Video module page objects
- `com.liftofftech.falcon.pages.audio/` - Audio module page objects
- `com.liftofftech.falcon.pages.common/` - Shared page objects (e.g., `LoginPage`, `SecureAreaPage`)

**Test Classes Structure:**
- `com.liftofftech.falcon.tests.image/` - Image module tests (e.g., `ImageGeneratorTests`)
- `com.liftofftech.falcon.tests.video/` - Video module tests
- `com.liftofftech.falcon.tests.audio/` - Audio module tests
- `com.liftofftech.falcon.tests.common/` - Shared tests (e.g., `LoginTests`)

**Module URL Structure:**
- Production: `https://{subdomain}.galaxy.ai/{base-path}`
- Stage: `https://{subdomain}.rework.vip/{base-path}`
- Example: Image module → Prod: `https://image.galaxy.ai/ai-image-generator`, Stage: `https://image.rework.vip/ai-image-generator`

### 8. Extending the Framework

#### Adding a New Module
1. **Add ModuleType enum value:** Add new enum (e.g., `TEXT`) to `com.liftofftech.falcon.core.driver.ModuleType`
2. **Add config entries:** Add to all config files:
   ```
   module.text.subdomain=text
   module.text.base.path=/ai-text-generator
   ```
3. **Create page objects:** Create classes in `com.liftofftech.falcon.pages.text/` extending `BasePage`
4. **Create tests:** Create test classes in `com.liftofftech.falcon.tests.text/` extending `BaseTest`
5. **Create TestNG suite:** Add `testng-text.xml` if needed
6. **No code changes needed:** ModuleNavigator and FrameworkConfig work automatically via config

#### Adding a New Page Object
Create a new class under the appropriate module package (e.g., `com.liftofftech.falcon.pages.image`) that extends `BasePage` and expose user flows annotated with `@Step`. Use `navigateToModule(ModuleType, Map)` helper for navigation.

#### Creating a Test
Extend `BaseTest`, inject page objects, and leverage existing data providers or JSON utilities. Place tests in the appropriate module package (e.g., `com.liftofftech.falcon.tests.image`).

#### Adding a New Environment
1. Duplicate `property.config` into `property.{env}.config`
2. Update `base.domain` and other environment-specific settings
3. Create `testng-{env}.xml` suite file
4. Add Maven profile in `pom.xml` if needed
5. Run: `mvn test -Dconfig.file=property.{env}.config -Dsurefire.suiteXmlFiles=testng-{env}.xml`

This document should be your living reference as you expand Falcon with new modules, CI pipelines, or cloud-device integrations.

