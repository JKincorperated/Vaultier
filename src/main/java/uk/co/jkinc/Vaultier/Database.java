package uk.co.jkinc.Vaultier;

import java.io.Serializable;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.HashMap;
import java.util.UUID;

public class Database implements Serializable {
    public ECPrivateKey privateKey;
    public ECPublicKey publicKey;
    public transient HashMap<String, Transaction> Transactions;
}
