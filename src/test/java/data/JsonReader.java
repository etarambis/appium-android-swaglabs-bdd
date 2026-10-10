package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.CredentialJson;
import utilities.Config;
import utilities.Logs;

import java.io.File;
import java.io.IOException;

public class JsonReader {
    private static final String CREDENTIAL_PATH = Config.get("credentials.file");

    public static CredentialJson readCredentials() {
        final var objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(new File(CREDENTIAL_PATH), CredentialJson.class);
        } catch (IOException ioException) {
            Logs.error("Error al leer del JSON", ioException);
            throw new RuntimeException(ioException.getLocalizedMessage());
        }
    }
}