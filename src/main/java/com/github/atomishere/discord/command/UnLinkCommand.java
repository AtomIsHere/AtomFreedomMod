package com.github.atomishere.discord.command;

import com.github.atomishere.AtomFreedomMod;
import com.github.atomishere.discord.accountlinker.AccountContainer;
import com.github.atomishere.discord.accountlinker.AccountManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class UnLinkCommand extends DiscordCommand {
    public UnLinkCommand(JDA jda, AtomFreedomMod plugin) {
        super(jda, plugin);
    }

    @Override
    public void run(Member sender, Message message, String[] args) {
        AccountManager accountManager = plugin.dm.getAccountManager();

        AccountContainer container = accountManager.getContainerByUser(sender.getUser());
        if(container == null) {
            message.getChannel().sendMessage("You are not linked to an account").complete();
        }

        if(!accountManager.unlink(container)) {
            message.getChannel().sendMessage("You are not linked to an account").complete();
        }

        message.getChannel().sendMessage("Successfully unlinked account").complete();
    }
}
