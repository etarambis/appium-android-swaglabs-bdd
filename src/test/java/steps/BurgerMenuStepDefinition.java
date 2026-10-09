package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.BurgerMenu;
import pages.LoginPage;
import utilities.CommonFlows;

public class BurgerMenuStepDefinition {

    private final CommonFlows commonFlows = new CommonFlows();
    private final BurgerMenu burgerMenu = new BurgerMenu();
    private final LoginPage loginPage = new LoginPage();

    @Given("El usuario abre el Burger Menu")
    public void openBurguerMenu() {
        commonFlows.openBurgerMenu();
    }

    @When("El usuario hace click en logout")
    public void clickLogout() {
        burgerMenu.clickLogout();
    }

    @Then("Se debe redirigir a la pagina de Login")
    public void verifyNavigateLopginPAGE() {
        loginPage.waitPageToLoad();
        loginPage.verifyPage();
    }

    @When("El usuario hace click en la X")
    public void ClickX() {
        burgerMenu.clickCloseX();

    }
}
