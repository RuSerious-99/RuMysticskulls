package com.ruserious99.mysticskulls.skull_event;

import com.ruserious99.mysticskulls.AbstactMysticSkull;
import com.ruserious99.mysticskulls.MysticSkulls;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

public class TestMask extends AbstactMysticSkull implements Listener {

    @Override
    public void onEquip(Player player) {
       // Bukkit.getConsoleSender().sendMessage("TestMask: set fly true ");
        player.setMetadata("testMaskOn", new FixedMetadataValue(MysticSkulls.get(), System.currentTimeMillis()));
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    @Override
    public void onUnequip(Player player) {
        //Bukkit.getConsoleSender().sendMessage("TestMask: set fly false ");
        player.removeMetadata("testMaskOn", MysticSkulls.get());
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}