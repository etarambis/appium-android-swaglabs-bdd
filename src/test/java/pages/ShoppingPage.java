package pages;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Assertions;
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
        waitPage(title, this.getClass().getSimpleName());
    }

    @Override

    public void verifyPage() {
        Logs.info("Verificando la pagina de Shopping");
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(title).isDisplayed()),
                () -> Assertions.assertTrue(find(filterButton).isDisplayed()),
                () -> Assertions.assertTrue(find(toggleViewButton).isDisplayed()),
                () -> Assertions.assertTrue(find(itemList).isDisplayed())
        );
    }

    public void clickItemImage(int index) {
        Logs.info("Haciendo click en la imagen del item segun su index");
        final var elements = findAll(imageList);
        Logs.info("Cantidad de imagenes encontradas: " + elements.size()); // TEMPORAL
        elements.get(index).click();
    }


    public void changeViewMode() {
        Logs.info("Cambiando a modo lista");
        find(toggleViewButton).click();

        Logs.info("Esperando que se ordene en formato lista");
        sleep(1500);
    }


    public void addToCartDrag(int cantidad) {
        Logs.info("Arrastrando el item hacia la barra para agregar al carrito segun cantidad");
        final var destinyElement = find(dropZone);

        for (int i = 0; i < cantidad; i++) {
            final var handleListElements = findAll(handleList);
            final var originElement = handleListElements.get(0);
            Gestures.dragTo(originElement, destinyElement);
        }
    }


    public void addToCartDrag(List<Integer> itemListAdd) {
        addToCartDrag(itemListAdd.size());
    }

}
