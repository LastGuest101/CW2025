package com.tetris.ui;

import com.tetris.data.DownData;
import com.tetris.data.ViewData;
import com.tetris.gameLogic.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Manages user keyboard input and maps keystrokes to game actions.
 * <p>
 * This class abstracts the JavaFX {@link KeyEvent} logic away from the main View.
 * It supports dual control schemes (Arrow Keys and WASD) and handles:
 * <ul>
 * <li>Movement (Left, Right, Soft Drop).</li>
 * <li>Actions (Rotate, Hard Drop).</li>
 * <li>Game State Controls (Pause, New Game, Mute, Power-ups).</li>
 * </ul>
 *
 * @author Jacob Villegas
 */
public class GameInputHandler {

    private final InputEventListener eventListener;
    private final Map<KeyCode, Runnable> keyActions = new HashMap<>();

    // Callbacks to update the view immediately after input
    private final Consumer<ViewData> onViewUpdate;
    private final Consumer<DownData> onDownUpdate;
    private final Runnable onPauseToggle;
    private final Runnable onNewGame;
    private final Runnable onMusicToggle;

    /**
     * Constructs a new Input Handler with the necessary callbacks.
     *
     * @param eventListener The controller interface to notify when logic events occur (e.g., "Move Left requested").
     * @param onViewUpdate  Callback to refresh the view after lateral movement or rotation.
     * @param onDownUpdate  Callback to refresh the view after a drop action (which might clear lines).
     * @param onPauseToggle Callback to pause/unpause the game loop.
     * @param onNewGame     Callback to restart the game.
     * @param onMusicToggle Callback to mute/unmute audio.
     */
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

    /**
     * Initializes the key mappings.
     * <p>
     * Mappings:
     * <ul>
     * <li><b>Left/A:</b> Move Left</li>
     * <li><b>Right/D:</b> Move Right</li>
     * <li><b>Up/W:</b> Rotate</li>
     * <li><b>Down/S:</b> Soft Drop</li>
     * <li><b>Space:</b> Hard Drop</li>
     * </ul>
     */
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

    /**
     * Processes a raw key press event from JavaFX.
     * <p>
     * It first checks for global game state keys (P, N, M, F). If the key is not
     * a global control, it looks up the key in the {@code keyActions} map to trigger
     * gameplay movements.
     *
     * @param event The JavaFX KeyEvent to process.
     */
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
        else if (event.getCode() == KeyCode.F) {
            eventListener.onFreezeEvent();
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