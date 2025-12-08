package com.tetris.data;

import com.tetris.gameLogic.ClearRow;

/**
 * A container object representing the result of a "Move Down" operation.
 * <p>
 * @author Jacob Villegas
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
