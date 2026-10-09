package pages;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utilities.BasePage;
import utilities.Gestures;
import utilities.Logs;

public class ItemDetailPage extends BasePage {

    private final By backProductsButton = AppiumBy.accessibilityId("test-BACK TO PRODUCTS");
    private final By itemDescription = AppiumBy
            .xpath("//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView");
    private final By itemImage = AppiumBy
            .androidUIAutomator("description(\"test-Image Container\")" +
                    ".childSelector(className(\"android.widget.ImageView\"))");
    private final By itemPrice = AppiumBy.accessibilityId("test-Price");
    private final By addCartButton = AppiumBy.accessibilityId("test-ADD TO CART");
    private final By canvas = AppiumBy
            .androidUIAutomator("className(\"android.widget.ScrollView\").description(\"test-Inventory item page\")");

    private WebElement getTitleElement() {
        return findAll(itemDescription).get(0);
    }

    private WebElement getDescriptionElement() {
        return findAll(itemDescription).get(1);
    }

    @Override
    public void waitPageToLoad() {
        //waitPage(canvas, this.getClass().getSimpleName());
        waitForDisplayed(canvas, 20);
        Logs.info("ItemDetailPage ha cargado satisfactoriamente");
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando la pagina del detalle del item");

        Assertions.assertAll(
                () -> Assertions.assertTrue(find(backProductsButton).isDisplayed()),
                () -> Assertions.assertTrue(find(itemImage).isDisplayed()),
                () -> Assertions.assertTrue(find(itemPrice).isDisplayed()),
                () -> Assertions.assertTrue(getTitleElement().isDisplayed()),
                () -> Assertions.assertTrue(getDescriptionElement().isDisplayed())
        );

        Gestures.swipeVertical(50, 70, 30, find(canvas));
        Assertions.assertTrue(find(addCartButton).isDisplayed());
    }

    public void clickBackProducts() {
        Logs.info("Haciendo click en back to products");
        find(backProductsButton).click();
    }

    public void verifyItemInfo(String itemName, double expectedPrice) {
        Logs.info("Verificando la informacion del item");

        final var priceDollar = find(itemPrice).getText();
        final var priceNoDollar = Double.parseDouble(priceDollar.replace("$", ""));

        Assertions.assertAll(

                () -> Assertions.assertEquals(getTitleElement().getText(), itemName),
                () -> Assertions.assertEquals(priceNoDollar, expectedPrice)

        );


    }

}
