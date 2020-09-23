package me.schooltests.stgui.panes;

import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.data.GUIPosition;
import me.schooltests.stgui.guis.GUI;
import me.schooltests.stgui.util.Util;
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

    private final DrawHandler handler = new DrawHandler() {
        @Override
        public List<Integer> drawPane(GUI gui) {
            List<Integer> list = new ArrayList<>();
            int pos = 0;
            for (int i : Util.getSlots(FillerPane.this, gui.getRows(), gui.getCols())) {
                if (items.size() <= pos)
                    pos = 0;
                GUIItem item = items.get(pos);

                gui.getInventory().setItem(i, item.getItem());
                list.add(i);
                pos++;
            }

            return list;
        }

        @Override
        public void setItem(GUI gui, GUIItem item, int guiSlot, int paneSlot) {
            items.set(paneSlot % items.size(), item);
        }

        @Override
        public GUIItem getItem(GUI gui, int guiSlot, int paneSlot) {
            return items.get(paneSlot % items.size());
        }
    };

    @Override
    public DrawHandler getHandler() {
        return handler;
    }
}
