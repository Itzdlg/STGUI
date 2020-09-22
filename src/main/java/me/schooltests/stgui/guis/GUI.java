package me.schooltests.stgui.guis;

import me.schooltests.stgui.STGUI;
import me.schooltests.stgui.data.DataHolder;
import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.data.GUIPosition;
import me.schooltests.stgui.util.Util;
import me.schooltests.stgui.panes.FillerPane;
import me.schooltests.stgui.panes.PaginatedPane;
import me.schooltests.stgui.panes.Pane;
import me.schooltests.stgui.panes.StaticPane;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class GUI extends DataHolder {
    private final Map<Pane, List<Integer>> paneSlots = new HashMap<>();
    private final Map<Integer, Pane> slotToPane = new HashMap<>();
    protected Inventory inventory;
    protected InventoryType type = InventoryType.CHEST;
    protected Set<Player> viewers = new HashSet<>();
    protected String title = "";
    protected int rows = 6;
    protected int cols = 9;

    protected BiConsumer<InventoryCloseEvent, GUI> closeEvent;
    protected Map<Integer, Set<Pane>> panes = new HashMap<>();

    public Set<Player> getViewers() {
        return viewers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void open(Player player) {
        viewers.add(player);

        draw();
        STGUI.setOpenedGUI(player, this);
    }

    public void addPane(Pane pane, int priority) {
        Set<Pane> set = panes.containsKey(priority) ? panes.get(priority) : new HashSet<>();
        set.add(pane);
        panes.put(priority, set);
    }

    public void addPane(Pane pane) {
        addPane(pane, pane.getPriority());
    }

    public void removePane(Pane pane) {
        for (Map.Entry<Integer, Set<Pane>> entry : panes.entrySet())
            entry.getValue().remove(pane);
    }

    public Map<Integer, Set<Pane>> getPaneMap() {
        return Collections.unmodifiableMap(panes);
    }

    public void setCloseEvent(BiConsumer<InventoryCloseEvent, GUI> closeEvent) {
        this.closeEvent = closeEvent;
    }

    public BiConsumer<InventoryCloseEvent, GUI> getCloseEvent() {
        return closeEvent;
    }

    public void draw() {
        Set<Player> toReopen = new HashSet<>();
        boolean reopen = false;

        paneSlots.clear();
        slotToPane.clear();
        if (inventory != null) inventory.clear();
        if (inventory == null || inventory.getSize() / cols != rows || !inventory.getTitle().equals(getTitle())) {
            if (type == InventoryType.CHEST) inventory = Bukkit.createInventory(null, rows * 9, getTitle());
            else inventory = Bukkit.createInventory(null, type, getTitle());
            toReopen = new HashSet<>(viewers);
            reopen = true;

            getViewers().forEach(HumanEntity::closeInventory);
        }

        for (int priority : getPaneMap().keySet().stream().sorted().collect(Collectors.toList())) {
            Set<Pane> set = getPaneMap().get(priority);
            for (Pane pane : set) {
                drawPane(pane);
            }
        }

        if (reopen && !toReopen.isEmpty()) {
            for (Player p : toReopen) {
                p.openInventory(inventory);
            }
        }
    }

    private void drawPane(Pane pane) {
        if (pane instanceof FillerPane) {
            FillerPane fillerPane = (FillerPane) pane;
            if (fillerPane.getItems().isEmpty()) return;

            int pos = 0;
            for (int i : Util.getSlots(fillerPane, rows, cols)) {
                if (fillerPane.getItems().size() <= pos)
                    pos = 0;
                GUIItem item = fillerPane.getItems().get(pos);

                inventory.setItem(i, item.getItem());
                addSlotToCache(pane, i);
                pos++;
            }
        } else if (pane instanceof StaticPane) {
            StaticPane staticPane = (StaticPane) pane;
            if (staticPane.getItems().isEmpty()) return;

            List<Integer> slots = Util.getSlots(staticPane, rows, cols);
            for (int i : slots) {
                GUIPosition pos = getPanePositionFromSlot(pane, i); 
                if (staticPane.getItems().containsKey(pos)) {
                    GUIItem item = staticPane.getItems().get(pos);
                    inventory.setItem(i, item.getItem()); 
                    addSlotToCache(pane, i); 
                }
            }
        } else if (pane instanceof PaginatedPane) {
            PaginatedPane paginatedPane = (PaginatedPane) pane;
            if (paginatedPane.getItems().isEmpty()) return;

            List<Integer> slots = Util.getSlots(paginatedPane, rows, cols);
            List<GUIItem> pageItems = paginatedPane.getPageItems(paginatedPane.getPage());
            for (int i : slots) { 
                if (pageItems.size() <= slots.indexOf(i)) break; 
                GUIPosition pos = getPanePositionFromSlot(pane, i); 
                inventory.setItem(i, pageItems.get(slots.indexOf(i)).getItem()); 
                addSlotToCache(pane, i); 
            }
        }
    }

    private void addSlotToCache(Pane pane, int slot) {
        List<Integer> list = paneSlots.containsKey(pane) ? paneSlots.get(pane) : new ArrayList<>();
        list.add(slot);
        paneSlots.put(pane, list);

        slotToPane.put(slot, pane);
    }
 
    public GUIItem getItem(int slot) {
        Pane pane = slotToPane.get(slot);
        if (pane == null
                || !paneSlots.containsKey(pane)
                || !slotToPane.containsKey(slot)) return null;

        int paneSlot = paneSlots.get(pane).indexOf(slot);
        if (paneSlot == -1) return null;

        if (pane instanceof FillerPane) {
            FillerPane fillerPane = (FillerPane) pane;
            return fillerPane.getItems().get(paneSlot % fillerPane.getItems().size());
        } else if (pane instanceof StaticPane) {
            StaticPane staticPane = (StaticPane) pane;
            return staticPane.getItems().get(getPanePositionFromSlot(pane, slot)); 
        } else if (pane instanceof PaginatedPane) {
            PaginatedPane paginatedPane = (PaginatedPane) pane;
            return paginatedPane.getPageItems(paginatedPane.getPage()).get(paneSlot);
        }

        return null;
    }

    public GUIItem getItem(int row, int col) {
        return getItem(row * this.cols + col);
    }

    public void setItem(int slot, GUIItem item) {
        Pane pane = slotToPane.get(slot);
        if (pane == null
                || !paneSlots.containsKey(pane)
                || !slotToPane.containsKey(slot)) {
            StaticPane staticPane = new StaticPane(1, 1, new GUIPosition(slot / cols, slot % cols));
            staticPane.setItem(0, 0, item);
            addPane(staticPane, 999);

            return;
        }

        if (pane instanceof FillerPane) {
            FillerPane fillerPane = (FillerPane) pane;
            fillerPane.getItems().set(paneSlots.get(pane).indexOf(slot) % fillerPane.getItems().size(), item);
        } else if (pane instanceof StaticPane) {
            StaticPane staticPane = (StaticPane) pane;
            int paneSlot = paneSlots.get(pane).indexOf(slot);
            staticPane.getItems().put(getPanePositionFromSlot(pane, slot), item); 
        } else if (pane instanceof PaginatedPane) {
            PaginatedPane paginatedPane = (PaginatedPane) pane;
            int paneSlot = paneSlots.get(pane).indexOf(slot);
            paginatedPane.getPageItems(paginatedPane.getPage()).set(paneSlot, item);
        }
    }

    public void setItem(int row, int col, GUIItem item) {
        setItem(row * this.cols + col, item);
    }

    private GUIPosition getPanePositionFromSlot(Pane pane, int slot) {
        int row = (slot / cols) - pane.getPosition().row;
        int col = (slot % cols) - pane.getPosition().col;
        return new GUIPosition(row, col);
    }

    private int getSlotFromGUIPosition(Pane pane, GUIPosition pos) {
        return pos.row * pane.getCols() + pos.col;
    }
}