package com.andurilunlogic.flyspeed.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import com.andurilunlogic.flyspeed.FlySpeedPlugin;

import static com.andurilunlogic.flyspeed.FlySpeedPlugin.noPermission;
import static com.andurilunlogic.flyspeed.FlySpeedPlugin.prefix;

@SuppressWarnings("deprecation")
public class FlySpeedCommand implements CommandExecutor {

	private static FlySpeedPlugin plugin;

	public FlySpeedCommand(FlySpeedPlugin _plugin) {
		plugin = _plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (command.getName().equalsIgnoreCase("flyspeed")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(prefix + "You cannot use this command from the console!");
				return true;
			}

			Player p = (Player) sender;			

			if (args.length == 0) {
				if (!p.hasPermission("flyspeed.flyspeed")) {
					p.sendMessage(prefix + noPermission);
					return true;
				}
				
				float fspeedn = p.getFlySpeed();
				float fspeed = fspeedn * 10.0F;
				p.sendMessage(prefix + ChatColor.GOLD + "Your flyspeed is: " + fspeed);
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (!p.hasPermission("flyspeed.reload")) {
						p.sendMessage(prefix + noPermission);
						return true;
					}

					plugin.reloadConfig();
					plugin.saveConfig();
					sender.sendMessage(prefix + ChatColor.AQUA + "FlySpeed reloaded!");
					return true;
				} else if (args[0].equalsIgnoreCase("info")) {
					sendPluginInfo(p);
					return true;
				} else if (args[0].equalsIgnoreCase("reset")) {
					if (!p.hasPermission("flyspeed.flyspeed")) {
						p.sendMessage(prefix + noPermission);
						return true;
					}
					p.setFlySpeed(0.1F);
					p.sendMessage(prefix + ChatColor.GREEN + "Flyspeed set to default!");
					return true;
				} else if (args[0].equalsIgnoreCase("help")) {
					if (!p.hasPermission("flyspeed.help")) {
						p.sendMessage(prefix + noPermission);
						return true;
					}

					sendHelp(p);
					return true;
				}

				Player target = Bukkit.getPlayer(args[0]);

				if (target != null) {
					if (!p.hasPermission("flyspeed.flyspeed.others")) {
						p.sendMessage(prefix + noPermission);
						return true;
					}

					float fspeedn = target.getFlySpeed();
					float fspeed = fspeedn * 10.0F;
					p.sendMessage(prefix + ChatColor.GOLD + "Flyspeed of " + args[0] + " is: " + fspeed);
					return true;
				}

				try {
					float speed = Float.valueOf(args[0]).floatValue();
					if (!p.hasPermission("flyspeed.flyspeed")) {
						p.sendMessage(String.valueOf(prefix) + noPermission);
						return true;
					}

					if (speed < 0.0F || speed > 10.0F) {
						p.sendMessage(prefix + ChatColor.RED + "Please choose a speed between 0 and 10!");
						return true;
					}

					float speed2 = speed / 10.0F;
					p.setFlySpeed(speed2);
					p.sendMessage(prefix + ChatColor.GREEN + "Flyspeed set to: " + speed);
					return true;
				} catch (NumberFormatException e) {
					p.sendMessage(prefix + ChatColor.RED + "That is not a valid speed.");
					return false;
				}
			} else if (args.length == 2) {
				if (!p.hasPermission("flyspeed.flyspeed.others")) {
					p.sendMessage(prefix + noPermission);
					return true;
				}

				Player target = Bukkit.getServer().getPlayer(args[1]);

				if (target == null) {
					p.sendMessage(prefix + ChatColor.RED + "Couldn't find player!");
					return true;
				}

				if (args[0].equalsIgnoreCase("reset")) {
					target.setFlySpeed(0.1F);
					target.sendMessage(prefix + ChatColor.GREEN + "Flyspeed set to default!");
					p.sendMessage(prefix + ChatColor.GREEN + "Flyspeed of " + target.getName() + " set to default!");
					return true;
				} else if (args[0].equalsIgnoreCase("get")) {
					float fspeedn = target.getFlySpeed();
					float fspeed = fspeedn * 10.0F;
					p.sendMessage(prefix + ChatColor.GOLD + "Flyspeed from " + target.getName() + "is: " + fspeed);
					return true;
				}

				try {
					float speed = Float.valueOf(args[0]).floatValue();
					if (speed < 0.0F || speed > 10.0F) {
						p.sendMessage(prefix + ChatColor.RED + "Please choose a speed between 0 and 10!");
						return true;
					}

					float speed2 = speed / 10.0F;
					target.setFlySpeed(speed2);
					p.sendMessage(prefix + ChatColor.GREEN + "Flyspeed of " + target.getName() + " set to: " + speed);
					target.sendMessage(prefix + ChatColor.GREEN + "Flyspeed set to: " + speed);
					return true;
				} catch (NumberFormatException e) {
					p.sendMessage(prefix + ChatColor.RED + "That is not a valid speed.");
					return true;
				}
			}

			return true;
		}

		return false;
	}

	private static void sendPluginInfo(Player p) {
		PluginDescriptionFile pdf = plugin.getDescription();
		p.sendMessage("");
		p.sendMessage(ChatColor.GOLD + "======================================================================");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.GOLD + " Version: " + pdf.getVersion());
		p.sendMessage(ChatColor.DARK_AQUA + "Author: " + ChatColor.GOLD + "AndurilUnlogic, JustBru00");
		p.sendMessage(ChatColor.DARK_AQUA + "Description: " + ChatColor.GOLD + pdf.getDescription());
		p.sendMessage(ChatColor.DARK_AQUA + "Commands: " + ChatColor.GOLD
				+ "Use '/flyspeed help' (or '/walkspeed help') to see a list of all commands");
		p.sendMessage(ChatColor.GOLD + "======================================================================");
	}

	private static void sendHelp(Player p) {
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.GOLD + " Help");
		p.sendMessage(ChatColor.GOLD + "======================================================================");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /flyspeed help-------------------Brings up this list");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /flyspeed reload-----------------Reloads the plugin.");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /flyspeed reset------------------Resets your flyspeed to the default.");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /flyspeed reset <player>----------Resets flyspeed to default.");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /flyspeed <speed>----------------Sets your own flyspeed.");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /flyspeed <speed> <player>--------Sets flyspeed for someone else");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /walkspeed help------------------Displays /walkspeed commands.");
		p.sendMessage(ChatColor.GOLD + "======================================================================");
	}
}
