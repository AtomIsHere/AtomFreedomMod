package com.github.atomishere.discord.accountlinker;

import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.OfflinePlayer;

public class AccountContainer {
    @Getter
    private final User user;

    @Getter
    private final OfflinePlayer player;

    public AccountContainer(User user, OfflinePlayer player) {
        this.user = user;
        this.player = player;
    }
}
