package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Gestures;
import utilities.Logs;

public class LoginPage extends BasePage {
    private final By usernameInput = AppiumBy.accessibilityId("test-Username");
    private final By passwordInput = AppiumBy.accessibilityId("test-Password");
    private final By loginButton   = AppiumBy.accessibilityId("test-LOGIN");
    private final By standardUserLabel = AppiumBy.accessibilityId("test-standard_user");
    private final By lockedUserLabel = AppiumBy.accessibilityId("test-locked_out_user");
    private final By problemUserLabel = AppiumBy.accessibilityId("test-problem_user");
    private final By passwordLabel = AppiumBy.androidUIAutomator("text(\"secret_sauce\")");
    private final By mainCanvas = AppiumBy.id("android:id/content");


    @Override
    public void waitPageToLoad() {
        waitPage(usernameInput);
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando la pagina de login");
        verifyDisplayed(usernameInput, passwordInput, loginButton);
    }


    public void fillData(String username, String password) {
        Logs.info("Escribiendo el username");
        type(usernameInput, username);

        Logs.info("Escribiendo el password");
        type(passwordInput, password);

        Logs.info("Haciendo click en el boton de login");
        click(loginButton);
    }


    public void fillDataTap() {
        Logs.info("Haciendo tap en el label de standard_user");
        Gestures.tap(find(standardUserLabel));

        Logs.info("Haciendo click en login");
        click(loginButton);
    }


    public void verifyErrorMessage(String errorText) {
        verifyErrorMessage(ERROR_MESSAGE_LABEL, errorText);
    }


    public void verifyLabels() {
        Logs.info("Verificando los labels de credenciales");
        Gestures.swipeVertical(50, 50, 20, find(mainCanvas));

        verifyDisplayed(standardUserLabel, lockedUserLabel, problemUserLabel, passwordLabel);
    }

}
