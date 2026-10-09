package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.YourInformationPage;
import utilities.CommonFlows;

import java.util.List;

public class YourInformationStepDefinition {

    private final CommonFlows commonFlows = new CommonFlows();
    private final YourInformationPage yourInformationPage = new YourInformationPage();

    @Given("El usuario navega a Your Information agregando al carrito items con indices:")
    public void goToInformationCart(List<Integer> listInteger) {

        commonFlows.goToYourInformationPage(listInteger);
    }

    @When("El usuario escribe el {string}, {string}, {string} y presiona el boton de Continue")
    public void userBotonContinue(String name, String lastName, String zipCode) {

        yourInformationPage.fillData(name, lastName, zipCode);
    }

    @Then("Debe aparecer el mensaje con texto {string}")

    public void verifyErrorMessage(String message) {

        yourInformationPage.verifyErrorMessage(message);
    }
}
