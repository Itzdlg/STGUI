package me.schooltests.stgui.guis;

import me.schooltests.stgui.STGUI;
import me.schooltests.stgui.data.DataHolder;
import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.data.GUIPosition;
import me.schooltests.stgui.panes.Pane;
import me.schooltests.stgui.panes.StaticPane;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
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
                for (int i : pane.getHandler().drawPane(this)) {
                    addSlotToCache(pane, i);
                }
            }
        }

        if (reopen && !toReopen.isEmpty()) {
            for (Player p : toReopen) {
                p.openInventory(inventory);
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

        return pane.getHandler().getItem(this, slot, paneSlot);
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

        int paneSlot = paneSlots.get(pane).indexOf(slot);
        if (paneSlot == -1) return;

        pane.getHandler().setItem(this, item, slot, paneSlots.get(pane).indexOf(slot));
    }

    public void setItem(int row, int col, GUIItem item) {
        setItem(row * this.cols + col, item);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}