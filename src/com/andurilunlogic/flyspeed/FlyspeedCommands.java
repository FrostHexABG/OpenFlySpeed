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
      if (cmd.getName().equalsIgnoreCase("flyspeed") && 
        args.length == 1 && 
        args[0].equalsIgnoreCase("reload")) {
        if (!sender.hasPermission("flyspeed.reload")) {
          sender.sendMessage(String.valueOf(prefix) + noPermission);
          return true;
        } 
        this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        this.plugin.getServer().getPluginManager().enablePlugin(this.plugin);
        sender.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed] reloaded!");
        return true;
      } 
      sender.sendMessage(String.valueOf(prefix) + "You cannot use this command from the console!");
      return true;
    } 
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
    if (cmd.getName().equalsIgnoreCase("speed")) {
      if (p.isFlying()) {
        if (!p.hasPermission("flyspeed.walkspeed")) {
          p.sendMessage(String.valueOf(prefix) + noPermission);
          return true;
        } 
        if (args.length == 0) {
          float fspeedn = p.getFlySpeed();
          float fspeed = fspeedn * 10.0F;
          p.sendMessage(String.valueOf(prefix) + ChatColor.GOLD + "Your flyspeed is: " + fspeed);
          return true;
        } 
        if (args.length == 1) {
          if (args[0].equalsIgnoreCase("reset")) {
            if (!p.hasPermission("flyspeed.flyspeed")) {
              p.sendMessage(String.valueOf(prefix) + noPermission);
              return true;
            } 
            p.setFlySpeed(0.1F);
            p.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Flyspeed set to default!");
            return true;
          } 
          if (args[0].equalsIgnoreCase("help")) {
            if (!p.hasPermission("flyspeed.help")) {
              p.sendMessage(String.valueOf(prefix) + noPermission);
              return true;
            } 
            p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.GOLD + " Help");
            p.sendMessage(ChatColor.GOLD + "======================================================================");
            p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed help-------------------Brings up this list");
            p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed reload-----------------Reloads the plugin.");
            p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed reset------------------Resets your flyspeed to the default.");
            p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed reset <player>----------Resets flyspeed to default.");
            p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed <speed>----------------Sets your own flyspeed.");
            p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed <speed> <player>--------Sets flyspeed for someone else");
            p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /walkspeed help------------------Displays /walkspeed commands.");
            p.sendMessage(ChatColor.GOLD + "======================================================================");
            return true;
          } 
          Player target = Bukkit.getPlayer(args[0]);
          if (target != null) {
            if (!p.hasPermission("flyspeed.flyspeed.others")) {
              p.sendMessage(String.valueOf(prefix) + noPermission);
              return true;
            } 
            float fspeedn = target.getFlySpeed();
            float fspeed = fspeedn * 10.0F;
            p.sendMessage(String.valueOf(prefix) + ChatColor.GOLD + "Flyspeed of " + args[0] + " is: " + fspeed);
            return true;
          } 
          try {
            float speed = Float.valueOf(args[0]).floatValue();
            if (!p.hasPermission("flyspeed.flyspeed")) {
              p.sendMessage(String.valueOf(prefix) + noPermission);
              return true;
            } 
            if (speed < 0.0F) {
              p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
              return true;
            } 
            if (speed > 10.0F) {
              p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
              return true;
            } 
            float speed2 = speed / 10.0F;
            p.setFlySpeed(speed2);
            p.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Flyspeed set to: " + speed);
            return true;
          } catch (NumberFormatException e) {
            p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "That is not a valid speed.");
            return false;
          } 
        } 
        p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Usage: /speed <1-10;reset;help>");
        return true;
      } 
      if (args.length == 0) {
        if (!p.hasPermission("flyspeed.walkspeed")) {
          p.sendMessage(String.valueOf(prefix) + noPermission);
          return true;
        } 
        if (args.length == 0) {
          float wspeedn = p.getWalkSpeed();
          float wspeed = wspeedn * 10.0F;
          p.sendMessage(String.valueOf(prefix) + ChatColor.GOLD + "Your walkspeed is: " + wspeed);
          return true;
        } 
      } 
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("reset")) {
          if (!p.hasPermission("flyspeed.walkspeed")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          p.setWalkSpeed(0.2F);
          p.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Walkspeed set to default!");
          return true;
        } 
        if (args[0].equalsIgnoreCase("help")) {
          if (!p.hasPermission("flyspeed.help")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          p.sendMessage("" + ChatColor.GOLD);
          p.sendMessage(ChatColor.DARK_AQUA + "[WalkSpeed]" + ChatColor.GOLD + " Help");
          p.sendMessage(ChatColor.GOLD + "======================================================================");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /walkspeed reset--------------------Resets your walkspeed to the default.");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /walkspeed reset <player>------------Resets walkspeed to default.");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /walkspeed <speed>------------------Sets your own walkspeed.");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /walkspeed <speed> <player>----------Sets walkspeed for someone else.");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed help----------------------Displays /flyspeed commands");
          p.sendMessage(ChatColor.GOLD + "======================================================================");
          return true;
        } 
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
          if (!p.hasPermission("flyspeed.walkspeed.others")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          float wspeedn = target.getWalkSpeed();
          float wspeed = wspeedn * 10.0F;
          p.sendMessage(String.valueOf(prefix) + ChatColor.GOLD + "Walkspeed of " + args[0] + " is: " + wspeed);
          return true;
        } 
        try {
          float speed = Float.valueOf(args[0]).floatValue();
          if (!p.hasPermission("flyspeed.walkspeed")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          if (speed < 0.0F) {
            p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
            return true;
          } 
          if (speed > 10.0F) {
            p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
            return true;
          } 
          float speed2 = speed / 10.0F;
          p.setWalkSpeed(speed2);
          p.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Walkspeed set to: " + speed);
          return true;
        } catch (NumberFormatException e) {
          p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "That is not a valid speed.");
          return false;
        } 
      } 
      p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Usage: /speed <1-10;reset;help>");
    } 
    if (cmd.getName().equalsIgnoreCase("flyspeed")) {
      if (!p.hasPermission("flyspeed.walkspeed")) {
        p.sendMessage(String.valueOf(prefix) + noPermission);
        return true;
      } 
      if (args.length == 0) {
        float fspeedn = p.getFlySpeed();
        float fspeed = fspeedn * 10.0F;
        p.sendMessage(String.valueOf(prefix) + ChatColor.GOLD + "Your flyspeed is: " + fspeed);
        return true;
      } 
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("reload")) {
          if (!p.hasPermission("flyspeed.reload")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          this.plugin.reloadConfig();
          this.plugin.saveConfig();
          sender.sendMessage(String.valueOf(prefix) + ChatColor.AQUA + "FlySpeed reloaded!");
          return true;
        } 
        if (args[0].equalsIgnoreCase("info")) {
          PluginDescriptionFile pdf = this.plugin.getDescription();
          p.sendMessage("");
          p.sendMessage(ChatColor.GOLD + "======================================================================");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.GOLD + " Version: " + pdf.getVersion());
          p.sendMessage(ChatColor.DARK_AQUA + "Author: " + ChatColor.GOLD + "AndurilUnlogic");
          p.sendMessage(ChatColor.DARK_AQUA + "Description: " + ChatColor.GOLD + pdf.getDescription());
          p.sendMessage(ChatColor.DARK_AQUA + "Commands: " + ChatColor.GOLD + "Use '/flyspeed help' (or '/walkspeed help') to see a list of all commands");
          p.sendMessage(ChatColor.GOLD + "======================================================================");
          return true;
        } 
        if (args[0].equalsIgnoreCase("reset")) {
          if (!p.hasPermission("flyspeed.flyspeed")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          p.setFlySpeed(0.1F);
          p.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Flyspeed set to default!");
          return true;
        } 
        if (args[0].equalsIgnoreCase("help")) {
          if (!p.hasPermission("flyspeed.help")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.GOLD + " Help");
          p.sendMessage(ChatColor.GOLD + "======================================================================");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed help-------------------Brings up this list");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed reload-----------------Reloads the plugin.");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed reset------------------Resets your flyspeed to the default.");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed reset <player>----------Resets flyspeed to default.");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed <speed>----------------Sets your own flyspeed.");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /flyspeed <speed> <player>--------Sets flyspeed for someone else");
          p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE + " /walkspeed help------------------Displays /walkspeed commands.");
          p.sendMessage(ChatColor.GOLD + "======================================================================");
          return true;
        } 
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
          if (!p.hasPermission("flyspeed.flyspeed.others")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          float fspeedn = target.getFlySpeed();
          float fspeed = fspeedn * 10.0F;
          p.sendMessage(String.valueOf(prefix) + ChatColor.GOLD + "Flyspeed of " + args[0] + " is: " + fspeed);
          return true;
        } 
        try {
          float speed = Float.valueOf(args[0]).floatValue();
          if (!p.hasPermission("flyspeed.flyspeed")) {
            p.sendMessage(String.valueOf(prefix) + noPermission);
            return true;
          } 
          if (speed < 0.0F) {
            p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
            return true;
          } 
          if (speed > 10.0F) {
            p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
            return true;
          } 
          float speed2 = speed / 10.0F;
          p.setFlySpeed(speed2);
          p.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Flyspeed set to: " + speed);
          return true;
        } catch (NumberFormatException e) {
          p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "That is not a valid speed.");
          return false;
        } 
      } 
      if (args.length == 2) {
        if (!p.hasPermission("flyspeed.flyspeed.others")) {
          p.sendMessage(String.valueOf(prefix) + noPermission);
          return true;
        } 
        Player target = Bukkit.getServer().getPlayer(args[1]);
        if (target == null) {
          p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Couldn't find player!");
          return false;
        } 
        if (args[0].equalsIgnoreCase("reset")) {
          target.setFlySpeed(0.1F);
          target.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Flyspeed set to default!");
          p.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Flyspeed of " + target.getName() + " set to default!");
          return true;
        } 
        if (args[0].equalsIgnoreCase("get")) {
          float fspeedn = target.getFlySpeed();
          float fspeed = fspeedn * 10.0F;
          p.sendMessage(String.valueOf(prefix) + ChatColor.GOLD + "Flyspeed from " + target.getName() + "is: " + fspeed);
          return true;
        } 
        try {
          float speed = Float.valueOf(args[0]).floatValue();
          if (speed < 0.0F) {
            p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
            return true;
          } 
          if (speed > 10.0F) {
            p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
            return true;
          } 
          float speed2 = speed / 10.0F;
          target.setFlySpeed(speed2);
          p.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Flyspeed of " + target.getName() + " set to: " + speed);
          target.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + "Flyspeed set to: " + speed);
          return true;
        } catch (NumberFormatException e) {
          p.sendMessage(String.valueOf(prefix) + ChatColor.RED + "That is not a valid speed.");
          return false;
        } 
      } 
      return true;
    } 
     
    return true;
  }
}
