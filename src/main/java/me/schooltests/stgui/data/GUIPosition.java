package me.schooltests.stgui.data;

public class GUIPosition {
    public final int row;
    public final int col;
    public GUIPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public int hashCode() {
        int result = 31 + row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GUIPosition)) return false;
        GUIPosition pos = (GUIPosition) obj;

        return pos.row == row && pos.col == col;
    }
}
