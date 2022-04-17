package com.ruserious99.mysticskulls;

import com.ruserious99.mysticskulls.commands.SkullCommands;
import com.ruserious99.mysticskulls.commands.SkullTab;
import com.ruserious99.mysticskulls.listeners.GeneralListeners;
import com.ruserious99.mysticskulls.listeners.SkullListeners;
import com.ruserious99.mysticskulls.skull_event.TestMask;
import com.ruserious99.mysticskulls.skulls.SkullsManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class MysticSkulls extends JavaPlugin {

    //To add skull
    //add new item to SkullManager
    //Make MaskEvents class that extends AbstactMysticSkull and implements Listener
    //Register your Listener here
    //add skull to methods giveCustomItem in SkullListener and RunInstalledMasks equip and unequip.
    //add skull to SkullCommands and SkullTab


    private static MysticSkulls instance;

    @Override
    public void onEnable() {
        MysticSkulls.instance = this;
        SkullsManager.init();
        this.registerCommands();
        this.registerListeners();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new GeneralListeners(), this);
        this.getServer().getPluginManager().registerEvents(new SkullListeners(), this);
        this.getServer().getPluginManager().registerEvents(new TestMask(), this);
    }

    private void registerCommands() {
        (getCommand("skull")).setTabCompleter(new SkullTab());
        (this.getCommand("skull")).setExecutor(new SkullCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MysticSkulls get() {
        return instance;
    }

}
