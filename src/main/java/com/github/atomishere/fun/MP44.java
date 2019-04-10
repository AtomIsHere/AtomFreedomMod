package com.github.atomishere.fun;

import com.github.atomishere.AtomFreedomMod;
import com.github.atomishere.FreedomService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class MP44 extends FreedomService
{

    public MP44(AtomFreedomMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        plugin.pl.getPlayer(event.getPlayer()).disarmMP44();
    }

}
