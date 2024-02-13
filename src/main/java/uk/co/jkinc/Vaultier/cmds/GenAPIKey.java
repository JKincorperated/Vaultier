package uk.co.jkinc.Vaultier.cmds;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jkinc.Vaultier.Vaultier;
import uk.co.jkinc.Vaultier.Key;
import uk.co.jkinc.Vaultier.Hash;

import java.util.UUID;

public class GenAPIKey implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You need to be a player to execute this command");
            return true;
        }

        Player player = (Player)sender;
        UUID playerUUID = player.getUniqueId();

        String apikey = Key.genAPIKey();

        Vaultier.database.db.APIKeys.values().remove(playerUUID);
        Vaultier.database.db.APIKeys.put(Key.bytesToHex(Hash.hashAPIKey(apikey)), playerUUID);

        ComponentBuilder cb = new ComponentBuilder(
                apikey).bold(false);

        TextComponent message = new TextComponent( ChatColor.GREEN + "Successfully generated API key, click this message to copy it!" );
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, cb.create()));
        message.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, apikey ) );
        player.spigot().sendMessage( message );

        return true;
    }
}
