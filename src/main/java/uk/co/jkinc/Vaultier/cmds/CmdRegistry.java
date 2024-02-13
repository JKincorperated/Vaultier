package uk.co.jkinc.Vaultier.cmds;

import org.bukkit.plugin.java.JavaPlugin;

public class CmdRegistry {
    public static void registerCommands(JavaPlugin pluginInstance) {
        pluginInstance.getCommand("getapikey").setExecutor(new GenAPIKey());
        pluginInstance.getCommand("acceptTransfer").setExecutor(new AcceptTransfer());
        pluginInstance.getCommand("denyTransfer").setExecutor(new DenyTransfer());
    }
}
