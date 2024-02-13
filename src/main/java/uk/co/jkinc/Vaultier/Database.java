package uk.co.jkinc.Vaultier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Database implements Serializable {
    public HashMap<String, UUID> APIKeys;
    public transient HashMap<String, Transaction> Transactions;
}
