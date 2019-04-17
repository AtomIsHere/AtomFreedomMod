package com.github.atomishere.discord.console;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class DiscordCommandSender implements ConsoleCommandSender {
    private final User discordUser;
    private final OfflinePlayer offlinePlayer;
    private final MessageChannel consoleChannel;

    public DiscordCommandSender(User discordUser, OfflinePlayer offlinePlayer, MessageChannel consoleChannel) {
        this.discordUser = discordUser;
        this.offlinePlayer = offlinePlayer;
        this.consoleChannel = consoleChannel;
    }

    @Override
    public void sendMessage(String message) {
        PrivateChannel privateChannel = discordUser.openPrivateChannel().complete();
        if(privateChannel == null) {
            consoleChannel.sendMessage(message).complete();
            return;
        }

        privateChannel.sendMessage(message).complete();
    }

    @Override
    public void sendMessage(String[] messages) {
        PrivateChannel privateChannel = discordUser.openPrivateChannel().complete();
        if(privateChannel == null) {
            sendMessage(messages, consoleChannel);
        }

        sendMessage(messages, privateChannel);
    }

    private void sendMessage(String[] messages, MessageChannel channel) {
        for(String message : messages) {
            channel.sendMessage(message).complete();
        }
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public String getName() {
        return offlinePlayer.getName();
    }

    @Override
    public Spigot spigot() {
        return new Spigot() {
            @Override
            public void sendMessage(BaseComponent component) {
                DiscordCommandSender.this.sendMessage(component.toPlainText());
            }

            @Override
            public void sendMessage(BaseComponent... components) {
                for(BaseComponent bc : components) {
                    sendMessage(bc);
                }
            }
        };
    }

    @Override
    public boolean isConversing() {
        return false;
    }

    @Override
    public void acceptConversationInput(String s) {
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return false;
    }

    @Override
    public void abandonConversation(Conversation conversation) {
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {
    }

    @Override
    public void sendRawMessage(String message) {
        sendMessage(message);
    }

    @Override
    public boolean isPermissionSet(String s) {
        return true;
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return true;
    }

    @Override
    public boolean hasPermission(String s) {
        return true;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return true;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
    }

    @Override
    public void recalculatePermissions() {
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean b) {
    }
}
