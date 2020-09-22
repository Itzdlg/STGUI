package me.schooltests.stgui.data;

import me.schooltests.stgui.ItemClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GUIItem extends DataHolder {
    private ItemStack item;
    private Consumer<ItemClickEvent> clickEvent;

    public GUIItem(ItemStack item, Consumer<ItemClickEvent> clickEvent) {
        this.item = item;
        this.clickEvent = clickEvent;
    }

    public GUIItem(ItemStack item) {
        this(item, null);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Consumer<ItemClickEvent> getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(Consumer<ItemClickEvent> clickEvent) {
        this.clickEvent = clickEvent;
    }
}
