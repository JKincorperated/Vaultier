package uk.co.jkinc.Vaultier.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.jkinc.Vaultier.Transaction;
import uk.co.jkinc.Vaultier.Vaultier;

public class BlockTransfer extends DenyTransfer {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You need to be a player to execute this command");
            return true;
        }

        if (strings.length != 1) {
            sender.sendMessage(ChatColor.RED + "Do not invoke this command directly.");
            return true;
        }

        String transactionID = strings[0];

        Transaction transaction = Vaultier.database.db.Transactions.get(transactionID);

        if (transaction == null) {
            sender.sendMessage(ChatColor.RED + "Unknown Transaction, did it time out?");
            return true;
        }

        Player player = (Player) sender;

        if (transaction.Payer != player) {
            sender.sendMessage(ChatColor.RED + "You cannot interact with someone else's transaction!");
            return true;
        }


        String uid = transaction.Payee.getUniqueId().toString() + player.getUniqueId().toString();

        Vaultier.database.db.blockedPlayers.put(uid, true);

        player.sendMessage(ChatColor.RED + "Blocked Player");

        super.onCommand(sender, command, s, strings);

        return true;
    }
}
