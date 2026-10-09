package utilities;


import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.OutputType;
import io.cucumber.java.Scenario;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileManager {

    private static final String screenshotsPath = Config.get("evidence.screenshots.dir");
    private static final String pageSourcePath  = Config.get("evidence.pagesource.dir");


    // La evidencia es secundaria: si falla, se registra y NO se lanza excepcion,
    // para no ocultar el error real del escenario.

    public static void getScreenshot(String screenshotName) {
        Logs.debug("Tomando screenshot: " + screenshotName);

        final var driver = new DriverProvider().get();
        if (driver == null) {
            Logs.warning("No hay driver activo, no se puede tomar el screenshot");
            return;
        }

        final var path = String.format("%s/%s.png", screenshotsPath, screenshotName);

        try {
            final var screenshotFile = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File(path));
        } catch (Exception exception) {
            Logs.error("Error al guardar el screenshot: %s", exception.getLocalizedMessage());
        }
    }

    public static void getPageSource(String fileName) {
        Logs.debug("Guardando estructura de la página: " + fileName);

        final var driver = new DriverProvider().get();
        if (driver == null) {
            Logs.warning("No hay driver activo, no se puede guardar el page source");
            return;
        }

        final var path = String.format("%s/%s.xml", pageSourcePath, fileName);

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
            FileUtils.deleteDirectory(new File(screenshotsPath));
            FileUtils.deleteDirectory(new File(pageSourcePath));
        } catch (IOException ioException) {
            Logs.error("Error al eliminar los screenshots previos: %s", ioException.getLocalizedMessage());
            throw new RuntimeException(ioException);
        }

    }

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
