package com.comp2042.ui;

import com.comp2042.data.DownData;
import com.comp2042.data.ViewData;
import com.comp2042.gameLogic.MoveEvent;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
