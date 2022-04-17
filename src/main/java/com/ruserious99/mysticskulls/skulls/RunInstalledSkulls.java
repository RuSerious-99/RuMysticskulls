package com.ruserious99.mysticskulls.skulls;

import com.ruserious99.mysticskulls.skull_event.TestMask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RunInstalledSkulls {
    public void equipSkull(String mask, Player player) {
        //Bukkit.getConsoleSender().sendMessage("runInstalledMask: " + mask);

        switch (mask) {


            case "testSkull":
                TestMask testMask = new TestMask();
                testMask.onEquip(player);
                break;


            default:
        }
    }

    public void unEquipSkull(String mask, Player player) {
        switch (mask) {


            case "testSkull":
                TestMask testMask = new TestMask();
                testMask.onUnequip(player);
                break;


            default:
        }
    }
}
