package me.schooltests.stgui.panes;

import me.schooltests.stgui.data.GUIItem;
import me.schooltests.stgui.guis.GUI;

import java.util.List;

public abstract class DrawHandler {
    public abstract List<Integer> drawPane(GUI gui);
    public abstract void setItem(GUI gui, GUIItem item, int guiSlot, int paneSlot);
    public abstract GUIItem getItem(GUI gui, int guiSlot, int paneSlot);
}
