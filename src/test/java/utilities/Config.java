package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Acceso centralizado a la configuracion (config.properties).
 * Prioridad: propiedad de sistema, variable de entorno y por ultimo el archivo.
 */
public final class Config {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private Config() {
    }

    private static Properties load() {
        final var properties = new Properties();
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("No se encontro " + CONFIG_FILE + " en el classpath");
            }
            properties.load(stream);
        } catch (IOException ioException) {
            throw new IllegalStateException("No se pudo leer " + CONFIG_FILE, ioException);
        }
        return properties;
    }

    public static String get(String key) {
        final var systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        final var envValue = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }

        final var fileValue = PROPERTIES.getProperty(key);
        if (fileValue == null || fileValue.isBlank()) {
            throw new IllegalStateException("Falta la configuracion '" + key + "' en " + CONFIG_FILE);
        }
        return fileValue.trim();
    }

    public static int getInt(String key) {
        final var value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException numberFormatException) {
            throw new IllegalStateException("La configuracion '" + key + "' debe ser un entero y es: " + value);
        }
    }
}
