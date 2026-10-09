package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ItemDetailPage;
import pages.LoginPage;
import utilities.CommonFlows;

public class ItemDetailStepDefinition {

    private final CommonFlows commonFlows = new CommonFlows();
    private final ItemDetailPage itemDetailPage  = new ItemDetailPage();


    @Given("El usuario navega al detalle del item con index {int}")
    public void goToItemDetail(int index) {
        commonFlows.goToItemDetailPage(index);

    }

    @Then("El usuario verifica que la UI de la pagina de Login sea correcta")
    public void verifyItemDetailPage() {

        itemDetailPage.verifyPage();
    }

    @When("El usuario hace click en el boton Back to Products")
    public void clickBackToProducts() {
        itemDetailPage.clickBackProducts();
    }

    @When("El usuario presiona el boton back de su celular")
    public void pressBack() {

       itemDetailPage.pressBack();
    }
}
