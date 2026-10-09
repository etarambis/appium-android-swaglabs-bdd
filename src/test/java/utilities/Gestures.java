package utilities;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

public class Gestures {

    // Duraciones de los gestos, en milisegundos
    private static final int MOVE_TO_ELEMENT_MS = 1000;
    private static final int TAP_HOLD_MS = 1000;
    private static final int LONG_TAP_HOLD_MS = 3500;
    private static final int DRAG_APPROACH_MS = 500;
    private static final int DRAG_PRESS_MS = 2000;
    private static final int DRAG_MOVE_MS = 1000;
    private static final int DRAG_RELEASE_MS = 1500;
    private static final int SWIPE_PAUSE_MS = 1000;
    private static final int SWIPE_MOVE_MS = 1000;

    private static final PointerInput FINGER = new PointerInput(PointerInput.Kind.TOUCH, "FINGER");

    private static AndroidDriver getDriver() {
        return new DriverProvider().get();

    }

    public static void tap(WebElement element) {
        pressElement(element, TAP_HOLD_MS, 1);
    }

    public static void longTap(WebElement element) {
        pressElement(element, LONG_TAP_HOLD_MS, 1);
    }

    public static void doubleTap(WebElement element) {
        pressElement(element, TAP_HOLD_MS, 2);
    }

    /**
     * Mueve el dedo al centro del elemento y lo presiona {@code times} veces,
     * manteniendo cada pulsacion {@code holdMs} milisegundos.
     */
    private static void pressElement(WebElement element, int holdMs, int times) {
        final var centerPoint = getCenterPoint(element);
        final var sequence = new Sequence(FINGER, 1);

        Logs.debug("Moviendo el dedo hacia el elemento");
        sequence.addAction(
                FINGER.createPointerMove(
                        Duration.ofMillis(MOVE_TO_ELEMENT_MS),
                        PointerInput.Origin.viewport(),
                        centerPoint
                )
        );

        for (var i = 0; i < times; i++) {
            Logs.debug("Presionando el elemento");
            sequence.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

            Logs.debug("Esperando %d ms", holdMs);
            sequence.addAction(new Pause(FINGER, Duration.ofMillis(holdMs)));

            Logs.debug("Dejando de presionar el elemento");
            sequence.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        }

        getDriver().perform(List.of(sequence));
    }

    public static void dragTo(WebElement originElement, WebElement destinationElement) {
        final var originCenter = getCenterPoint(originElement);
        final var destinationCenter = getCenterPoint(destinationElement);
        final var sequence = new Sequence(FINGER, 1);

        //1. Movemos el dedo hacia el elemento de origen
        sequence.addAction(
                FINGER.createPointerMove(
                        Duration.ofMillis(DRAG_APPROACH_MS), //duración de la acción
                        PointerInput.Origin.viewport(), //área donde se hará la acción
                        originCenter //punto donde se hará la acción con respecto al canvas
                )
        );

        //2. Tocamos la pantalla bajando el dedo
        sequence.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        //3. Agregamos una pequeña pausa
        sequence.addAction(new Pause(FINGER, Duration.ofMillis(DRAG_PRESS_MS)));

        //4. Arrastramos hacia el elemento de destino
        sequence.addAction(
                FINGER.createPointerMove(
                        Duration.ofMillis(DRAG_MOVE_MS), //duración de la acción
                        PointerInput.Origin.viewport(), //área donde se hará la acción
                        destinationCenter //punto donde se hará la acción con respecto al canvas
                )
        );

        //5. Agregamos una pequeña pausa
        sequence.addAction(new Pause(FINGER, Duration.ofMillis(DRAG_RELEASE_MS)));

        //6. Dejamos de tocar la pantalla levantando el dedo
        sequence.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        //7. Finalmente, ejecutamos las acciones
        getDriver().perform(List.of(sequence));
    }


    public static void swipeGeneral(
            double startPercentX,
            double startPercentY,
            double endPercentX,
            double endPercentY,
            WebElement element
    ) {
        final var startPoint =
                getElementPointUsingPercentages(startPercentX, startPercentY, element);
        final var endPoint =
                getElementPointUsingPercentages(endPercentX, endPercentY, element);
        swipeGeneralPuntos(startPoint, endPoint);
    }

    public static void swipeHorizontal(
            double percentY,
            double startPercentX,
            double endPercentX,
            WebElement element
    ) {
        swipeGeneral(startPercentX, percentY, endPercentX, percentY, element);
    }

    public static void swipeVertical(
            double percentX,
            double startPercentY,
            double endPercentY,
            WebElement element
    ) {
        swipeGeneral(percentX, startPercentY, percentX, endPercentY, element);
    }

    private static void swipeGeneralPuntos(Point origin, Point destination) {
        Logs.debug("Haciendo swipe desde el punto %s hasta el punto %s", origin, destination);
        final var sequence = new Sequence(FINGER, 1);

        Logs.debug("Moviendo el dedo hacia la posición inicial");
        sequence.addAction(
                FINGER.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        origin
                )
        );

        Logs.debug("Tocando la pantalla en el punto de origen");
        sequence.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        Logs.debug("Agregando una breve pausa");
        sequence.addAction(new Pause(FINGER, Duration.ofMillis(SWIPE_PAUSE_MS)));

        Logs.debug("Moviendo el dedo hacia la posición final");
        sequence.addAction(
                FINGER.createPointerMove(
                        Duration.ofMillis(SWIPE_MOVE_MS),
                        PointerInput.Origin.viewport(),
                        destination
                )
        );

        Logs.debug("Dejando de tocar la pantalla en el punto de destino");
        sequence.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Logs.debug("Ejecutando las acciones");
        getDriver().perform(List.of(sequence));


    }


    private static Point getCenterPoint(WebElement element) {
        final var elementLocation = element.getLocation();
        final var elementSize = element.getSize();

        final var centerX = elementLocation.getX() + elementSize.getWidth() / 2;
        final var centerY = elementLocation.getY() + elementSize.getHeight() / 2;

        return new Point(centerX, centerY);
    }

    private static Point getElementPointUsingPercentages(
            double percentageX,
            double percentageY,
            WebElement element
    ) {
        final var location = element.getLocation();
        final var size = element.getSize();

        final var xDelta = (percentageX / 100) * size.getWidth();
        final var yDelta = (percentageY / 100) * size.getHeight();

        final var x = (int) (location.getX() + xDelta);
        final var y = (int) (location.getY() + yDelta);

        return new Point(x, y);
    }

}
