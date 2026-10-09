package data;

import models.Credential;
import java.util.Map;

public class DataGiver {
    private static Map<String, Credential> getCredentialMap() {
        return JsonReader.getCredentialMap().getMapCredentials();
    }

    public static Credential getValidCredentials() {
        return getCredentialMap().get("valid");
    }

    public static Credential getLockedCredentials() {
        return getCredentialMap().get("locked");
    }

    public static Credential getInvalidCredentials() {
        return getCredentialMap().get("invalid");
    }
}
