package utilities;

import io.appium.java_client.android.AndroidDriver;

public class DriverProvider {

    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    public void set(AndroidDriver driver) {
        DRIVER.set(driver);
    }


    public AndroidDriver get() {
        return DRIVER.get();
    }

    public void remove() {
        DRIVER.remove();
    }

}
