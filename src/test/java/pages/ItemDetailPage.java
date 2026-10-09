package pages;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Gestures;
import utilities.Logs;

public class ItemDetailPage extends BasePage {

    private static final String TEXT_VIEW_IN_DESCRIPTION = "new UiSelector().description(\"test-Description\")" +
            ".childSelector(new UiSelector().className(\"android.widget.TextView\").instance(%d))";

    private final By backProductsButton = AppiumBy.accessibilityId("test-BACK TO PRODUCTS");
    private final By itemTitle = AppiumBy.androidUIAutomator(String.format(TEXT_VIEW_IN_DESCRIPTION, 0));
    private final By itemDescription = AppiumBy.androidUIAutomator(String.format(TEXT_VIEW_IN_DESCRIPTION, 1));
    private final By itemImage = AppiumBy
            .androidUIAutomator("description(\"test-Image Container\")" +
                    ".childSelector(className(\"android.widget.ImageView\"))");
    private final By itemPrice = AppiumBy.accessibilityId("test-Price");
    private final By addCartButton = AppiumBy.accessibilityId("test-ADD TO CART");
    private final By canvas = AppiumBy
            .androidUIAutomator("className(\"android.widget.ScrollView\").description(\"test-Inventory item page\")");

    @Override
    public void waitPageToLoad() {
        waitPage(canvas);
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando la pagina del detalle del item");
        verifyDisplayed(backProductsButton, itemImage, itemPrice, itemTitle, itemDescription);
    }

    /**
     * Hace scroll hacia abajo y verifica que el boton Add to cart quede visible.
     */
    public void verifyAddToCartAfterScroll() {
        Logs.info("Haciendo scroll y verificando el boton Add to cart");
        Gestures.swipeVertical(50, 70, 30, find(canvas));
        Assertions.assertTrue(find(addCartButton).isDisplayed());
    }

    public void clickBackProducts() {
        Logs.info("Haciendo click en back to products");
        click(backProductsButton);
    }

    public void verifyItemInfo(String itemName, double expectedPrice) {
        Logs.info("Verificando la informacion del item");

        final var priceDollar = find(itemPrice).getText();
        final var priceNoDollar = Double.parseDouble(priceDollar.replace("$", ""));

        Assertions.assertAll(

                () -> Assertions.assertEquals(itemName, find(itemTitle).getText()),
                () -> Assertions.assertEquals(expectedPrice, priceNoDollar)

        );
    }

}
