package pages;

import io.appium.java_client.AppiumBy;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import utilities.BasePage;
import utilities.Logs;

public class TopBar extends BasePage {
    private final By burgerButton = AppiumBy.accessibilityId("test-Menu");
    private final By checkoutButton = AppiumBy.accessibilityId("test-Cart");
    private final By itemCartCountLabel = AppiumBy.androidUIAutomator(
            "new UiSelector().description(\"test-Cart\").childSelector(new UiSelector().className(\"android.widget.TextView\"))");

    @Override
    public void waitPageToLoad() {
        // TODO document why this method is empty
    }

    @Override

    public void verifyPage() {
        Logs.info("Verificando la barra superior");
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(burgerButton).isDisplayed()),
                () -> Assertions.assertTrue(find(checkoutButton).isDisplayed())

        );
    }


    public void openBurgerMenu() {
        Logs.info("Abriendo el burger menu");
        click(burgerButton);
    }


    public void verifyItemCount(int expected) {
        Logs.info("Verificando la cantidad de items en el carrito: %d", expected);

        Assertions.assertEquals(Integer.parseInt(find(itemCartCountLabel).getText()), expected);

    }


    public void clickCheckout() {
        Logs.info("Haciendo click en checkout");
        click(checkoutButton);
    }

}