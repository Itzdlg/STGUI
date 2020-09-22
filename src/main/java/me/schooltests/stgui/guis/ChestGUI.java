package me.schooltests.stgui.guis;

import me.schooltests.stgui.STGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestGUI extends GUI {
    public ChestGUI(JavaPlugin plugin, String title, int rows) {
        STGUI.setPlugin(plugin);
        this.title = title;

        this.rows = rows;
        this.cols = 9;

        this.type = InventoryType.CHEST;
    }

    public ChestGUI(JavaPlugin plugin) {
        this(plugin, "", 6);
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
