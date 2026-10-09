package utilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    private final static int defaultTimeout = Config.getInt("timeout.default");

    private final int timeOut;

    public BasePage(int timeOut) {

        this.timeOut = timeOut;
    }

    public BasePage() {
        this(defaultTimeout); //llamo al constructor de arriba con el default timeout
    }

    protected AndroidDriver getDriver() {
        return new DriverProvider().get();
    }

    protected WebElement waitForDisplayed(By locator, int time) {
        final var wait = new WebDriverWait(getDriver(), Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForDisplayed(By locator) {
        return waitForDisplayed(locator, defaultTimeout);
    }

    protected void waitPage(By locator, String pageName) {
        Logs.info("Esperando que la pagina %s cargue", pageName);
        waitForDisplayed(locator, timeOut);
        Logs.info("%s ha cargado satisfactoriamente", pageName);
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

    public void pressBack() {
        Logs.info("Presionando atras en el dispositivo movil");
        getDriver().pressKey(new KeyEvent(AndroidKey.BACK));
    }

    protected void sleep(int timeMS) {
        try {
            Thread.sleep(timeMS);
        } catch (InterruptedException interruptedException) {
            Logs.error("InterruptedException: %s", interruptedException);
        }
    }

    public abstract void waitPageToLoad(); //esperar que cargue la pagina

    public abstract void verifyPage(); //verificar la UI de la pagina

}