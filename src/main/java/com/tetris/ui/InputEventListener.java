package com.tetris.ui;

import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import com.tetris.gameLogic.MoveEvent;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
