package uk.co.jkinc.Vaultier;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import uk.co.jkinc.Vaultier.cmds.CmdRegistry;

import java.io.IOException;
import java.util.logging.Logger;

public class Vaultier extends JavaPlugin {
    public static Economy econ = null;
    public static Permission perms = null;
    public static boolean vaultPresent = false;
    public static Plugin plugin;
    public static DatabaseManager database = new DatabaseManager();
    public HTTP server = new HTTP();
    Logger log;
    @Override
    public void onEnable() {
        plugin = this;

        this.log = getLogger();
        log.info("Starting " + this.getName());

        plugin.saveDefaultConfig();

        if (checkVault()) {
            if (setupEconomy()) {
                this.log.info("Economy set up.");
            } else {
                this.log.warning("No economy plugin detected.");
                getPluginLoader().disablePlugin((Plugin) this);
                return;
            }
            if (setupPermissions()) {
                this.log.info("Permissions set up.");
            } else {
                this.log.warning("No permission plugin detected");
                getPluginLoader().disablePlugin((Plugin) this);
                return;
            }
        }

        CmdRegistry.registerCommands(this);

        log.info("Starting HTTP server");
        try {
            server.startServer();
        } catch (IOException e) {
            log.warning("Failed to start HTTP server.");
        }

        database.init(this);
    }

    @Override
    public void onDisable() {
        server.stopServer();
        log.info("Saving DB...");
        database.save();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = (Economy) rsp.getProvider();
        return (econ != null);
    }


    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = (Permission) rsp.getProvider();
        return (perms != null);
    }

    private boolean checkVault() {
        vaultPresent = (getServer().getPluginManager().getPlugin("Vault") != null);
        return vaultPresent;
    }
}
