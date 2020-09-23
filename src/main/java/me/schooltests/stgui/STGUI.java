package me.schooltests.stgui;

import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.guis.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public final class STGUI implements Listener {
    private static JavaPlugin plugin;
    private final static Map<Player, GUI> OPENED = new IdentityHashMap<>();
    public static Optional<GUI> getOpenedGUI(Player p) {
        return Optional.ofNullable(OPENED.get(p));
    }

    public static void setOpenedGUI(Player p, GUI gui) {
        OPENED.put(p, gui);
    }

    public static Map<Player, GUI> getOpenedGUIMap() {
        return Collections.unmodifiableMap(OPENED);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void setPlugin(JavaPlugin plugin) {
        if (STGUI.plugin != null) return;
        STGUI.plugin = plugin;
        registerEvents();
    }

    private static void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent event) {
                if (!(event.getWhoClicked() instanceof Player)) return;
                Player player = (Player) event.getWhoClicked();
                GUI gui = OPENED.get(player);
                if (gui == null
                        || event.getSlot() < 0
                        || event.getRawSlot() > (gui.getInventory().getSize())
                        || (!gui.getTitle().isEmpty() && event.getView().getTitle().isEmpty())
                        || !gui.getTitle().equals(event.getView().getTitle())) return;

                GUIItem item = gui.getItem(event.getSlot());
                if (item == null) return;

                ItemClickEvent itemClickEvent = new ItemClickEvent(player, gui, item, event.getClick(), event.getSlot());
                Bukkit.getPluginManager().callEvent(itemClickEvent);

                if (item.getClickEvent() != null) item.getClickEvent().accept(itemClickEvent);
                event.setCancelled(itemClickEvent.isCancelled());
            }

            @EventHandler
            public void onClose(InventoryCloseEvent event) {
                if (!(event.getPlayer() instanceof Player)) return;
                Player player = (Player) event.getPlayer();
                GUI gui = OPENED.get(player);
                if (gui == null) return;

                if (gui.getCloseEvent() != null) gui.getCloseEvent().accept(event, gui);

                OPENED.remove(player);
                gui.getViewers().remove(player);
            }
        }, STGUI.plugin);
    }
}
