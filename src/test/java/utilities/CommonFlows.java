package utilities;

import data.DataGiver;
import pages.*;

import java.util.List;

public class CommonFlows {
    public void goToLoginPage() {
        new LoginPage().waitPageToLoad();
    }


    public void goToShoppingPage() {

        final var validCredentials = DataGiver.getValidCredentials();

        goToLoginPage();
        new LoginPage().fillData(validCredentials.getUsername(), validCredentials.getPassword());
        new ShoppingPage().waitPageToLoad();
    }


    public void openBurgerMenu() {
        goToShoppingPage();
        new TopBar().openBurgerMenu();
        new BurgerMenu().waitPageToLoad();
    }

    public void goToYourCartPage(List<Integer> itemListAdd) {
        goToShoppingPage();
        final var shoppingPage = new ShoppingPage();
        shoppingPage.changeViewMode();
        shoppingPage.addToCartDrag(itemListAdd);
        new TopBar().clickCheckout();
        new YourCartPage().waitPageToLoad();
    }

    public void goToItemDetailPage(int index) {
        goToShoppingPage();
        new ShoppingPage().clickItemImage(index);
        new ItemDetailPage().waitPageToLoad();
    }

    public void goToYourInformationPage(List<Integer> itemListAdd) {
        goToYourCartPage(itemListAdd);
        new YourCartPage().clickCheckout();
        new YourInformationPage().waitPageToLoad();
    }

}
