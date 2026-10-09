package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Logs;

public class YourInformationPage extends BasePage {

    private final By firstNameInput = AppiumBy.accessibilityId("test-First Name");
    private final By lastNameInput = AppiumBy.accessibilityId("test-Last Name");
    private final By zipcodeInput = AppiumBy.accessibilityId("test-Zip/Postal Code");
    private final By continueButton = AppiumBy.accessibilityId("test-CONTINUE");

    @Override
    public void waitPageToLoad() {
        waitPage(firstNameInput);
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando la pagina de your information page");
        verifyDisplayed(firstNameInput, lastNameInput, zipcodeInput, continueButton);
    }

    public void fillData(String name, String lastname, String zipcode) {
        if (!name.isEmpty()) { //si el name es vacio no se escribe
            Logs.info("Escribiendo el name");
            type(firstNameInput, name);
        }
        if (!lastname.isEmpty()) { //si el lastname es vacio no se escribe
            Logs.info("Escribiendo el lastname");
            type(lastNameInput, lastname);
        }
        if (!zipcode.isEmpty()) { //si el zipcode es vacio no se escribe
            Logs.info("Escribiendo el zipcode");
            type(zipcodeInput, zipcode);
        }

        Logs.info("Haciendo click en el boton de continue");
        click(continueButton);
    }

    public void verifyErrorMessage(String errorMessage) {
        verifyErrorMessage(ERROR_MESSAGE_LABEL, errorMessage);
    }

}
