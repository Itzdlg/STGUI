package me.schooltests.stgui.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DefaultItems {
    public static final ItemStack GRAY_FILLER = new ItemBuilder(Material.STAINED_GLASS_PANE).data(DyeColor.SILVER).name("&c").get();
    public static final ItemStack BACKWARD = new ItemBuilder(Material.ARROW).name("&eGo Back").get();
    public static final ItemStack FORWARD = new ItemBuilder(Material.ARROW).name("&eGo Forward").get();

    public static final ItemStack SAVE = new ItemBuilder(Material.WOOL).data(DyeColor.LIME).name("&aSave").get();
    public static final ItemStack CANCEL = new ItemBuilder(Material.WOOL).data(DyeColor.RED).name("&cCancel").get();
    public static final ItemStack BARRIER = new ItemBuilder(Material.BARRIER).name("&c").get();
}
