package utilities;

import io.cucumber.java.Scenario;
import org.jsoup.Jsoup;
import org.openqa.selenium.OutputType;

/**
 * Adjunta evidencia (screenshot y page source) al reporte de Cucumber.
 * Igual que {@link FileManager}, sus errores se registran y no se lanzan.
 */
public class ReportAttachments {

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
                    scenario.getName()
            );
        } catch (Exception exception) {
            Logs.error("Error al adjuntar el screenshot: %s", exception.getLocalizedMessage());
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
                    scenario.getName()
            );
        } catch (Exception exception) {
            Logs.error("Error al adjuntar el page source: %s", exception.getLocalizedMessage());
        }
    }
}
