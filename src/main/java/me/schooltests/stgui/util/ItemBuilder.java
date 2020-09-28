package me.schooltests.stgui.util;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        Material type;
        this.item = new ItemStack(itemStack);
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder data(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    public ItemBuilder data(DyeColor color) {
        item.setDurability(color.getWoolData());
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        for (String l : lore) itemLore.add(ChatColor.translateAlternateColorCodes('&', l));

        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder itemFlags(ItemFlag... flags) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flags);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack get() {
        return item;
    }
}
