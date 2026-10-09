package utilities;


import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Guarda y borra la evidencia en disco. Adjuntar al reporte es responsabilidad de {@link ReportAttachments}.
 */
public class FileManager {

    private static final String SCREENSHOTS_PATH = Config.get("evidence.screenshots.dir");
    private static final String PAGE_SOURCE_PATH = Config.get("evidence.pagesource.dir");

    // La evidencia es secundaria: si falla, se registra y NO se lanza excepcion,
    // para no ocultar el error real del escenario.

    public static void saveScreenshot(String screenshotName) {
        Logs.debug("Tomando screenshot: " + screenshotName);

        final var driver = new DriverProvider().get();
        if (driver == null) {
            Logs.warning("No hay driver activo, no se puede tomar el screenshot");
            return;
        }

        final var path = String.format("%s/%s.png", SCREENSHOTS_PATH, screenshotName);

        try {
            final var screenshotFile = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File(path));
        } catch (Exception exception) {
            Logs.error("Error al guardar el screenshot: %s", exception.getLocalizedMessage());
        }
    }

    public static void savePageSource(String fileName) {
        Logs.debug("Guardando estructura de la página: " + fileName);

        final var driver = new DriverProvider().get();
        if (driver == null) {
            Logs.warning("No hay driver activo, no se puede guardar el page source");
            return;
        }

        final var path = String.format("%s/%s.xml", PAGE_SOURCE_PATH, fileName);

        try {
            final var pageSource = driver.getPageSource();
            if (pageSource == null) {
                Logs.warning("El page source llego vacio, no se guarda el archivo");
                return;
            }
            // writeStringToFile crea los directorios padres si no existen
            FileUtils.writeStringToFile(new File(path), Jsoup.parse(pageSource).toString(), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            Logs.error("Error al tomar el page source: %s", exception.getLocalizedMessage());
        }
    }

    public static void deletePreviousEvidence() {

        try {
            Logs.debug("Eliminando screenshots previos");
            FileUtils.deleteDirectory(new File(SCREENSHOTS_PATH));
            FileUtils.deleteDirectory(new File(PAGE_SOURCE_PATH));
        } catch (IOException ioException) {
            Logs.error("Error al eliminar los screenshots previos: %s", ioException.getLocalizedMessage());
            throw new RuntimeException(ioException);
        }

    }
}
