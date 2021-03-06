package com.github.atomishere.command;

import com.github.atomishere.rank.Rank;
import com.github.atomishere.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Sets everyone's Worldedit block modification limit to the default.", usage = "/<command>", aliases = "setl,swl")
public class Command_setlimit extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        FUtil.adminAction(sender.getName(), "Setting everyone's Worldedit block modification limit to 2500.", true);
        for (final Player player : server.getOnlinePlayers())
        {
            plugin.web.setLimit(player, 2500);
        }
        return true;
    }
}
