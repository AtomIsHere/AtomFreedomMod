package com.github.atomishere.command;

import com.github.atomishere.rank.Rank;
import com.github.atomishere.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Goto the nether.", usage = "/<command>")
public class Command_nether extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        plugin.wm.gotoWorld(playerSender, server.getWorlds().get(0).getName() + "_nether");
        return true;
    }
}
