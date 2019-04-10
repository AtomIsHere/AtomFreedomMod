package com.github.atomishere.command;

import com.github.atomishere.discord.accountlinker.AccountContainer;
import com.github.atomishere.discord.accountlinker.AccountManager;
import com.github.atomishere.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Unlink your discord account.", usage = "/<command>")
public class Command_unlink extends FreedomCommand {

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        AccountManager accountManager = plugin.dm.getAccountManager();

        AccountContainer container = accountManager.getContainerByPlayer(playerSender);
        if(container == null) {
            sender.sendMessage(ChatColor.GREEN + "You are not linked to a discord account");
        }

        if(!accountManager.unlink(container)) {
            sender.sendMessage(ChatColor.GREEN + "You are not linked to a discord account");
        }

        sender.sendMessage(ChatColor.RED + "Successfully unlinked a discord account");

        return true;
    }
}
