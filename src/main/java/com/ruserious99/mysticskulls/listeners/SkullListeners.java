package com.ruserious99.mysticskulls.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.ruserious99.mysticskulls.skulls.SkullsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkullListeners implements Listener {
    private ItemStack giveCustomItem(String whatSkull) {
        ItemStack giveBackCustom;
        switch (whatSkull) {
            case "testSkull":
                giveBackCustom = new ItemStack(SkullsManager.test);
                return giveBackCustom;
        }
        return null;
    }
    @EventHandler
    public void onArmorClicked(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();
        ItemStack itemCursor = event.getWhoClicked().getItemOnCursor();
        ItemStack clickedItem = event.getCurrentItem();

        if (event.isRightClick() && event.isShiftClick() && event.getCurrentItem().getType().name().endsWith("_HELMET")) {
            event.setCancelled(true);
            return;
        }

        if (clickedItem == null) {
            return;
        }
        String stringName = clickedItem.getType().name();


        //create multi mask
        if (isMask(itemCursor) && isMask(clickedItem) && (!isMultiMask(clickedItem))) {
            if (itemCursor.getItemMeta().getDisplayName().equals(clickedItem.getItemMeta().getDisplayName())) {
                player.sendMessage(ChatColor.RED + "invalid multi Skull");
                event.setCancelled(true);
                return;
            }
            if (isEquippedMultiMask(itemCursor) || isEquippedMultiMask(clickedItem)) {
                player.sendMessage("Already have 2 Skulls Equipped!");
                return;
            }
            ItemStack multi = createMultiMask(itemCursor, clickedItem, player, slot);
            player.getItemOnCursor().setAmount(0);
            player.getInventory().clear(slot);
            player.getInventory().setItem(slot, multi);
            return;
        }

        //adding to helmet
        if ((stringName.endsWith("_HELMET") && (!isMasked(clickedItem)) && !isMultiMask(clickedItem) && (isMask(itemCursor) || isMultiMask(itemCursor)))) {
            if (isMultiMask(itemCursor)) {
                String toSplit = SkullsManager.getPersistentData().getCustomDataTag(itemCursor, "multiSkull");
                String[] splitted = toSplit.split(",");
                equipSwitch(itemCursor, clickedItem, splitted[0], splitted[1], player, slot);

            } else {
                String runEquip = SkullsManager.getPersistentData().getCustomDataTag(itemCursor, "skull");
                equipSwitch(itemCursor, clickedItem, runEquip, player, slot);
            }
            return;
        }

        //removing multiSkull from helmet
        if (isEquippedMultiMask(clickedItem) && event.isRightClick()) {
            String toSplit = SkullsManager.getPersistentData().getCustomDataTag(clickedItem, "equippedMultiSkull");
            String[] splitted = toSplit.split(",");
            player.getInventory().setItem(slot, createNewHelmet(clickedItem, player));
            player.setItemOnCursor(createMultiMask(splitted[0], splitted[1]));
            return;
        }

        //splitting multiSkull
        if ((isMasked(clickedItem) || isMultiMask(clickedItem)) && event.isRightClick()) {
            if (isMultiMask(clickedItem)) {
                String toSplit = SkullsManager.getPersistentData().getCustomDataTag(clickedItem, "multiSkull");
                String[] splitted = toSplit.split(",");
                player.getInventory().setItem(slot, giveCustomItem(splitted[1]));
                player.setItemOnCursor(giveCustomItem(splitted[0]));
            } else {
                equipSwitch(clickedItem, player, slot);
            }
        }
    }

    private boolean isEquippedMultiMask(ItemStack item) {
        String isMulti = SkullsManager.getPersistentData().getCustomDataTag(item, "equippedMultiSkull");
        if (isMulti != null) {
            return isMulti.contains(",");
        }
        return false;
    }

    private boolean isMultiMask(ItemStack item) {
        String isMulti = SkullsManager.getPersistentData().getCustomDataTag(item, "multiSkull");
        if (isMulti != null) {
            return isMulti.contains("Skull");
        }
        return false;
    }

    private boolean isMask(ItemStack item) {
        String isSkull = SkullsManager.getPersistentData().getCustomDataTag(item, "skull");
        if (isSkull != null) {
            return isSkull.contains("Skull");
        }
        return false;
    }

    private boolean isMasked(ItemStack item) {
        String isSkull = SkullsManager.getPersistentData().getCustomDataTag(item, "equippedSkull");
        if (isSkull != null) {
            return isSkull.contains("Skull");
        }
        return false;
    }

    //create multiSkull helmet and remove inventory items
    private void equipSwitch(ItemStack itemCursor, ItemStack clickedItem, String mask1, String mask2, Player player, int slot) {
        player.getItemOnCursor().setAmount(0);
        player.getInventory().clear(slot);
        player.getInventory().setItem(slot, createMultiMaskedHelmet(itemCursor, clickedItem, mask1, mask2, player, slot));
    }

    //create Skull helmet and remove inventory items
    private void equipSwitch(ItemStack itemCursor, ItemStack item, String switchVar, Player player, int slot) {
        player.getItemOnCursor().setAmount(0);
        player.getInventory().clear(slot);
        player.getInventory().setItem(slot, createMaskedHelmet(itemCursor, item, player, switchVar));
    }

    //split Skulls and return inventory items
    private void equipSwitch(ItemStack item, Player player, int slot) {
        String whatSkull = SkullsManager.getPersistentData().getCustomDataTag(item, "equippedSkull");
        player.getInventory().setItem(slot, createNewHelmet(item, player));
        player.setItemOnCursor(giveCustomItem(whatSkull));
    }

    private ItemStack createNewHelmet(ItemStack item, Player player) {

        ItemStack itemNew = item.clone();
        ItemMeta meta = itemNew.getItemMeta();

        List<String> loreToChange = new ArrayList<>((item.getItemMeta().getLore()));

        if (item.getEnchantments().isEmpty()) {
            loreToChange.clear();
        } else {
            int enchSize = item.getEnchantments().size();
            ArrayList<String> ench = new ArrayList<>();
            for(int i = 0; i<enchSize; i++) {
                ench.add(loreToChange.get(i));
            }
            loreToChange.clear();
            for(int i = 0; i<enchSize; i++) {
                loreToChange.add(ench.get(i));
            }
        }

      /*  String nbt = SkullsManager.getPersistentData().getCustomDataTag(item, "holyWhiteScroll");
        String nbtApplied = SkullsManager.getPersistentData().getCustomDataTag(item, "nbtApplied");
        if(nbtApplied.equals("true")){
            loreToChange.add(" ");
            switch (nbt) {
                case "first":
                    loreToChange.add(ChatColor.RED + "[" + ChatColor.WHITE + "First" + ChatColor.RED + "]" + ChatColor.WHITE + " Holy White Scroll Applied" );
                    break;
                case "second":
                    loreToChange.add(ChatColor.RED + "[" + ChatColor.WHITE + "Second" + ChatColor.RED + "]" + ChatColor.WHITE + " Holy White Scroll Applied" );
                    break;
                case "last":
                    loreToChange.add(ChatColor.RED + "[" + ChatColor.WHITE + "Last" + ChatColor.RED + "]" + ChatColor.WHITE + " Holy White Scroll Applied" );
                    break;
            }
        }*/


        List<String> lore = new ArrayList<>(loreToChange);
        meta.setLore(lore);
        itemNew.setItemMeta(meta);
        ItemStack itemRemoveSkull = itemNew;
        itemRemoveSkull = SkullsManager.getPersistentData().removeCustomDataTag(itemRemoveSkull, "equippedSkull");
        itemRemoveSkull =SkullsManager.getPersistentData().removeCustomDataTag(itemRemoveSkull, "equippedMultiSkull");

        return itemRemoveSkull;
    }

    private ItemStack createMaskedHelmet(ItemStack itemCursor, ItemStack item, Player player, String var) {
        ItemStack itemNew = item.clone();
        ItemMeta meta = itemNew.getItemMeta();
        List<String> lore = null;

        if (itemNew.getItemMeta() != null) {
            if (itemNew.getItemMeta().getLore() != null) {
                lore = new ArrayList<>((itemNew.getItemMeta().getLore()));
            } else {
                lore = new ArrayList<>();
            }
        }
        assert lore != null;
        lore.add(" ");
        lore.add(ChatColor.DARK_PURPLE + "Helmet has the Power Of:");
        lore.add(ChatColor.DARK_RED + (itemCursor.getItemMeta()).getDisplayName());
        meta.setLore(lore);

        itemNew.setItemMeta(meta);
        ItemStack itemSkull = itemNew;
        itemSkull = SkullsManager.getPersistentData().setCustomDataTag(itemSkull, "equippedSkull", var);
        return itemSkull;
    }


    private ItemStack createMultiMask(String skull1, String skull2) {
        String NBTString = skull1 + "," + skull2;
        ItemStack MultiSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) MultiSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "MULTI SKULL");

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "This Multi Skull has the Power of:");
        lore.add(ChatColor.DARK_RED + skull1.toUpperCase());
        lore.add(ChatColor.DARK_RED + skull2.toUpperCase());
        lore.add("");
        lore.add(ChatColor.GRAY + "Attach this skull to any helmet");
        lore.add(ChatColor.GRAY + "to give it a visual override!");
        lore.add("");
        lore.add(ChatColor.GRAY + "To equip, place this Skull on a helmet.");
        lore.add(ChatColor.GRAY + "To remove, right-click helmet while attached.");
        meta.setLore(lore);

        MultiSkull.setItemMeta(meta);

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
        profile.getProperties().add(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA3Y2U0MzczNGNiZWZiYjNkYTAzZjlhYzFmMDFhM2RmNDU2Y2VlMjMxNTUwYmQyZGQ0MjU4NTU5NGY5In19fQ=="));


        MultiSkull.setItemMeta(meta);
        ItemStack multiSkull = MultiSkull;
        multiSkull = SkullsManager.getPersistentData().setCustomDataTag(multiSkull, "multiSkull", NBTString);
        return multiSkull;
    }

    private ItemStack createMultiMask(ItemStack itemCursor, ItemStack clickedItem, Player player, int slot) {
        String skull1 = SkullsManager.getPersistentData().getCustomDataTag(itemCursor, "skull");
        String skull2 = SkullsManager.getPersistentData().getCustomDataTag(clickedItem, "skull");
        String NBTString = skull1 + "," + skull2;

        ItemStack MultiSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) MultiSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "MULTI SKULLS");

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.DARK_PURPLE + "This Multi Skull has the Power of:");
        lore.add(ChatColor.DARK_RED + skull1.toUpperCase());
        lore.add(ChatColor.DARK_RED + skull2.toUpperCase());
        lore.add("");
        lore.add(ChatColor.GRAY + "Attach this skull to any helmet");
        lore.add(ChatColor.GRAY + "to give it a visual override!");
        lore.add("");
        lore.add(ChatColor.GRAY + "To equip, place this skull on a helmet.");
        lore.add(ChatColor.GRAY + "To remove, right-click helmet while attached.");
        meta.setLore(lore);

        MultiSkull.setItemMeta(meta);

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
        profile.getProperties().add(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA3Y2U0MzczNGNiZWZiYjNkYTAzZjlhYzFmMDFhM2RmNDU2Y2VlMjMxNTUwYmQyZGQ0MjU4NTU5NGY5In19fQ=="));

        MultiSkull.setItemMeta(meta);
        ItemStack multiSkull = MultiSkull;
        multiSkull = SkullsManager.getPersistentData().setCustomDataTag(multiSkull, "multiSkull", NBTString);
        return multiSkull;
    }

    private ItemStack createMultiMaskedHelmet(ItemStack itemCursor, ItemStack item, String skull1, String skull2, Player player, int slot) {

        ItemStack itemNew = item.clone();
        ItemMeta meta = itemNew.getItemMeta();
        List<String> lore = null;

        if (itemNew.getItemMeta() != null) {
            if (itemNew.getItemMeta().getLore() != null) {
                lore = new ArrayList<>((itemNew.getItemMeta().getLore()));
            } else {
                lore = new ArrayList<>();
            }
        }
        lore.add(" ");
        lore.add(ChatColor.DARK_PURPLE + "Helmet has the Power Of:");
        lore.add(ChatColor.DARK_RED + skull1.toUpperCase());
        lore.add(ChatColor.DARK_RED + skull2.toUpperCase());
        meta.setLore(lore);

        itemNew.setItemMeta(meta);
        ItemStack itemSkulled = itemNew;
        itemSkulled = SkullsManager.getPersistentData().setCustomDataTag(itemSkulled, "equippedMultiSkull", (skull1 + "," + skull2));
        return itemSkulled;
    }
}

