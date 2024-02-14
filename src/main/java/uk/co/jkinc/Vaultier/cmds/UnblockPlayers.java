package uk.co.jkinc.Vaultier.cmds;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.jkinc.Vaultier.Vaultier;

public class UnblockPlayers implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You need to be a player to execute this command");
            return true;
        }

        if (strings.length != 1) {
            return false;
        }

        Player player = (Player) sender;

        Player PlayerToUnblock = Bukkit.getPlayer(strings[0]);
        if (PlayerToUnblock == null) {
            sender.sendMessage(ChatColor.RED + "That player does not exist or is offline, try again later.");
            return true;
        }

        String uid = PlayerToUnblock.getUniqueId().toString() + player.getUniqueId().toString();

        Boolean isBlocked = Vaultier.database.db.blockedPlayers.get(uid);

        if (isBlocked == null || !isBlocked) { // Avoid artificially inflating the database.
            sender.sendMessage(ChatColor.RED + "That player is already unblocked");
            return true;
        }

        Vaultier.database.db.blockedPlayers.put(uid, false);
        sender.sendMessage(ChatColor.GREEN + "Unblocked player " + PlayerToUnblock.getDisplayName());

        return true;
    }
}
