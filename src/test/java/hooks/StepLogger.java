package hooks;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.HookTestStep;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;
import utilities.Logs;

/**
 * Plugin de Cucumber que registra en el log cada step (texto, estado y duracion) y, si falla,
 * el stack trace completo. Se activa en junit-platform.properties (cucumber.plugin).
 */
public class StepLogger implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::onStepFinished);
    }

    private void onStepFinished(TestStepFinished event) {
        final var result = event.getResult();
        final var status = result.getStatus();
        final var millis = result.getDuration().toMillis();

        if (event.getTestStep() instanceof PickleStepTestStep stepTestStep) {
            final var step = stepTestStep.getStep();
            final var message = String.format("STEP %s %s -> %s (%d ms)",
                    step.getKeyword().trim(), step.getText(), status, millis);
            log(status, message, result.getError());

        } else if (event.getTestStep() instanceof HookTestStep hookTestStep && status != Status.PASSED) {
            // Los hooks que pasan no aportan; los que fallan (p. ej. al crear el driver) si
            final var message = String.format("HOOK %s -> %s (%d ms)", hookTestStep.getHookType(), status, millis);
            log(status, message, result.getError());
        }
    }

    private void log(Status status, String message, Throwable error) {
        if (status == Status.PASSED) {
            Logs.info(message);
        } else if (error != null) {
            Logs.error(message, error);
        } else {
            Logs.warning(message);
        }
    }
}
