package com.andurilunlogic.flyspeed.commands;

import static com.andurilunlogic.flyspeed.FlySpeedPlugin.flightDisabled;
import static com.andurilunlogic.flyspeed.FlySpeedPlugin.flightEnabled;
import static com.andurilunlogic.flyspeed.FlySpeedPlugin.noPermission;
import static com.andurilunlogic.flyspeed.FlySpeedPlugin.prefix;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.andurilunlogic.flyspeed.FlySpeedPlugin;

public class FlyCommand implements CommandExecutor {

	private static FlySpeedPlugin plugin;

	public FlyCommand(FlySpeedPlugin _plugin) {
		plugin = _plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

		if (command.getName().equalsIgnoreCase("fly")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(prefix + "You cannot use this command from the console!");
				return true;
			}

			Player p = (Player) sender;

			if (args.length == 0) {
				if (!p.hasPermission("flyspeed.fly")) {
					p.sendMessage(prefix + noPermission);
					return true;
				}

				if (p.getAllowFlight()) {
					p.setFlySpeed(0.1F);
					p.setAllowFlight(false);

					if (flightDisabled.contains("%player%")) {
						flightDisabled = flightDisabled.replace("%player%", p.getName());
					}

					p.sendMessage(prefix + flightDisabled);
					return true;
				} else {
					for (String s : plugin.getConfig().getConfigurationSection("default-speed").getKeys(false)) {
						if (p.hasPermission(plugin.getConfig().getString("default-speed." + s + ".permission"))
								&& !p.isOp()) {
							p.sendMessage(plugin.getConfig().getString("default-speed." + s + ".permission"));
							float speed = (float) plugin.getConfig().getDouble("default-speed." + s + ".speed");

							try {
								if (speed < 0.0F || speed > 10.0F) {
									p.sendMessage(prefix + "speed specified in the config cannot be lower than 0!");
									return true;
								}

								float fspeed = speed / 10.0F;
								p.setAllowFlight(true);
								p.setFlySpeed(fspeed);

								if (flightEnabled.contains("%player%")) {
									flightEnabled = flightEnabled.replace("%player%", p.getName());
								}

								p.sendMessage(prefix + flightEnabled);
								return true;
							} catch (NumberFormatException e) {
								p.sendMessage(prefix + "speed specified in the config needs to be a number!");
							}
						}
					}

					p.setAllowFlight(true);
					if (flightEnabled.contains("%player%")) {
						flightEnabled = flightEnabled.replace("%player%", p.getName());
					}
					p.sendMessage(prefix + flightEnabled);
					return true;
				}
			} else if (args.length == 1) {
				if (!p.hasPermission("flyspeed.fly.others")) {
					p.sendMessage(prefix + noPermission);
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

					if (flightDisabled.contains("%player%")) {
						flightDisabled = flightDisabled.replace("%player%", target.getName());
					}

					p.sendMessage(prefix + flightDisabled);

					if (p != target)
						target.sendMessage(prefix + flightDisabled);
				} else {
					for (String s : plugin.getConfig().getConfigurationSection("default-speed").getKeys(false)) {
						if (target.hasPermission(plugin.getConfig().getString("default-speed." + s + ".permission"))
								&& !target.isOp()) {
							float speed = (float) plugin.getConfig().getDouble("default-speed." + s + ".speed");

							try {
								if (speed < 0.0F || speed > 10.0F) {
									p.sendMessage(prefix + "speed specified in the config cannot be lower than 0!");
									return true;
								}

								float fspeed = speed / 10.0F;
								target.setAllowFlight(true);
								target.setFlySpeed(fspeed);

								if (flightEnabled.contains("%player%")) {
									flightEnabled = flightEnabled.replace("%player%", target.getName());
								}

								p.sendMessage(prefix + flightEnabled);

								if (p != target) {
									target.sendMessage(prefix + flightEnabled);
								}
								return true;
							} catch (NumberFormatException e) {
								p.sendMessage(prefix + "speed specified in the config needs to be a number!");
								return true;
							}
						}
					}

					target.setAllowFlight(true);

					if (flightEnabled.contains("%player%")) {
						flightEnabled = flightEnabled.replace("%player%", target.getName());
					}

					p.sendMessage(prefix + flightEnabled);

					if (p != target) {
						target.sendMessage(prefix + flightEnabled);
					}
					return true;
				}
			} else {
				p.sendMessage("/fly [player]");
				return true;
			}
		}

		return false;
	}

}
