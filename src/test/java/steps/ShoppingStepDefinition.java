package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ShoppingPage;
import pages.TopBar;
import utilities.CommonFlows;

import java.util.List;

public class ShoppingStepDefinition {

    private final CommonFlows commonFlows = new CommonFlows();
    private final ShoppingPage shoppingPage = new ShoppingPage();
    private final TopBar topBar = new TopBar();


    @Given("El usuario navega a la pagina de Shopping")
    public void goToShoppingPage() {

        commonFlows.goToShoppingPage();
    }

    @Then("El usuario verifica que la UI de la pagina de Shopping es correcta")
    public void verifyShoppingPage() {
        shoppingPage.verifyPage();
    }

    @When("El usuario activa el modo vertical")
    public void toggleViewMode() {

        shoppingPage.changeViewMode();
    }

    @And("El usuario agrega al carrito los items con indices:")
    public void addItemDrag(List<Integer> indexList) {

        shoppingPage.addToCartDrag(indexList);
    }

    @Then("El usuario verifica que haya {int} items en el carrito")
    public void verifyItemCount(int expectedValue) {

        topBar.verifyItemCount(expectedValue);
    }


}
