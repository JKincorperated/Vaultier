package uk.co.jkinc.Vaultier;

import java.util.Random;

public class Key {
    public static String genAPIKey() {
        byte[] b = new byte[32];
        new Random().nextBytes(b);
        return "cc_" + bytesToHex(b);
    }
    public static String genTransactionID() {
        byte[] b = new byte[64];
        new Random().nextBytes(b);
        return "t_" + bytesToHex(b);
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
