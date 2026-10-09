package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Gestures;
import utilities.Logs;

import java.util.List;

public class ShoppingPage extends BasePage {
    private final By title = AppiumBy.androidUIAutomator("text(\"PRODUCTS\")");
    private final By filterButton = AppiumBy.accessibilityId("test-Modal Selector Button");
    private final By toggleViewButton = AppiumBy.accessibilityId("test-Toggle");
    private final By dropZone = AppiumBy.accessibilityId("test-Cart drop zone");

    //List
    private final By itemList = AppiumBy.accessibilityId("test-PRODUCTS");
    private final By imageList = AppiumBy.androidUIAutomator(
            "new UiSelector().description(\"test-Item\").childSelector(new UiSelector().className(\"android.widget.ImageView\"))");
    private final By handleList = AppiumBy.accessibilityId("test-Drag Handle");


    @Override
    public void waitPageToLoad() {
        waitPage(title);
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando la pagina de Shopping");
        verifyDisplayed(title, filterButton, toggleViewButton, itemList);
    }

    public void clickItemImage(int index) {
        Logs.info("Haciendo click en la imagen del item segun su index");
        findAll(imageList).get(index).click();
    }


    public void changeViewMode() {
        Logs.info("Cambiando a modo lista");
        click(toggleViewButton);

        Logs.info("Esperando que se ordene en formato lista");
        waitForDisplayed(handleList);
    }


    /**
     * Agrega {@code itemCount} items al carrito arrastrando siempre el primer item visible de la lista
     * hacia la zona del carrito (cada item arrastrado sale de la lista).
     */
    public void addToCartDrag(int itemCount) {
        Logs.info("Arrastrando el item hacia la barra para agregar al carrito segun cantidad");
        final var destinationElement = find(dropZone);

        for (int i = 0; i < itemCount; i++) {
            final var handleListElements = findAll(handleList);
            final var originElement = handleListElements.get(0);
            final var handlesBefore = handleListElements.size();
            Gestures.dragTo(originElement, destinationElement);

            // espera a que el item arrastrado salga de la lista antes de tomar el siguiente
            waitFor(driver -> driver.findElements(handleList).size() < handlesBefore);
        }
    }


    /**
     * Agrega al carrito tantos items como elementos tenga la lista.
     * <b>Los valores de la lista (indices) no se usan</b>: solo cuenta su tamano, y se arrastran
     * los primeros items visibles. Los features indican los indices por legibilidad.
     */
    public void addToCartDrag(List<Integer> itemListAdd) {
        addToCartDrag(itemListAdd.size());
    }

}
