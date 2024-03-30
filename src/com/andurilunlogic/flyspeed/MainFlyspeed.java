package com.andurilunlogic.flyspeed;

import org.bukkit.plugin.java.JavaPlugin;

public class MainFlyspeed extends JavaPlugin {
  public void onEnable() {
    getConfig().options().copyDefaults(true);
    saveConfig();
    FlyspeedCommands fspeedcom = new FlyspeedCommands();
    getCommand("flyspeed").setExecutor(fspeedcom);
    getCommand("walkspeed").setExecutor(fspeedcom);
    getCommand("speed").setExecutor(fspeedcom);
    getCommand("fly").setExecutor(fspeedcom);
  }
  
  public void onDisable() {}
}

