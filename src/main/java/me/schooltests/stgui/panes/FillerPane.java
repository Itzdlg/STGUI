package me.schooltests.stgui.panes;

import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.data.GUIPosition;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FillerPane extends Pane {
    private GUIPosition position;
    private List<GUIItem> items = new ArrayList<>();

    private int rows;
    private int cols;

    public FillerPane(int rows, int cols, GUIPosition position) {
        this.rows = rows;
        this.cols = cols;
        this.position = position;
    }

    @Override
    public int getPriority() {
        return 0;
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

    public void addItems(GUIItem... items) {
        this.items.addAll(Arrays.asList(items));
    }

    public void addRawItems(ItemStack... items) {
        for (ItemStack item : items) {
            this.items.add(new GUIItem(item, (click) -> {}));
        }
    }

    public void setItems(List<GUIItem> items) {
        this.items = items;
    }

    public void setRawItems(List<ItemStack> items) {
        this.items.clear();
        for (ItemStack item : items) {
            this.items.add(new GUIItem(item, (click) -> {}));
        }
    }

    public List<GUIItem> getItems() {
        return items;
    }
}
