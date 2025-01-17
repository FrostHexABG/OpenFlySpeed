package com.andurilunlogic.flyspeed;

import com.andurilunlogic.flyspeed.listeners.OnJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.andurilunlogic.flyspeed.commands.FlyCommand;
import com.andurilunlogic.flyspeed.commands.FlySpeedCommand;
import com.andurilunlogic.flyspeed.commands.SpeedCommand;
import com.andurilunlogic.flyspeed.commands.WalkSpeedCommand;

@SuppressWarnings("deprecation")
public class FlySpeedPlugin extends JavaPlugin {
	
	 public static String prefix = null;
	 public static String noPermission = null;
	 public static String flightEnabled = null;
	 public static String flightDisabled = null;
	 private static FlySpeedPlugin instance;
	
	@Override
	public void onEnable() {
		instance = this;
		// Save default configuration file
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		// Register command executors
		getCommand("flyspeed").setExecutor(new FlySpeedCommand(this));
		getCommand("walkspeed").setExecutor(new WalkSpeedCommand());
		getCommand("speed").setExecutor(new SpeedCommand());
		getCommand("fly").setExecutor(new FlyCommand(this));

		// Register Listener(s)
		Bukkit.getPluginManager().registerEvents(new OnJoinListener(), instance);
		
		// Load Text Strings
		prefix = color(getConfig().getString("prefix"));
		noPermission = color(getConfig().getString("no-permission"));
		flightEnabled = color(getConfig().getString("flight-enabled"));
		flightDisabled = color(getConfig().getString("flight-disabled"));
		
	}

	@Override
	public void onDisable() {
		instance = null;
	}
	
	public static String color(String uncolored) {
		return ChatColor.translateAlternateColorCodes('&', uncolored);
	}

	public static FlySpeedPlugin getInstance() {
		return instance;
	}
}
