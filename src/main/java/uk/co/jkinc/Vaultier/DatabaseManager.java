package uk.co.jkinc.Vaultier;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseManager {
    private transient File dataFolder;
    public Database db;
    public void init(Vaultier plugin) {
        dataFolder = new File(plugin.getDataFolder(), "data"); // get folder "plugins/MyPlugin/data"
        dataFolder.mkdirs(); // create folder if not exists
        try {
            FileInputStream fileIn = new FileInputStream(new File(dataFolder, "DB.ser"));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            db = (Database) in.readObject();
            in.close();
            fileIn.close();
            db.Transactions = new HashMap<String, Transaction>();
            return;
        } catch (IOException ignored) {}
        catch (ClassNotFoundException j) {
            j.printStackTrace();
        }
        // DB does not exist create empty class

        db = new Database();
        db.APIKeys = new HashMap<String, UUID>();
        db.Transactions = new HashMap<String, Transaction>();
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
