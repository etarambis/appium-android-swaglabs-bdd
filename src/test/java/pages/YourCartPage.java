package pages;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Gestures;
import utilities.Logs;

public class YourCartPage extends BasePage {
    private final By itemList = AppiumBy.accessibilityId("test-Item");
    private final By deleteButton = AppiumBy.accessibilityId("test-Delete");
    private final By checkoutButton = AppiumBy.accessibilityId("test-CHECKOUT");
    private final By canvas = AppiumBy.accessibilityId("test-Cart Content");

    @Override
    public void waitPageToLoad() {
        waitPage(itemList, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {

        Logs.info("Verificando la pagina de your cart");

        Assertions.assertAll(
                () -> Assertions.assertTrue(find(itemList).isDisplayed())
        );
    }

    public void deleteItemFromList(int index) {
        Logs.info("Eliminando un item de la lista por su index: %d", index);
        final var canvas = findAll(itemList).get(index);
        Gestures.swipeHorizontal(50,60,20, canvas);

        Logs.info("Haciendo click en el boton de eliminar");
        Gestures.tap(find(deleteButton));

    }

    public void clickCheckout() {
        Logs.info("Haciendo click en el boton de checkout");
        Gestures.swipeVertical(50, 80, 40, find(canvas));
        find(checkoutButton).click();
    }


}