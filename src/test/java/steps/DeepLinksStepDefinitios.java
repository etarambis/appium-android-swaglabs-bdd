package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ItemDetailPage;
import utilities.Deeplinks;

public class DeepLinksStepDefinitios {
    private final ItemDetailPage itemDetailPage = new ItemDetailPage();

    @When("El usuario usa el deeplink del item detail con index {int}")
    public void goToDeeplinkDetail(int index) {

        Deeplinks.goToItemDetail(index);
    }

    @Then("Se debe redirigir a la pagina de Item Detail")
    public void goToPageDetail() {
        itemDetailPage.waitPageToLoad();
        itemDetailPage.verifyPage();
    }

    @And("Verifica que se tenga el item {string} con precio {double}")
    public void verifyItemPrice(String name, double price) {

        itemDetailPage.verifyItemInfo(name, price);
    }

    @When("El usuario usa el deeplink de Shopping con indices {string}")
    public void usedDeeLinkShopping(String index) {

        Deeplinks.goToShopping(index);
    }
}
