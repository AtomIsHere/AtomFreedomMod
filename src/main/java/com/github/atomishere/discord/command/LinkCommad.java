package com.github.atomishere.discord.command;

import com.github.atomishere.AtomFreedomMod;
import com.github.atomishere.discord.accountlinker.AccountContainer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LinkCommad extends DiscordCommand {
    public LinkCommad(JDA jda, AtomFreedomMod plugin) {
        super(jda, plugin);
    }

    @Override
    public void run(Member sender, Message message, String[] args) {
        PrivateChannel channel = sender.getUser().openPrivateChannel().complete();
        if(channel == null) {
            message.getChannel().sendMessage("You need to open your DMs to do this").complete();
            return;
        }

        if(args.length != 1) {
            channel.sendMessage("Usage: !link <name>").complete();
            return;
        }

        Player player = Bukkit.getServer().getPlayer(args[0]);

        if(player == null) {
            channel.sendMessage("You have to be online to do this").complete();
            return;
        }

        AccountContainer container = new AccountContainer(sender.getUser(), player);

        Integer linkCode = plugin.dm.getAccountManager().generateLinkCode(container);
        if(linkCode == null) {
            channel.sendMessage("You already have a link code, please wait for it to expire to generate a new one").complete();
        }

        channel.sendMessage("Your link code is " + linkCode + ". It will expire in 5 minutes.").complete();
        channel.sendMessage("To link your account type /link " + linkCode + " in game").complete();
    }
}
