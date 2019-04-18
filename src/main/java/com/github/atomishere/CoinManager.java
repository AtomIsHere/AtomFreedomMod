package com.github.atomishere;

import com.github.atomishere.util.FLog;
import lombok.Getter;
import net.pravian.aero.config.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoinManager extends FreedomService {
    private static final String CONFIG_FILENAME = "coins.yml";

    private final YamlConfig config;
    @Getter
    private final Map<OfflinePlayer, Long> playerCoins = new HashMap<>();

    public CoinManager(AtomFreedomMod plugin) {
        super(plugin);

        this.config = new YamlConfig(plugin, CONFIG_FILENAME, true);
    }

    @Override
    protected void onStart() {
        config.load();

        load();
    }

    @Override
    protected void onStop() {
        save();

        config.save();
    }

    public Long getPlayerCoins(OfflinePlayer player) {
        return playerCoins.get(player);
    }

    public void load() {
        for(String key : config.getKeys(false)) {
            if(!config.isConfigurationSection(key)) {
                continue;
            }

            ConfigurationSection coinSection = config.getConfigurationSection(key);

            if(!coinSection.isString("uuid") && !coinSection.isLong("coins")) {
                continue;
            }

            UUID uuid;
            try {
                uuid = UUID.fromString(coinSection.getString("uuid"));
            } catch(IllegalArgumentException ex) {
                FLog.severe(key + " is invalid in coin config.");
                FLog.severe(ex);
                continue;
            }

            OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);
            if(player == null) {
                FLog.info("Can not find player from uuid " + key + " in coin config");
                continue;
            }

            playerCoins.put(player, coinSection.getLong("coins"));
        }
    }

    public void save() {
        //Clear Config
        for(String key : config.getKeys(false)) {
            config.set(key, null);
        }

        for(Map.Entry<OfflinePlayer, Long> entry : playerCoins.entrySet()) {
            config.set("uuid", entry.getKey().getUniqueId().toString());
            config.set("coins", entry.getValue());
        }

        playerCoins.clear();
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

       if(!playerCoins.containsKey(player)) {
           playerCoins.put(player, 0L);
       }
    }
}
