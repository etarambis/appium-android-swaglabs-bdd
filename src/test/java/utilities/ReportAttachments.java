package utilities;

import io.cucumber.java.Scenario;
import org.jsoup.Jsoup;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntry;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adjunta evidencia (screenshot, page source, log del escenario y logcat) al reporte de Cucumber.
 * Igual que {@link FileManager}, sus errores se registran y no se lanzan.
 */
public class ReportAttachments {

    private static final String LOGCAT_TYPE = "logcat";

    public static void attachScreenshot(Scenario scenario) {
        final var driver = new DriverProvider().get();
        if (driver == null) {
            Logs.warning("No hay driver activo, no se adjunta el screenshot");
            return;
        }

        try {
            final var screenshotFile = driver.getScreenshotAs(OutputType.BYTES);

            scenario.attach(
                    screenshotFile,
                    "image/png",
                    "Screenshot"
            );
        } catch (Exception exception) {
            Logs.error("Error al adjuntar el screenshot", exception);
        }
    }

    public static void attachPageSource(Scenario scenario) {
        final var driver = new DriverProvider().get();
        if (driver == null) {
            Logs.warning("No hay driver activo, no se adjunta el page source");
            return;
        }

        try {
            final var pageSource = driver.getPageSource();
            if (pageSource == null) {
                Logs.warning("El page source llego vacio, no se adjunta");
                return;
            }

            scenario.attach(
                    Jsoup.parse(pageSource).toString(),
                    "text/plain",
                    "Page source"
            );
        } catch (Exception exception) {
            Logs.error("Error al adjuntar el page source", exception);
        }
    }

    /**
     * Adjunta las ultimas lineas del logcat del dispositivo (no requiere driver nuevo, usa el de la sesion).
     */
    public static void attachLogcat(Scenario scenario) {
        final var driver = new DriverProvider().get();
        if (driver == null) {
            Logs.warning("No hay driver activo, no se adjunta el logcat");
            return;
        }

        try {
            final var maxLines = Config.getInt("logcat.max.lines");
            final List<LogEntry> entries = driver.manage().logs().get(LOGCAT_TYPE).getAll();
            final var tail = entries.subList(Math.max(0, entries.size() - maxLines), entries.size());

            scenario.attach(
                    tail.stream().map(LogEntry::toString).collect(Collectors.joining("\n")),
                    "text/plain",
                    String.format("Logcat (ultimas %d lineas)", tail.size())
            );
        } catch (Exception exception) {
            Logs.warning("No se pudo obtener el logcat del dispositivo", exception);
        }
    }

    /**
     * Adjunta el log individual del escenario. No depende del driver, asi que sirve incluso si el driver
     * no llego a crearse.
     */
    public static void attachScenarioLog(Scenario scenario, String scenarioKey) {
        final Path logFile = Logs.scenarioLogFile(scenarioKey);

        try {
            if (!Files.exists(logFile)) {
                Logs.warning("No existe el log del escenario: %s", logFile);
                return;
            }

            scenario.attach(
                    Files.readString(logFile, StandardCharsets.UTF_8),
                    "text/plain",
                    "Log del escenario"
            );
        } catch (Exception exception) {
            Logs.error("Error al adjuntar el log del escenario", exception);
        }
    }
}
