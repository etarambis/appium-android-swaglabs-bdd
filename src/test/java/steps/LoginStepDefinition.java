package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.LoginPage;
import pages.ShoppingPage;
import utilities.CommonFlows;

public class LoginStepDefinition {

    private final CommonFlows commonFlows = new CommonFlows();
    private final LoginPage loginPage = new LoginPage();
    private final ShoppingPage shoppingPage = new ShoppingPage();

    @Given("El usuario navega a la pagina de Login")
    public void goToLoginPage() {
        commonFlows.goToLoginPage();
    }

    @When("El usuario escribe el username {string} con el password {string} y presiona el boton de login")
    public void fillData(String username, String password) {
        loginPage.fillData(username, password);
    }

    @Then("Debe aparecer un mensaje de error indicando {string}")
    public void verifyErrorMessage(String errorMessage) {
        loginPage.verifyErrorMessage(errorMessage);
    }

    @Then("El usuario verifica que la UI de la pagina de Login es correcta")
    public void verifyLoginPage() {

        loginPage.verifyPage();
    }

    @When("El usuario se loguea haciendo tap")
    public void loginTap() {

        loginPage.fillDataTap();
    }

    @Then("Se debe redirigir a la pagina de Shopping")
    public void verifyNavigateShoppingPage() {

        shoppingPage.waitPageToLoad();
        shoppingPage.verifyPage();
    }

    @Then("El usuario verifica los labels de credenciales")
    public void verifyCredentialLabels() {
        loginPage.verifyLabels();
    }
}
