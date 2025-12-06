package com.tetris.gameLogic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class FreezeManager {

    private boolean isFrozen = false;
    private boolean hasUsedFreeze = false;

    public boolean tryActivate(Runnable onFreezeEnd) {
        if (isFrozen || hasUsedFreeze) {
            return false;
        }

        isFrozen = true;
        hasUsedFreeze = true;

        new Timeline(new KeyFrame(
                Duration.millis(GameConfig.FREEZE_DURATION),
                ae -> {
                    isFrozen = false;
                    if (onFreezeEnd != null) onFreezeEnd.run();
                }
        )).play();

        return true;
    }

    public void reset() {
        isFrozen = false;
        hasUsedFreeze = false;
    }

    public boolean isFrozen() {
        return isFrozen;
    }
}