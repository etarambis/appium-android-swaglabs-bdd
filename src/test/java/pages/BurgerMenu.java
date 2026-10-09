package pages;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Logs;

public class BurgerMenu extends BasePage {
    private final By logoutButton = AppiumBy.accessibilityId("test-LOGOUT");
    private final By closeButton  = AppiumBy.accessibilityId("test-Close");

    @Override

    public void waitPageToLoad() {
        waitPage(logoutButton, this.getClass().getSimpleName());
    }

    @Override

    public void verifyPage() {
        Logs.info("Verificando el burger menu");
        Assertions.assertAll(
                () -> Assertions.assertTrue((find(logoutButton).isDisplayed()))
        );

    }


    public void clickLogout(){
        Logs.info("haciendo click en el boton de logout");
        find(logoutButton).click();
    }


    public void clickCloseX(){
        Logs.info("haciendo click en la x");
        find(closeButton).click();
    }

}