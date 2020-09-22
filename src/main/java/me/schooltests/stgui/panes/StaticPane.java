package me.schooltests.stgui.panes;

import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.data.GUIPosition;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StaticPane extends Pane {
    private GUIPosition position;
    private Map<GUIPosition, GUIItem> items = new HashMap<>();

    private int rows;
    private int cols;

    public StaticPane(int rows, int cols, GUIPosition position) {
        this.rows = rows;
        this.cols = cols;
        this.position = position;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public void setDimensions(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public void setPosition(GUIPosition position) {
        this.position = position;
    }

    public GUIPosition getPosition() {
        return position;
    }

    public void setItem(GUIPosition position, GUIItem item) {
        this.items.put(position, item);
    }

    public void setItem(int row, int col, GUIItem item) {
        this.items.put(new GUIPosition(row, col), item);
    }

    public void setItems(Map<GUIPosition, GUIItem> items) {
        this.items = items;
    }

    public void setRawItems(Map<GUIPosition, ItemStack> items) {
        for (Map.Entry<GUIPosition, ItemStack> entry : items.entrySet()) {
            this.items.put(entry.getKey(), new GUIItem(entry.getValue(), (click) -> {}));
        }
    }

    public Map<GUIPosition, GUIItem> getItems() {
        return items;
    }
}
