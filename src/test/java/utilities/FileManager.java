package utilities;


import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import io.cucumber.java.Scenario;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    private static final String screenshotsPath = "src/test/resources/screenshots/";
    private static final String pageSourcePath  = "src/test/resources/pageStructure/";


    public static void getScreenshot(String screenshotName) {
        Logs.debug("Tomando screenshot: " + screenshotName);

        final var screenshotFile = ((TakesScreenshot) new DriverProvider().get())
                .getScreenshotAs(OutputType.FILE);

        final var path = String.format("%s/%s.png", screenshotsPath, screenshotName);

        try {
            FileUtils.copyFile(screenshotFile, new File(path));
        } catch (IOException ioExeption) {
            Logs.error("Error al guardar el screenshot: %s", ioExeption.getLocalizedMessage());
            throw new RuntimeException(ioExeption);
        }

    }

    public static void getPageSource(String fileName) {
        Logs.debug("Guardando estructura de la página: " + fileName);
        final var path = String.format("%s/%s.xml", pageSourcePath, fileName);

        try {
            final var file = new File(path);
            Logs.debug("Creando los directorios padres si es que no existen ");
            if (file.getParentFile().mkdir()) {
                final var fileWriter = new FileWriter(file);
                final var pageSource = new DriverProvider().get().getPageSource();

                if (pageSource != null) {
                    fileWriter.write(Jsoup.parse(pageSource).toString());
                }
                fileWriter.close();
            }

        } catch (IOException ioException) {

            Logs.error("Error al tomar el page source: %s", ioException.getLocalizedMessage());
            throw new RuntimeException(ioException);

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
        final var screenshotFile = ((TakesScreenshot) new DriverProvider().get())
                .getScreenshotAs(OutputType.BYTES);

        scenario.attach(
                screenshotFile,
                "image/png",
                scenario.getName()
        );
    }

    public static void attachPageSource(Scenario scenario) {
        final var pageSource = new DriverProvider().get().getPageSource();
        final var parsedPageSource = Jsoup.parse(pageSource).toString();

        scenario.attach(
                parsedPageSource,
                "text/plain",
                scenario.getName()
        );
    }
}
