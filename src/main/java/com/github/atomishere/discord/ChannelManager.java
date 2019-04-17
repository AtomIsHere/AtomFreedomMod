package com.github.atomishere.discord;

import com.github.atomishere.util.FLog;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChannelManager extends ListenerAdapter implements Listener {
    private final ConfigurationSection channelSection;

    private final JDA jda;

    private MessageChannel statusChannel = null;
    private MessageChannel chatChannel = null;

    @Getter
    private MessageChannel consoleChannel = null;

    public ChannelManager(ConfigurationSection channelSection, JDA jda) {
        this.channelSection = channelSection;
        this.jda = jda;
    }

    public boolean load() {
        if(!channelSection.isLong("guildid")) {
            FLog.severe("Could not load channel managers. Guild id is invalid in config.");
            return false;
        }

        Guild guild = jda.getGuildById(channelSection.getLong("guildid"));
        if(guild == null) {
            FLog.severe("Could not load channel managers. Guild id is invalid.");
            return false;
        }

        this.statusChannel = loadChannel(guild, "statusid");
        this.chatChannel = loadChannel(guild, "chatid");
        this.consoleChannel = loadChannel(guild, "consoleid");

        return true;
    }

    public void unload() {
        statusChannel = null;
        chatChannel = null;
        consoleChannel = null;
    }

    public void sendStatus(boolean online) {
        if(statusChannel == null) {
            return;
        }

        if(online) {
            statusChannel.sendMessage(":white_check_mark: Server is online!").complete();
        } else {
            statusChannel.sendMessage(":no_entry: Server is offline").complete();
        }
    }

    @EventHandler
    public void playerChatEvent(AsyncPlayerChatEvent event) {
        if(chatChannel == null) {
            return;
        }

        chatChannel.sendMessage(ChatColor.stripColor("[MINECRAFT] " + "<" + event.getPlayer().getDisplayName() + "> " + event.getMessage())).complete();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(chatChannel == null) {
            return;
        }

        if(!chatChannel.equals(event.getChannel())) {
            return;
        }

        if(event.getMember().getUser().isBot()) {
            return;
        }

        String name = event.getMember().getNickname();
        if(name == null) {
            name = event.getMember().getEffectiveName();
        }

        Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[DISCORD] " + ChatColor.WHITE + "<" + name + "> " + event.getMessage().getContentStripped());
    }

    private MessageChannel loadChannel(Guild guild, String name) {
        if(!(channelSection.isLong(name))) {
            FLog.severe("Could not load channel manager. Channel id is invalid in config.");
            return null;
        }

        MessageChannel channel = guild.getTextChannelById(channelSection.getLong(name));

        if(channel == null) {
            FLog.severe("Could not load channel manager. Channel id is invalid.");
            return null;
        }


        return channel;
    }
}
