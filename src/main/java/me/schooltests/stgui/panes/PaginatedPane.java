package me.schooltests.stgui.panes;

import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.data.GUIPosition;
import me.schooltests.stgui.guis.GUI;
import me.schooltests.stgui.util.Util;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PaginatedPane extends Pane {
    private GUIPosition position;
    private List<GUIItem> items = new ArrayList<>();
    private int page = 0;

    private int rows;
    private int cols;

    public PaginatedPane(int rows, int cols, GUIPosition position) {
        this.rows = rows;
        this.cols = cols;
        this.position = position;
    }

    @Override
    public int getPriority() {
        return 1;
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

    public List<GUIItem> getPageItems(int page) {
        int slots = rows * cols;
        if (items == null || items.isEmpty() || items.size() < (slots * page)) return Collections.emptyList();

        int start = slots * page;
        int end = start + slots;
        if (items.size() < end) end = items.size();

        List<GUIItem> items = new ArrayList<>();
        for (int i = start; i < end; i++)
            items.add(this.items.get(i));

        return items;
    }

    public void forward() {
        if (hasNext()) {
            page++;
        }
    }

    public void backward() {
        if (hasLast()) {
            page--;
        }
    }

    public boolean hasNext() {
        return items.size() >= (rows * cols * (page + 1));
    }

    public boolean hasLast() {
        return page >= 1;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private final DrawHandler handler = new DrawHandler() {
        @Override
        public List<Integer> drawPane(GUI gui) {
            List<Integer> list = new ArrayList<>();
            if (items.isEmpty()) return list;

            List<Integer> slots = Util.getSlots(PaginatedPane.this, gui.getRows(), gui.getCols());
            List<GUIItem> pageItems = getPageItems(page);
            for (int i : slots) {
                if (pageItems.size() <= slots.indexOf(i)) break;
                GUIPosition pos = Util.getPanePositionFromSlot(PaginatedPane.this, i, gui.getRows(), gui.getCols());
                gui.getInventory().setItem(i, pageItems.get(slots.indexOf(i)).getItem());
                list.add(i);
            }

            return list;
        }

        @Override
        public void setItem(GUI gui, GUIItem item, int guiSlot, int paneSlot) {
            items.set(paneSlot + (rows * cols * page), item);
        }

        @Override
        public GUIItem getItem(GUI gui, int guiSlot, int paneSlot) {
            return getPageItems(page).get(paneSlot);
        }
    };

    @Override
    public DrawHandler getHandler() {
        return handler;
    }
}
