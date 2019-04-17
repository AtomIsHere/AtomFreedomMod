package com.github.atomishere.discord.console;

import com.github.atomishere.AtomFreedomMod;
import com.github.atomishere.admin.Admin;
import com.github.atomishere.discord.accountlinker.AccountContainer;
import com.github.atomishere.discord.accountlinker.AccountManager;
import com.github.atomishere.rank.Rank;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class ConsoleManager extends ListenerAdapter {
    private final MessageChannel consoleChannel;
    private final AccountManager accountManager;
    private final AtomFreedomMod plugin;

    private final Map<User, DiscordCommandSender> commandSenders = new HashMap<>();

    public ConsoleManager(MessageChannel consoleChannel, AccountManager accountManager, AtomFreedomMod plugin) {
        this.consoleChannel = consoleChannel;
        this.accountManager = accountManager;
        this.plugin = plugin;
    }

    public void load() {
        Logger rootLogger = (Logger) LogManager.getRootLogger();

        OutputGrabber grabber = new OutputGrabber(consoleChannel);
        rootLogger.addAppender(grabber);
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, grabber, 0L, 10L);

        for(AccountContainer container : accountManager.getAccounts()) {
            loadCommandSender(container);
        }
    }

    public void clearSenders() {
        commandSenders.clear();
    }

    private void loadCommandSender(AccountContainer container) {
        Admin admin = plugin.al.getEntryByName(container.getPlayer().getName());
        if(admin == null) {
            return;
        }

        if(!admin.isActive()) {
            return;
        }

        if(!admin.getRank().isAtLeast(Rank.TELNET_ADMIN)) {
            return;
        }

        commandSenders.put(container.getUser(), new DiscordCommandSender(container.getUser(), container.getPlayer(), consoleChannel));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMember().getUser().isBot()) {
            return;
        }

        if(consoleChannel == null) {
            return;
        }

        if(!consoleChannel.equals(event.getChannel())) {
            return;
        }

        DiscordCommandSender commandSender = commandSenders.get(event.getMember().getUser());

        if(commandSender == null) {
            return;
        }

        Bukkit.getServer().dispatchCommand(commandSender, event.getMessage().getContentStripped());
    }
}
