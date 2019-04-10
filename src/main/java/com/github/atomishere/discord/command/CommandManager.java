package com.github.atomishere.discord.command;

import com.github.atomishere.AtomFreedomMod;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private static final String COMMAND_PREFIX = "!";

    private final Map<String, DiscordCommand> commands;

    private final JDA jda;
    private final AtomFreedomMod plugin;

    public CommandManager(JDA jda, AtomFreedomMod plugin) {
        this.commands = new HashMap<>();

        this.jda = jda;
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentStripped();

        if(!message.startsWith(COMMAND_PREFIX)) {
           return;
        }

        String[] rawArgs = message.split(" ");

        String name = rawArgs[0].replaceFirst("!", "");

        String[] args = Arrays.copyOfRange(rawArgs, 1, rawArgs.length);

        DiscordCommand command = commands.get(name);

        if(command == null) {
            return;
        }

        command.run(event.getMember(), event.getMessage(), args);
    }


    public void registerCommand(DiscordCommand command, String name) {
        commands.put(name, command);
    }
}
