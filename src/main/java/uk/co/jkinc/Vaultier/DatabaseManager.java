package uk.co.jkinc.Vaultier;

import java.io.*;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseManager {
    private transient File dataFolder;
    public Database db;

    public DatabaseManager(Vaultier plugin) {
        dataFolder = new File(plugin.getDataFolder(), "data");
        dataFolder.mkdirs(); // create folder if not exists
        try {
            FileInputStream fileIn = new FileInputStream(new File(dataFolder, "DB.ser"));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            db = (Database) in.readObject();
            in.close();
            fileIn.close();
            db.Transactions = new HashMap<String, Transaction>();
            db.RateLimits = new HashMap<String, Integer>();
            db.currentTimePeriod = new Date().getTime() / (1000 * 60 * 30); // Reset rate limit every 30 minutes
            return;
        } catch (IOException ignored) {}
        catch (ClassNotFoundException j) {
            j.printStackTrace();
        }
        // DB does not exist create empty class

        db = new Database();
        db.Transactions = new HashMap<String, Transaction>();
        db.playerSequence = new HashMap<UUID, Integer>();
        db.blockedPlayers = new HashMap<String, Boolean>();

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(256, new SecureRandom());
            KeyPair keyPair = kpg.generateKeyPair();
            db.privateKey = (ECPrivateKey) keyPair.getPrivate();
            db.publicKey = (ECPublicKey) keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // This should never happen
        }
    }

    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream(new File(dataFolder, "DB.ser"));
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(db);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
