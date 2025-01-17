package com.andurilunlogic.flyspeed.listeners;

import com.andurilunlogic.flyspeed.FlySpeedPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.andurilunlogic.flyspeed.FlySpeedPlugin.prefix;

public class OnJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(FlySpeedPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (e.getPlayer().getWalkSpeed() != 0.2F) {
                    e.getPlayer().setWalkSpeed(0.2F);
                    e.getPlayer().sendMessage(prefix + ChatColor.GREEN + "Walkspeed set to default!");
                }

                if (e.getPlayer().getFlySpeed() != 0.1F) {
                    e.getPlayer().setFlySpeed(0.1F);
                    e.getPlayer().sendMessage(prefix + ChatColor.GREEN + "Flyspeed set to default!");
                }
            }
        }, 4);
    }

}
