package me.schooltests.stgui.panes;

import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.data.GUIPosition;
import me.schooltests.stgui.guis.GUI;
import me.schooltests.stgui.util.Util;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final DrawHandler handler = new DrawHandler() {
        @Override
        public List<Integer> drawPane(GUI gui) {
            List<Integer> list = new ArrayList<>();
            if (items.isEmpty()) return list;

            List<Integer> slots = Util.getSlots(StaticPane.this, gui.getRows(), gui.getCols());
            for (int i : slots) {
                GUIPosition pos = Util.getPanePositionFromSlot(StaticPane.this, i, gui.getRows(), gui.getCols());
                if (items.containsKey(pos)) {
                    GUIItem item = items.get(pos);
                    gui.getInventory().setItem(i, item.getItem());
                    list.add(i);
                }
            }

            return list;
        }

        @Override
        public void setItem(GUI gui, GUIItem item, int guiSlot, int paneSlot) {
            items.put(Util.getPanePositionFromSlot(StaticPane.this, guiSlot, gui.getRows(), gui.getCols()), item);
        }

        @Override
        public GUIItem getItem(GUI gui, int guiSlot, int paneSlot) {
            return items.get(Util.getPanePositionFromSlot(StaticPane.this, guiSlot, gui.getRows(), gui.getCols()));
        }
    };

    @Override
    public DrawHandler getHandler() {
        return handler;
    }
}
