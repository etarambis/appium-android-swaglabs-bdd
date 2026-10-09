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
        }
    }

    private void buildLocalDriver(){
        try {

            final var appiumUrl = "http://127.0.0.1:4723/";
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

    }

    private static DesiredCapabilities getDesiredLocalCapabilities() {

        final var desiredCapabilities = new DesiredCapabilities();

        final var fileAPK = new File("src/test/resources/apk/sauceLabs.apk");

        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("appium:automationName", "UiAutomator2");
        desiredCapabilities.setCapability("appium:app", fileAPK.getAbsolutePath());
        desiredCapabilities.setCapability("appium:appWaitActivity", "com.swaglabsmobileapp.MainActivity");
        desiredCapabilities.setCapability("appium:autoGrantPermissions", true);

        // Más tiempo para emuladores lentos
        desiredCapabilities.setCapability("appium:adbExecTimeout", 60000);
        desiredCapabilities.setCapability("appium:uiautomator2ServerLaunchTimeout", 90000);

        return desiredCapabilities;
    }
}