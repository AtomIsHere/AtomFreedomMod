package com.github.atomishere.command;

import com.github.atomishere.rank.Rank;
import com.github.atomishere.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Essentials Interface Command - Remove the nickname of all players on the server.", usage = "/<command>")
public class Command_denick extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        FUtil.adminAction(sender.getName(), "Removing all nicknames", false);

        for (Player player : server.getOnlinePlayers())
        {
            plugin.esb.setNickname(player.getName(), null);
        }

        return true;
    }
}
