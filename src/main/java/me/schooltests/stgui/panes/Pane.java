package me.schooltests.stgui.panes;

import me.schooltests.stgui.data.GUIPosition;

public abstract class Pane {
    public abstract int getPriority();

    public abstract void setDimensions(int rows, int cols);
    public abstract int getRows();
    public abstract int getCols();

    public abstract void setPosition(GUIPosition pos);
    public abstract GUIPosition getPosition();

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        result = result * prime + getRows();
        result = result * prime + getCols();
        result = result * prime + getPosition().hashCode();
        result = result * prime + getPriority();

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pane)) return false;
        Pane pane = (Pane) obj;

        return getPosition().equals(pane.getPosition())
                && getRows() == pane.getRows()
                && getCols() == pane.getCols()
                && getPriority() == pane.getPriority();
    }

    public abstract DrawHandler getHandler();
}