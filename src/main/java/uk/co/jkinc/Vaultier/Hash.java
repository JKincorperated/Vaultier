package uk.co.jkinc.Vaultier;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static byte[] hashAPIKey (String ApiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(ApiKey.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException i) {
            // This should never happen
            i.printStackTrace();
            return new byte[0];
        }
    }
}
