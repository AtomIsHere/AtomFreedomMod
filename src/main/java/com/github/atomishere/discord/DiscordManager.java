package com.github.atomishere.discord;

import com.github.atomishere.AtomFreedomMod;
import com.github.atomishere.FreedomService;
import com.github.atomishere.discord.accountlinker.AccountManager;
import com.github.atomishere.discord.command.CommandManager;
import com.github.atomishere.discord.command.LinkCommad;
import com.github.atomishere.discord.command.UnLinkCommand;
import com.github.atomishere.discord.console.ConsoleManager;
import com.github.atomishere.util.FLog;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.pravian.aero.config.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import javax.security.auth.login.LoginException;

public class DiscordManager extends FreedomService {
    public static final String CONFIG_FILENAME = "discord.yml";

    private final YamlConfig config;

    @Getter
    private JDA jda = null;

    @Getter
    private AccountManager accountManager = null;

    private ChannelManager channelManager = null;

    private ConsoleManager consoleManager = null;

    public DiscordManager(AtomFreedomMod plugin) {
        super(plugin);

        this.config = new YamlConfig(plugin, CONFIG_FILENAME, true);
    }

    @Override
    protected void onStart() {
        config.load();

        //Load JDA
        JDA jda;
        try {
            jda = new JDABuilder(config.getString("secret_token"))
                    .addEventListeners(registerCommands())
                    .build();
        } catch(LoginException | NullPointerException ex) {
            FLog.severe("Invalid Token");
            FLog.severe(ex);
            return;
        }
        try {
            jda.awaitReady();
        } catch(InterruptedException ignored) {}

        this.jda = jda;

        //Load Account Manager
        ConfigurationSection accountSection = config.getConfigurationSection("accounts");
        if(accountSection == null) {
            accountSection = config.createSection("accounts");
        }

        AccountManager accountManager = new AccountManager(jda, accountSection, plugin);
        accountManager.load();
        this.accountManager = accountManager;

        //Load channel managers
        ConfigurationSection channelSection = config.getConfigurationSection("channels");
        if(channelSection == null) {
            FLog.severe("Could not find channels in config. Not loading channel managers.");
            channelManager = null;
        } else {
            channelManager = new ChannelManager(channelSection, jda);
            channelManager.load();

            ConsoleManager consoleManager = new ConsoleManager(channelManager.getConsoleChannel(), accountManager, plugin);
            consoleManager.load();

            this.consoleManager = consoleManager;

            jda.addEventListener(channelManager, consoleManager);

            Bukkit.getServer().getPluginManager().registerEvents(channelManager, plugin);

            channelManager.sendStatus(true);
        }
    }

    @Override
    protected void onStop() {
        if(channelManager != null) {
            channelManager.sendStatus(false);

            consoleManager.clearSenders();
            consoleManager = null;

            channelManager.unload();
            channelManager = null;
        }

        accountManager.save();
        accountManager = null;

        jda.shutdown();
        jda = null;

        config.save();
    }

    private CommandManager registerCommands() {
        CommandManager commandManager = new CommandManager(jda, plugin);

        //Register Commands
        commandManager.registerCommand(new LinkCommad(jda, plugin), "link");
        commandManager.registerCommand(new UnLinkCommand(jda, plugin), "unlink");

        return commandManager;
    }
}
