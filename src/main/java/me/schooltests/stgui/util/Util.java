package me.schooltests.stgui.util;

import me.schooltests.stgui.panes.Pane;

import java.util.ArrayList;
import java.util.List;

public final class Util {
    private Util() {}

    public static List<Integer> getSlots(Pane pane, int guiRows, int guiCols) {
        int rowStart = pane.getPosition().row * guiCols + pane.getPosition().col;
        int rowEnd = rowStart + pane.getCols() - 1;
        List<Integer> list = between(rowStart, rowEnd);
        for (int i = 0; i < pane.getRows() - 1; i++) {
            list.addAll(between(rowStart + (guiCols * (i + 1)), rowEnd + (guiCols * (i + 1))));
        }

        list.removeIf(i -> i >= guiRows * guiCols);
        return list;
    }

    public static List<Integer> between(int i, int j) {
        List<Integer> list = new ArrayList<>();
        for (int k = i; k <= j; k++) list.add(k);
        return list;
    }
}
