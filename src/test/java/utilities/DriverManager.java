package utilities;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {
    private final boolean runServer = System.getenv("JOB_NAME") != null;

    public void buildDriver(){
        if(runServer){
            buildRemoteDriver();
        } else{
            buildLocalDriver();
        }
    }

    public void killDriver(){
        Logs.debug("Matando al driver");
        try {
            final var driver = new DriverProvider().get();
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception exception) {
            Logs.error("No se pudo cerrar el driver: %s", exception.getMessage());
        } finally {
            new DriverProvider().remove();
        }
    }

    private void buildLocalDriver(){
        try {

            final var appiumUrl = Config.get("appium.url");
            final var desiredCapabilities = getDesiredLocalCapabilities();

            Logs.debug("Inicializando el DRIVER");
            final var driver = new AndroidDriver(new URL(appiumUrl), desiredCapabilities);

            Logs.debug("Asignando el driver al driver provider");
            new DriverProvider().set(driver);

        } catch (MalformedURLException malformedURLException) {

            Logs.error("Error al inicializar el DRIVER: %s", malformedURLException.getMessage());
            throw new RuntimeException(malformedURLException);
        }
    }

    private void buildRemoteDriver(){
        throw new UnsupportedOperationException(
                "El modo remoto (variable JOB_NAME definida) aun no esta implementado en DriverManager.buildRemoteDriver()");
    }

    private static DesiredCapabilities getDesiredLocalCapabilities() {

        final var desiredCapabilities = new DesiredCapabilities();

        final var fileAPK = new File(Config.get("app.path"));

        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("appium:automationName", "UiAutomator2");
        desiredCapabilities.setCapability("appium:app", fileAPK.getAbsolutePath());
        desiredCapabilities.setCapability("appium:appWaitActivity", Config.get("app.wait.activity"));
        desiredCapabilities.setCapability("appium:autoGrantPermissions", true);

        // Más tiempo para emuladores lentos
        desiredCapabilities.setCapability("appium:adbExecTimeout", Config.getInt("timeout.adb.exec"));
        desiredCapabilities.setCapability("appium:uiautomator2ServerLaunchTimeout", Config.getInt("timeout.uiautomator2.launch"));

        return desiredCapabilities;
    }
}