package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
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
        waitPage(itemList);
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando la pagina de your cart");
        verifyDisplayed(itemList);
    }

    public void deleteItemFromList(int index) {
        Logs.info("Eliminando un item de la lista por su index: %d", index);
        final var itemToDelete = findAll(itemList).get(index);
        Gestures.swipeHorizontal(50, 60, 20, itemToDelete);

        Logs.info("Haciendo click en el boton de eliminar");
        Gestures.tap(getDeleteButtonOf(itemToDelete));
    }

    /**
     * Devuelve el boton Delete que esta en la misma fila (misma franja vertical) que el item,
     * en lugar de tomar el primero de la pantalla.
     */
    private WebElement getDeleteButtonOf(WebElement item) {
        final var itemRect = item.getRect();

        return findAll(deleteButton).stream()
                .filter(button -> {
                    final var rect = button.getRect();
                    final var centerY = rect.getY() + rect.getHeight() / 2;
                    return centerY >= itemRect.getY() && centerY <= itemRect.getY() + itemRect.getHeight();
                })
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontro el boton Delete de la fila del item"));
    }

    public void clickCheckout() {
        Logs.info("Haciendo click en el boton de checkout");
        Gestures.swipeVertical(50, 80, 40, find(canvas));
        click(checkoutButton);
    }


}
