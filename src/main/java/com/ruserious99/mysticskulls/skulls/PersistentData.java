package com.ruserious99.mysticskulls.skulls;

import com.ruserious99.mysticskulls.MysticSkulls;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PersistentData {
    public ItemStack setCustomDataTag(final ItemStack itemStack, final String key, final String value) {
        if (itemStack == null) {
            return null;
        }
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        final PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(new NamespacedKey(MysticSkulls.get(), key), PersistentDataType.STRING)) {
            data.set(new NamespacedKey(MysticSkulls.get(), key), PersistentDataType.STRING, value);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public String getCustomDataTag(final ItemStack itemStack, final String key) {
        if (itemStack == null) {
            return null;
        }
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        final PersistentDataContainer data = meta.getPersistentDataContainer();
        if (data.has(new NamespacedKey(MysticSkulls.get(), key), PersistentDataType.STRING)) {
            return data.get(new NamespacedKey(MysticSkulls.get(), key), PersistentDataType.STRING);
        }
        return null;
    }

    public ItemStack removeCustomDataTag(final ItemStack itemStack, final String key) {
        if (itemStack == null) {
            return null;
        }
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        final PersistentDataContainer data = meta.getPersistentDataContainer();
        if (data.has(new NamespacedKey(MysticSkulls.get(), key), PersistentDataType.STRING)) {
            data.remove(new NamespacedKey(MysticSkulls.get(), key));
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
}
