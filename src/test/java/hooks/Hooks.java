package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import utilities.DriverManager;
import utilities.Logs;
import utilities.FileManager;
import utilities.ReportAttachments;

import java.time.Duration;

public class Hooks {

    private static final DriverManager driverManager = new DriverManager();
    private static final ThreadLocal<Long> SCENARIO_START = new ThreadLocal<>();

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
        // Primero se asocia el escenario al log, para que todo lo que sigue quede en su log individual
        Logs.bindScenario(scenarioKey(scenario));
        SCENARIO_START.set(System.nanoTime());

        Logs.info("INICIO escenario: %s (linea %d) tags=%s", scenario.getName(), scenario.getLine(), scenario.getSourceTagNames());
        driverManager.buildDriver();
    }

    @After
    public static void after(Scenario scenario) {
        final var status = scenario.getStatus();
        final var failed = status == Status.FAILED || status == Status.SKIPPED;
        final var evidenceName = scenarioKey(scenario);

        Logs.info("FIN escenario: %s, estado: %s, duracion: %d ms",
                scenario.getName(), status, elapsedMillis());
        try {
            if (failed) {
                // FileManager y ReportAttachments ignoran (y registran) sus errores y la ausencia de driver
                FileManager.saveScreenshot(evidenceName);
                FileManager.savePageSource(evidenceName);
                ReportAttachments.attachScreenshot(scenario);
                ReportAttachments.attachPageSource(scenario);
                ReportAttachments.attachLogcat(scenario);
            }
        } finally {
            driverManager.killDriver();
            // El log del escenario se adjunta al final para que incluya tambien el cierre del driver
            if (failed) {
                ReportAttachments.attachScenarioLog(scenario, evidenceName);
            }
            SCENARIO_START.remove();
            Logs.unbindScenario();
        }
    }

    private static String scenarioKey(Scenario scenario) {
        return FileManager.safeName(scenario.getName(), scenario.getLine());
    }

    private static long elapsedMillis() {
        final var start = SCENARIO_START.get();
        return start == null ? 0 : Duration.ofNanos(System.nanoTime() - start).toMillis();
    }
}
