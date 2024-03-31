package com.andurilunlogic.flyspeed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import static com.andurilunlogic.flyspeed.FlySpeedPlugin.prefix;
import static com.andurilunlogic.flyspeed.FlySpeedPlugin.noPermission;
import static com.andurilunlogic.flyspeed.FlySpeedPlugin.flightDisabled;
import static com.andurilunlogic.flyspeed.FlySpeedPlugin.flightEnabled;

public class FlyspeedCommands implements CommandExecutor {
  Plugin plugin = (Plugin)FlySpeedPlugin.getPlugin(FlySpeedPlugin.class);
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
   
    if (!(sender instanceof Player)) { 
    Player p = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("fly"))
      if (args.length == 0) {
        if (!p.hasPermission("flyspeed.fly")) {
          p.sendMessage(String.valueOf(prefix) + noPermission);
          return true;
        } 
        if (p.getAllowFlight()) {
          p.setFlySpeed(0.1F);
          p.setAllowFlight(false);
          if (flightDisabled.contains("%player%"))
            flightDisabled = flightDisabled.replace("%player%", p.getName()); 
          p.sendMessage(String.valueOf(prefix) + flightDisabled);
        } else {
          for (String s : this.plugin.getConfig().getConfigurationSection("default-speed").getKeys(false)) {
            if (p.hasPermission(this.plugin.getConfig().getString("default-speed." + s + ".permission")) && !p.isOp()) {
              p.sendMessage(this.plugin.getConfig().getString("default-speed." + s + ".permission"));
              float speed = (float)this.plugin.getConfig().getDouble("default-speed." + s + ".speed");
              try {
                if (speed < 0.0F) {
                  p.sendMessage(String.valueOf(prefix) + "speed specified in the config cannot be lower than 0!");
                  return true;
                } 
                if (speed > 10.0F) {
                  p.sendMessage(String.valueOf(prefix) + "speed specified in the config cannot be higher than 10!");
                  return true;
                } 
                float fspeed = speed / 10.0F;
                p.setAllowFlight(true);
                p.setFlySpeed(fspeed);
                if (flightEnabled.contains("%player%"))
                  flightEnabled = flightEnabled.replace("%player%", p.getName()); 
                p.sendMessage(String.valueOf(prefix) + flightEnabled);
                return true;
              } catch (NumberFormatException e) {
                p.sendMessage(String.valueOf(prefix) + "speed specified in the config needs to be a number!");
              } 
            } 
          } 
          p.setAllowFlight(true);
          if (flightEnabled.contains("%player%"))
            flightEnabled = flightEnabled.replace("%player%", p.getName()); 
          p.sendMessage(String.valueOf(prefix) + flightEnabled);
          return true;
        } 
      } else if (args.length == 1) {
        if (!p.hasPermission("flyspeed.fly.others")) {
          p.sendMessage(String.valueOf(prefix) + noPermission);
          return true;
        } 
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
          p.sendMessage("" + args[0] + " is not online!");
          return true;
        } 
        if (target.getAllowFlight()) {
          target.setFlySpeed(0.1F);
          target.setAllowFlight(false);
          if (flightDisabled.contains("%player%"))
            flightDisabled = flightDisabled.replace("%player%", target.getName()); 
          p.sendMessage(String.valueOf(prefix) + flightDisabled);
          if (p != target)
            target.sendMessage(String.valueOf(prefix) + flightDisabled); 
        } else {
          for (String s : this.plugin.getConfig().getConfigurationSection("default-speed").getKeys(false)) {
            if (target.hasPermission(this.plugin.getConfig().getString("default-speed." + s + ".permission")) && !target.isOp()) {
              float speed = (float)this.plugin.getConfig().getDouble("default-speed." + s + ".speed");
              try {
                if (speed < 0.0F) {
                  p.sendMessage(String.valueOf(prefix) + "speed specified in the config cannot be lower than 0!");
                  return true;
                } 
                if (speed > 10.0F) {
                  p.sendMessage(String.valueOf(prefix) + "speed specified in the config cannot be higher than 10!");
                  return true;
                } 
                float fspeed = speed / 10.0F;
                target.setAllowFlight(true);
                target.setFlySpeed(fspeed);
                if (flightEnabled.contains("%player%"))
                  flightEnabled = flightEnabled.replace("%player%", target.getName()); 
                p.sendMessage(String.valueOf(prefix) + flightEnabled);
                if (p != target)
                  target.sendMessage(String.valueOf(prefix) + flightEnabled); 
                return true;
              } catch (NumberFormatException e) {
                p.sendMessage(String.valueOf(prefix) + "speed specified in the config needs to be a number!");
              } 
            } 
          } 
          target.setAllowFlight(true);
          if (flightEnabled.contains("%player%"))
            flightEnabled = flightEnabled.replace("%player%", target.getName()); 
          p.sendMessage(String.valueOf(prefix) + flightEnabled);
          if (p != target)
            target.sendMessage(String.valueOf(prefix) + flightEnabled); 
          return true;
        } 
      } else {
        p.sendMessage("/fly [player]");
        return true;
      }  
     
    return true;
  }
  return false;  
  }
  }
