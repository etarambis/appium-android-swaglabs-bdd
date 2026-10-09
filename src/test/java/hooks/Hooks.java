package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import utilities.DriverManager;
import utilities.Logs;
import utilities.FileManager;

public class Hooks {

    private static final DriverManager driverManager = new DriverManager();

    @BeforeAll
    public static void beforeAll() {
        Logs.info("beforeAll");
        FileManager.deletePreviousEvidence();
    }

    @AfterAll
    public static void afterAll() {
        Logs.info("afterAll");
    }

    @Before
    public static void before(Scenario scenario) {
        Logs.info("before: %s", scenario.getName());
        driverManager.buildDriver();
    }

    @After
    public static void after(Scenario scenario) {
        Logs.info("after: %s, status: %s", scenario.getName(), scenario.getStatus());
        try {
            switch (scenario.getStatus()) {
                case SKIPPED, FAILED -> {
                    // FileManager ignora (y registra) sus propios errores y la ausencia de driver
                    FileManager.getScreenshot(scenario.getName());
                    FileManager.getPageSource(scenario.getName());
                    FileManager.attachScreenshot(scenario);
                    FileManager.attachPageSource(scenario);
                }
                default -> { }
            }
        } finally {
            driverManager.killDriver();
        }
    }
}