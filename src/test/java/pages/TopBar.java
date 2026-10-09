package pages;

import io.appium.java_client.AppiumBy;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import utilities.BasePage;
import utilities.Logs;

public class TopBar extends BasePage {
    private final By burgerButton = AppiumBy.accessibilityId("test-Menu");
    private final By cartButton = AppiumBy.accessibilityId("test-Cart");
    private final By itemCartCountLabel = AppiumBy.androidUIAutomator(
            "new UiSelector().description(\"test-Cart\").childSelector(new UiSelector().className(\"android.widget.TextView\"))");

    @Override
    public void waitPageToLoad() {
        waitPage(burgerButton);
    }

    @Override
    public void verifyPage() {
        Logs.info("Verificando la barra superior");
        verifyDisplayed(burgerButton, cartButton);
    }


    public void openBurgerMenu() {
        Logs.info("Abriendo el burger menu");
        click(burgerButton);
    }


    public void verifyItemCount(int expected) {
        Logs.info("Verificando la cantidad de items en el carrito: %d", expected);

        Assertions.assertEquals(expected, Integer.parseInt(find(itemCartCountLabel).getText()));

    }


    public void clickCart() {
        Logs.info("Haciendo click en el carrito");
        click(cartButton);
    }

}
