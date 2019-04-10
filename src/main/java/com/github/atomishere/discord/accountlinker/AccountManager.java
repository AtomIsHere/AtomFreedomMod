package com.github.atomishere.discord.accountlinker;

import com.github.atomishere.AtomFreedomMod;
import com.github.atomishere.util.FLog;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class AccountManager {
    private final ConfigurationSection accountSection;

    private final List<AccountContainer> accounts = new ArrayList<>();
    private final Map<Integer, AccountContainer> linkCodes = new HashMap<>();

    private final JDA jda;

    private final AtomFreedomMod plugin;

    public AccountManager(JDA jda, ConfigurationSection accountSection, AtomFreedomMod plugin) {
        this.accountSection = accountSection;
        this.jda = jda;
        this.plugin = plugin;
    }

    public void load() {
        for(String key : accountSection.getKeys(false)) {
            if(!accountSection.isConfigurationSection(key)) {
                continue;
            }

            ConfigurationSection accountSection = this.accountSection.getConfigurationSection(key);

            if(!accountSection.isString("uuid")) {
                continue;
            }

            String uuidS = accountSection.getString("uuid");

            UUID uuid;
            try {
                uuid = UUID.fromString(uuidS);
            } catch(IllegalArgumentException ex) {
                FLog.severe("Can not load the uuid for linked account " + accountSection.getName());
                FLog.severe(ex);

                continue;
            }

            OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);

            if(player == null) {
                FLog.severe("Can not load the player for linked account " + accountSection.getName());
                continue;
            }

            if(!accountSection.isLong("userid")) {
                continue;
            }

            long userId = accountSection.getLong("userid");

            User user = jda.getUserById(userId);

            if(user == null)  {
                FLog.severe("Can not load discord user for linked account " + accountSection.getName());
                continue;
            }

            AccountContainer accountContainer = new AccountContainer(user, player);

            accounts.add(accountContainer);
        }
    }

    public void save() {
        //Clear the config
        for(String key : accountSection.getKeys(false)) {
            accountSection.set(key, null);
        }

        for(AccountContainer container : accounts) {
            ConfigurationSection accountSection = this.accountSection.createSection(container.getPlayer().getUniqueId().toString());

            accountSection.set("uuid", container.getPlayer().getUniqueId().toString());
            accountSection.set("userid", container.getUser().getIdLong());
        }

        linkCodes.clear();
        accounts.clear();
    }

    public AccountContainer getContainerFromCode(Integer code) {
        return linkCodes.get(code);
    }

    public Integer generateLinkCode(AccountContainer container) {
        if(accounts.contains(container)) {
            return null;
        }

        Random rand = new Random();
        int randomCode = rand.nextInt(10000);
        if(linkCodes.containsKey(randomCode)) {
            return generateLinkCode(container);
        }

        linkCodes.put(randomCode, container);

        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> linkCodes.remove(randomCode), 6000L);

        return randomCode;
    }

    public boolean verifyLinkCode(Integer code) {
        if(!linkCodes.containsKey(code)) {
            return false;
        }

        AccountContainer container = linkCodes.get(code);
        accounts.add(container);
        linkCodes.remove(code);
        return true;
    }

    public AccountContainer getContainerByPlayer(OfflinePlayer player) {
        for(AccountContainer container : accounts) {
            if(container.getPlayer().equals(player)) {
                return container;
            }
        }

        return null;
    }


    public AccountContainer getContainerByUser(User user) {
        for(AccountContainer container : accounts) {
            if(container.getUser().equals(user)) {
                return container;
            }
        }

        return null;
    }

    public boolean unlink(AccountContainer container) {
        return accounts.remove(container);
    }
}
