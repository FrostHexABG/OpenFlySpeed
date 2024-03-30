package com.andurilunlogic.flyspeed.commands;

import static com.andurilunlogic.flyspeed.FlySpeedPlugin.noPermission;
import static com.andurilunlogic.flyspeed.FlySpeedPlugin.prefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.andurilunlogic.flyspeed.FlySpeedPlugin;

public class WalkSpeedCommand implements CommandExecutor {

	private static FlySpeedPlugin plugin;

	public WalkSpeedCommand(FlySpeedPlugin _plugin) {
		plugin = _plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (command.getName().equalsIgnoreCase("walkspeed")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(prefix + "You cannot use this command from the console!");
				return true;
			}

			Player p = (Player) sender;

			if (args.length == 0) {
				if (!p.hasPermission("flyspeed.walkspeed")) {
					p.sendMessage(prefix + noPermission);
					return true;
				}

				float wspeedn = p.getWalkSpeed();
				float wspeed = wspeedn * 10.0F;
				p.sendMessage(prefix + ChatColor.GOLD + "Your walkspeed is: " + wspeed);
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reset")) {
					if (!p.hasPermission("flyspeed.walkspeed")) {
						p.sendMessage(prefix + noPermission);
						return true;
					}
					p.setWalkSpeed(0.2F);
					p.sendMessage(prefix + ChatColor.GREEN + "Walkspeed set to default!");
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
					if (!p.hasPermission("flyspeed.walkspeed.others")) {
						p.sendMessage(prefix + noPermission);
						return true;
					}

					float wspeedn = target.getWalkSpeed();
					float wspeed = wspeedn * 10.0F;
					p.sendMessage(prefix + ChatColor.GOLD + "Walkspeed of " + args[0] + " is: " + wspeed);
					return true;
				}
				try {
					float speed = Float.valueOf(args[0]).floatValue();

					if (!p.hasPermission("flyspeed.walkspeed")) {
						p.sendMessage(prefix + noPermission);
						return true;
					}

					if (speed < 0.0F || speed > 10.0F) {
						p.sendMessage(prefix + ChatColor.RED + "Please choose a speed between 0 and 10!");
						return true;
					}

					float speed2 = speed / 10.0F;
					p.setWalkSpeed(speed2);
					p.sendMessage(prefix + ChatColor.GREEN + "Walkspeed set to: " + speed);
					return true;
				} catch (NumberFormatException e) {
					p.sendMessage(prefix + ChatColor.RED + "That is not a valid speed.");
					return false;
				}
			} else if (args.length == 2) {
				if (!p.hasPermission("flyspeed.walkspeed.others")) {
					p.sendMessage(prefix + noPermission);
					return true;
				}

				Player target = Bukkit.getServer().getPlayer(args[1]);

				if (target == null) {
					p.sendMessage(prefix + ChatColor.RED + "Couldn't find player!");
					return false;
				}

				if (args[0].equalsIgnoreCase("reset")) {
					target.setWalkSpeed(0.2F);
					target.sendMessage(prefix + ChatColor.GREEN + "Walkspeed set to default!");
					p.sendMessage(prefix + ChatColor.GREEN + "Walkspeed of " + target.getName() + " set to default!");
					return true;
				} else if (args[0].equalsIgnoreCase("get")) {
					float wspeedn = target.getWalkSpeed();
					float wspeed = wspeedn * 10.0F;
					p.sendMessage(prefix + ChatColor.GOLD + "Walkspeed from " + target.getName() + "is: " + wspeed);
					return true;
				}

				try {
					float speed = Float.valueOf(args[0]).floatValue();
					if (speed < 0.0F || speed > 10.0F) {
						p.sendMessage(prefix + ChatColor.RED + "Please choose a speed between 0 and 10!");
						return false;
					}

					float speed2 = speed / 10.0F;
					target.setWalkSpeed(speed2);
					p.sendMessage(prefix + ChatColor.GREEN + "Walkspeed of " + target.getName() + " set to: " + speed);
					target.sendMessage(prefix + ChatColor.GREEN + "Walkspeed set to: " + speed);
					return true;
				} catch (NumberFormatException e) {
					p.sendMessage(prefix + ChatColor.RED + "That is not a valid speed.");
					return false;
				}
			}

			return true;
		}

		return false;
	}

	private static void sendHelp(Player p) {
		p.sendMessage("" + ChatColor.GOLD);
		p.sendMessage(ChatColor.DARK_AQUA + "[WalkSpeed]" + ChatColor.GOLD + " Help");
		p.sendMessage(ChatColor.GOLD + "======================================================================");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /walkspeed reset--------------------Resets your walkspeed to the default.");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /walkspeed reset <player>------------Resets walkspeed to default.");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /walkspeed <speed>------------------Sets your own walkspeed.");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /walkspeed <speed> <player>----------Sets walkspeed for someone else.");
		p.sendMessage(ChatColor.DARK_AQUA + "[FlySpeed]" + ChatColor.WHITE
				+ " /flyspeed help----------------------Displays /flyspeed commands");
		p.sendMessage(ChatColor.GOLD + "======================================================================");
	}

}
