package com.github.atomishere.discord.command;

import com.github.atomishere.AtomFreedomMod;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public abstract class DiscordCommand {
    protected final JDA jda;
    protected final AtomFreedomMod plugin;

    public DiscordCommand(JDA jda, AtomFreedomMod plugin) {
        this.jda = jda;
        this.plugin = plugin;
    }

    public abstract void run(Member sender, Message message, String[] args);
}
