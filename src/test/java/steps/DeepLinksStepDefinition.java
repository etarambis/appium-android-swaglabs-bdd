package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ItemDetailPage;
import utilities.DeepLinks;

public class DeepLinksStepDefinition {
    private final ItemDetailPage itemDetailPage = new ItemDetailPage();

    @When("El usuario usa el deeplink del item detail con index {int}")
    public void openItemDetailDeepLink(int index) {

        DeepLinks.goToItemDetail(index);
    }

    @Then("Se debe redirigir a la pagina de Item Detail")
    public void verifyItemDetailPageIsShown() {
        itemDetailPage.waitPageToLoad();
        itemDetailPage.verifyPage();
    }

    @And("Verifica que se tenga el item {string} con precio {double}")
    public void verifyItemNameAndPrice(String name, double price) {

        itemDetailPage.verifyItemInfo(name, price);
    }

    @When("El usuario usa el deeplink de Shopping con indices {string}")
    public void openShoppingDeepLink(String index) {

        DeepLinks.goToShopping(index);
    }
}
