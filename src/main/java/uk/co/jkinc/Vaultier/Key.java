package uk.co.jkinc.Vaultier;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Key {
    public static String genAPIKey(UUID playerUUID) {
        Integer playerSeq = Vaultier.database.db.playerSequence.get(playerUUID);
        if (playerSeq == null) {
            playerSeq = 0;
        }
        Vaultier.database.db.playerSequence.put(playerUUID, playerSeq + 1);
        return JWT.create()
                .withIssuer("Vaultier")
                .withClaim("Player", playerUUID.toString())
                .withClaim("index", playerSeq + 1)
                .sign(Algorithm.ECDSA256(Vaultier.database.db.publicKey, Vaultier.database.db.privateKey));
    }

    public static UUID verifyJWT(String jwt) {
        // Verify a JWT
        try {
            DecodedJWT _jwt = JWT.require(Algorithm.ECDSA256(Vaultier.database.db.publicKey, Vaultier.database.db.privateKey))
                    .withIssuer("Vaultier")
                    .build()
                    .verify(jwt);


            UUID playerUID = UUID.fromString(_jwt.getClaim("Player").asString());

            if (!Objects.equals(Vaultier.database.db.playerSequence.get(playerUID), _jwt.getClaim("index").asInt())) {
                return null;
            }

            return playerUID;
        } catch (JWTVerificationException i) {
            return null;
        }
    }

    public static String genTransactionID() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        return "t_" + bytesToHex(bytes);
    }
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
