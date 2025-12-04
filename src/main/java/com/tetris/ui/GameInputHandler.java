package com.tetris.ui;

import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import com.tetris.gameLogic.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GameInputHandler {

    private final InputEventListener eventListener;
    private final Map<KeyCode, Runnable> keyActions = new HashMap<>();

    // Callbacks to update the view immediately after input
    private final Consumer<ViewData> onViewUpdate;
    private final Consumer<DownData> onDownUpdate;
    private final Runnable onPauseToggle;
    private final Runnable onNewGame;
    private final Runnable onMusicToggle;

    public GameInputHandler(InputEventListener eventListener,
                            Consumer<ViewData> onViewUpdate,
                            Consumer<DownData> onDownUpdate,
                            Runnable onPauseToggle,
                            Runnable onNewGame,
                            Runnable onMusicToggle) {
        this.eventListener = eventListener;
        this.onViewUpdate = onViewUpdate;
        this.onDownUpdate = onDownUpdate;
        this.onPauseToggle = onPauseToggle;
        this.onNewGame = onNewGame;
        this.onMusicToggle = onMusicToggle;
        initKeyActions();
    }

    private void initKeyActions() {
        Runnable leftAction = () -> onViewUpdate.accept(
                eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER))
        );
        keyActions.put(KeyCode.LEFT, leftAction);
        keyActions.put(KeyCode.A, leftAction);

        Runnable rightAction = () -> onViewUpdate.accept(
                eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER))
        );
        keyActions.put(KeyCode.RIGHT, rightAction);
        keyActions.put(KeyCode.D, rightAction);

        Runnable rotateAction = () -> onViewUpdate.accept(
                eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))
        );
        keyActions.put(KeyCode.UP, rotateAction);
        keyActions.put(KeyCode.W, rotateAction);

        Runnable downAction = () -> onDownUpdate.accept(
                eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER))
        );
        keyActions.put(KeyCode.DOWN, downAction);
        keyActions.put(KeyCode.S, downAction);

        Runnable spaceAction = () -> onDownUpdate.accept(
                eventListener.onSpaceEvent(new MoveEvent(EventType.SPACE, EventSource.USER))
        );
        keyActions.put(KeyCode.SPACE, spaceAction);
    }

    public void handleKeyPress(KeyEvent event) {
        // Handle Game State Controls (Pause/New Game/Mute)
        if (event.getCode() == KeyCode.P) {
            onPauseToggle.run();
            return;
        }
        else if (event.getCode() == KeyCode.N) {
            onNewGame.run();
            return;
        }
        else if (event.getCode() == KeyCode.M) {
            onMusicToggle.run();
            return;
        }

        // Handle Gameplay Controls
        Runnable action = keyActions.get(event.getCode());
        if (action != null) {
            action.run();
            event.consume();
        }
    }
}