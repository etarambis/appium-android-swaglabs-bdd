package utilities;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public abstract class BasePage {
    private static final int DEFAULT_TIMEOUT = Config.getInt("timeout.default");

    /**
     * Label del mensaje de error que la app muestra en Login y en Your Information.
     */
    protected static final By ERROR_MESSAGE_LABEL = AppiumBy.androidUIAutomator(
            "new UiSelector().description(\"test-Error message\")" +
                    ".childSelector(new UiSelector().className(\"android.widget.TextView\"))");

    private final int timeOut;

    public BasePage(int timeOut) {

        this.timeOut = timeOut;
    }

    public BasePage() {
        this(DEFAULT_TIMEOUT); //llamo al constructor de arriba con el default timeout
    }

    protected AndroidDriver getDriver() {
        return new DriverProvider().get();
    }

    protected WebElement waitForDisplayed(By locator, int time) {
        final var wait = new WebDriverWait(getDriver(), Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForDisplayed(By locator) {
        return waitForDisplayed(locator, DEFAULT_TIMEOUT);
    }

    protected void waitPage(By locator, String pageName) {
        Logs.info("Esperando que la pagina %s cargue", pageName);
        waitForDisplayed(locator, timeOut);
        Logs.info("%s ha cargado satisfactoriamente", pageName);
    }

    /**
     * Espera que cargue la pagina usando el nombre de la clase en los logs.
     */
    protected void waitPage(By locator) {
        waitPage(locator, this.getClass().getSimpleName());
    }

    /**
     * Espera una condicion arbitraria (hasta el timeout de la pagina) y devuelve su resultado.
     */
    protected <T> T waitFor(ExpectedCondition<T> condition) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeOut)).until(condition);
    }

    /**
     * Busca un elemento esperando a que exista en la pantalla (no exige que sea visible,
     * para que {@code isDisplayed()} en las aserciones siga fallando como aserción).
     */
    protected WebElement find(By locator) {
        return waitFor(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Busca elementos esperando a que exista al menos uno.
     */
    protected List<WebElement> findAll(By locator) {
        return waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator) {
        waitFor(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        waitFor(ExpectedConditions.elementToBeClickable(locator)).sendKeys(text);
    }

    /**
     * Verifica que todos los elementos se muestren, reportando juntos los que fallen.
     */
    protected void verifyDisplayed(By... locators) {
        final var checks = Arrays.stream(locators)
                .map(locator -> (Executable) () ->
                        Assertions.assertTrue(find(locator).isDisplayed(), "No se muestra el elemento: " + locator))
                .toArray(Executable[]::new);
        Assertions.assertAll(checks);
    }

    /**
     * Espera el mensaje de error indicado por el locator y compara su texto.
     */
    protected void verifyErrorMessage(By errorLocator, String expectedText) {
        Logs.info("Esperando que el mensaje de error aparezca");
        final var errorElement = waitForDisplayed(errorLocator);

        Logs.info("Verificando el mensaje de error");
        Assertions.assertAll(
                () -> Assertions.assertTrue(errorElement.isDisplayed()),
                () -> Assertions.assertEquals(expectedText, errorElement.getText())
        );
    }

    public void pressBack() {
        Logs.info("Presionando atras en el dispositivo movil");
        getDriver().pressKey(new KeyEvent(AndroidKey.BACK));
    }

    public abstract void waitPageToLoad(); //esperar que cargue la pagina

    public abstract void verifyPage(); //verificar la UI de la pagina

}
