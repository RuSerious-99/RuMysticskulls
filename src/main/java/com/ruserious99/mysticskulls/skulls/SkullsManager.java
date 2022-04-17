package com.ruserious99.mysticskulls.skulls;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SkullsManager {

    private static HashMap<ItemStack, Boolean> skulls;
    private static PersistentData persistentData;
    private static RunInstalledSkulls runInstalledMasks;

    public static ItemStack test;

    public static void init() {

        skulls = new HashMap<>();
        persistentData = new PersistentData();
        runInstalledMasks = new RunInstalledSkulls();

        createTestSkull();

    }


    private static void createTestSkull() {
        //Bukkit.getConsoleSender().sendMessage("SkullsManager: start create testSkull");
        ItemStack testSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) testSkull.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "TEST SKULL");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE +" ");
        lore.add("This Skull will Let You Fly!!!");
        lore.add(ChatColor.GOLD +" ");
        lore.add("This is a test Skull");
        lore.add("Put it on to Enjoy the look");
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Attach this Skull to any helmet to Enjoy the Mystical Powers");
        lore.add(ChatColor.GRAY + "To give it a visual override Attach with no Helmet");
        lore.add("");
        lore.add(ChatColor.GRAY + "To equip, place this Skull on a helmet");
        lore.add(ChatColor.GRAY + "To remove, right-click helmet while attached");
        meta.setLore(lore);

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
        profile.getProperties().add(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNjM2Y3ODFjOTIzYTI4ODdmMTRjMWVlYTExMDUwMTY2OTY2ZjI2MDI1Nzg0MDFmMTQ1MWU2MDk3Yjk3OWRmIn19fQ=="));
        meta.setPlayerProfile(profile);

        testSkull.setAmount(1);
        testSkull.setItemMeta(meta);
        test = testSkull;
        test = persistentData.setCustomDataTag(testSkull, "skull", "testSkull");

        skulls.put(test, false);
        //Bukkit.getConsoleSender().sendMessage("SkullsManager: create testskull Itemstack " + skulls.size());

    }

    public static PersistentData getPersistentData() {return persistentData;}
    public static RunInstalledSkulls getRunInstalledSkulls() { return runInstalledMasks; }
    public static HashMap<ItemStack, Boolean> getSkulls() { return skulls; }
}

