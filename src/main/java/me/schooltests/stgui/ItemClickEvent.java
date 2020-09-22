package me.schooltests.stgui;

import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.guis.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;

public class ItemClickEvent extends Event implements Cancellable {
    private final Player player;
    private final GUI gui;
    private GUIItem item;
    private final ClickType clickType;
    private final int slot;
    private boolean cancelled;

    public ItemClickEvent(Player player, GUI gui, GUIItem item, ClickType clickType, int slot) {
        this.player = player;
        this.gui = gui;
        this.item = item;
        this.clickType = clickType;
        this.slot = slot;
        this.cancelled = true;
    }

    public Player getPlayer() {
        return player;
    }

    public GUI getGui() {
        return gui;
    }

    public GUIItem getItem() {
        return item;
    }

    public void setItem(GUIItem item) {
        this.item = item;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public int getSlot() {
        return slot;
    }

    private static HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
