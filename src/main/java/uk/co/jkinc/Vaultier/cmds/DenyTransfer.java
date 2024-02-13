package uk.co.jkinc.Vaultier.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.jkinc.Vaultier.Vaultier;
import uk.co.jkinc.Vaultier.Transaction;

public class DenyTransfer implements CommandExecutor {
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

        if (transaction.state != Transaction.Status.PENDING) {
            sender.sendMessage(ChatColor.BLUE + "You have already interacted with this transaction.");
            return true;
        }

        transaction.state = Transaction.Status.REJECTED;
        Vaultier.database.db.Transactions.put(transactionID, transaction);

        sender.sendMessage(ChatColor.RED + "Transaction denied.");

        return true;
    }
}
