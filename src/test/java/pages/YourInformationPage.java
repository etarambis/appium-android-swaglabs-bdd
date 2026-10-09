package pages;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Logs;

public class YourInformationPage extends BasePage {

    private final By firstNameInput = AppiumBy.accessibilityId("test-First Name");
    private final By lastNameInput = AppiumBy.accessibilityId("test-Last Name");
    private final By zipcodeInput = AppiumBy.accessibilityId("test-Zip/Postal Code");
    private final By continueButton = AppiumBy.accessibilityId("test-CONTINUE");
    private final By errorLabel = AppiumBy.androidUIAutomator(
            "description(\"test-Error message\").childSelector(className(\"android.widget.TextView\"))");

    @Override
    public void waitPageToLoad() {
        waitPage(firstNameInput, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando la pagina de your information page");

        Assertions.assertAll(
                () -> Assertions.assertTrue(find(firstNameInput).isDisplayed()),
                () -> Assertions.assertTrue(find(lastNameInput).isDisplayed()),
                () -> Assertions.assertTrue(find(zipcodeInput).isDisplayed()),
                () -> Assertions.assertTrue(find(continueButton).isDisplayed())
        );
    }

    public void fillData(String name, String lastname, String zipcode) {
        if (!name.isEmpty()) { //si el name es vacio no se escribe
            Logs.info("Escribiendo el name");
            find(firstNameInput).sendKeys(name);
        }
        if (!lastname.isEmpty()) { //si el lastname es vacio no se escribe
            Logs.info("Escribiendo el lastname");
            find(lastNameInput).sendKeys(lastname);
        }
        if (!zipcode.isEmpty()) { //si el zipcode es vacio no se escribe
            Logs.info("Escribiendo el zipcode");
            find(zipcodeInput).sendKeys(zipcode);
        }

        Logs.info("Haciendo click en el boton de continue");
        find(continueButton).click();
    }

    public void verifyErrorMessage(String errorMessage) {
        Logs.info("Verificando el mensaje de error");
        waitForDisplayed(errorLabel);   // primero espera a que aparezca

        final var errorLabelElement = find(errorLabel);

        Assertions.assertAll(
                () -> Assertions.assertTrue(errorLabelElement.isDisplayed()),
                () -> Assertions.assertEquals(errorMessage, errorLabelElement.getText())
        );
    }

}