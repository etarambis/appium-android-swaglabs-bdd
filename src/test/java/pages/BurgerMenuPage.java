package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Logs;

public class BurgerMenuPage extends BasePage {
    private final By logoutButton = AppiumBy.accessibilityId("test-LOGOUT");
    private final By closeButton  = AppiumBy.accessibilityId("test-Close");

    @Override
    public void waitPageToLoad() {
        waitPage(logoutButton);
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando el burger menu");
        verifyDisplayed(logoutButton);
    }


    public void clickLogout() {
        Logs.info("haciendo click en el boton de logout");
        click(logoutButton);
    }


    public void clickCloseX() {
        Logs.info("haciendo click en la x");
        click(closeButton);
    }

}
