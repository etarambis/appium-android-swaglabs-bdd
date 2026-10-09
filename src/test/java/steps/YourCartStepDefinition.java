package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.YourCartPage;
import utilities.CommonFlows;

import java.util.List;

public class YourCartStepDefinition {

    private final CommonFlows commonFlows = new CommonFlows();
    private final YourCartPage yourCartPage = new YourCartPage();

    @Given("El usuario navega a la pagina de Your Cart agregando al carrito items con indices:")
    public void navigateYourCart(List<Integer> indexList) {

          commonFlows.goToYourCartPage(indexList);
    }

    @When("El usuario elimina el item con index {int} arrastrando de derecha a izquierda")
    public void deleteItemByIndex(int index) {

        yourCartPage.deleteItemFromList(index);
    }
}
