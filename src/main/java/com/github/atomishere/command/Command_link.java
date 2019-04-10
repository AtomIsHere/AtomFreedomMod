package com.github.atomishere.command;

import com.github.atomishere.discord.accountlinker.AccountContainer;
import com.github.atomishere.discord.accountlinker.AccountManager;
import com.github.atomishere.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Link your discord account to your minecraft account on this server.", usage = "/<command> <linkcode>")
public class Command_link extends FreedomCommand {
    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        if(args.length != 1) {
            return false;
        }

        int linkCode;
        try {
            linkCode = Integer.parseInt(args[0]);
        } catch(NumberFormatException ex) {
            sender.sendMessage(ChatColor.RED + args[0] + " is not a number.");
            return true;
        }

        AccountManager accountManager = plugin.dm.getAccountManager();

        AccountContainer container = accountManager.getContainerFromCode(linkCode);

        if(container == null) {
            sender.sendMessage(ChatColor.RED + "Invalid code.");
            return true;
        }

        if(!container.getPlayer().equals(playerSender)) {
            sender.sendMessage(ChatColor.RED + "You're not linked to that code.");
            return true;
        }

        if(!accountManager.verifyLinkCode(linkCode)) {
            sender.sendMessage(ChatColor.RED + "Invalid code.");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Successfully linked discord account " + container.getUser().getName());
        return true;
    }
}
