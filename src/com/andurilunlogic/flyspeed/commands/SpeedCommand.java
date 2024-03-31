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

@SuppressWarnings("deprecation")
public class SpeedCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (command.getName().equalsIgnoreCase("speed")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(prefix + "You cannot use this command from the console!");
				return true;
			}

			Player p = (Player) sender;

			if (p.isFlying()) {
				if (!p.hasPermission("flyspeed.flyspeed")) {
					p.sendMessage(prefix + noPermission);
					return true;
				}

				if (args.length == 0) {
					float fspeedn = p.getFlySpeed();
					float fspeed = fspeedn * 10.0F;
					p.sendMessage(prefix + ChatColor.GOLD + "Your flyspeed is: " + fspeed);
					return true;
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reset")) {
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

						sendFlyHelp(p);
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
							p.sendMessage(prefix + noPermission);
							return true;
						}
						if (speed < 0.0F || speed > 10.0F) {
							p.sendMessage(
									String.valueOf(prefix) + ChatColor.RED + "Please choose a speed between 0 and 10!");
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
				}
				p.sendMessage(prefix + ChatColor.RED + "Usage: /speed <1-10;reset;help>");
				return true;
			}

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
				}

				if (args[0].equalsIgnoreCase("help")) {
					if (!p.hasPermission("flyspeed.help")) {
						p.sendMessage(prefix + noPermission);
						return true;
					}
					sendWalkHelp(p);
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
			}
			p.sendMessage(prefix + ChatColor.RED + "Usage: /speed <1-10;reset;help>");
		}

		return false;
	}

	private void sendWalkHelp(Player p) {
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

	private void sendFlyHelp(Player p) {
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
