package com.tetris.gameLogic;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FreezeManager class.
 * Tests freeze activation, one-time use restriction, duration, and reset functionality.
 *
 * Note: Requires JavaFX Platform initialization since FreezeManager uses Timeline.
 *
 * @author Jacob Villegas
 */
class FreezeManagerTest {

    private FreezeManager freezeManager;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // Start JavaFX platform if not already running
        Platform.startup(() -> {
            latch.countDown();
        });

        boolean initialized = latch.await(5, TimeUnit.SECONDS);
        assertTrue(initialized, "JavaFX Platform failed to initialize");
    }

    @BeforeEach
    void setUp() {
        freezeManager = new FreezeManager();
    }


    @Test
    void testInitialStateNotFrozen() {
        assertFalse(freezeManager.isFrozen(),
                "Freeze should be inactive initially");
    }

    @Test
    void testInitialStateAllowsActivation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = {false};

        Platform.runLater(() -> {
            result[0] = freezeManager.tryActivate(null);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);
        assertTrue(result[0],
                "First activation should succeed");
    }


    @Test
    void testActivateSuccessfully() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] activated = {false};

        Platform.runLater(() -> {
            activated[0] = freezeManager.tryActivate(null);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        assertTrue(activated[0],
                "tryActivate should return true on first use");
        assertTrue(freezeManager.isFrozen(),
                "isFrozen should return true after activation");
    }

    @Test
    void testActivateWithCallback() throws InterruptedException {
        boolean[] callbackExecuted = {false};
        CountDownLatch activationLatch = new CountDownLatch(1);
        CountDownLatch callbackLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            freezeManager.tryActivate(() -> {
                callbackExecuted[0] = true;
                callbackLatch.countDown();
            });
            activationLatch.countDown();
        });

        activationLatch.await(1, TimeUnit.SECONDS);

        assertTrue(freezeManager.isFrozen(),
                "Should be frozen immediately after activation");

        boolean completed = callbackLatch.await((long)
                        GameConfig.FREEZE_DURATION + 500,
                TimeUnit.MILLISECONDS
        );

        assertTrue(completed,
                "Callback should execute within expected time");
        assertTrue(callbackExecuted[0],
                "onFreezeEnd callback should have been executed");
    }

    @Test
    void testActivateWithNullCallback() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        assertDoesNotThrow(() -> {
            Platform.runLater(() -> {
                freezeManager.tryActivate(null);
                latch.countDown();
            });
        }, "Activating with null callback should not throw exception");

        latch.await(1, TimeUnit.SECONDS);

        assertTrue(freezeManager.isFrozen(),
                "Freeze should still activate with null callback");
    }

    @Test
    void testSecondActivationFails() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] firstResult = {false};
        boolean[] secondResult = {false};

        Platform.runLater(() -> {
            firstResult[0] = freezeManager.tryActivate(null);
            secondResult[0] = freezeManager.tryActivate(null);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        assertTrue(firstResult[0], "First activation should succeed");
        assertFalse(secondResult[0],
                "Second activation should fail (already used)");
    }

    @Test
    void testMultipleActivationAttemptsFail() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] results = new boolean[6];

        Platform.runLater(() -> {
            results[0] = freezeManager.tryActivate(null);
            // Try 5 more times
            for (int i = 1; i < 6; i++) {
                results[i] = freezeManager.tryActivate(null);
            }
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        assertTrue(results[0], "First activation should succeed");
        for (int i = 1; i < 6; i++) {
            assertFalse(results[i],
                    "Activation attempt " + (i + 1) + " should fail");
        }
    }

    @Test
    void testActivationFailsWhileFrozen() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] secondTry = {false};

        Platform.runLater(() -> {
            freezeManager.tryActivate(null);
            secondTry[0] = freezeManager.tryActivate(null);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        assertTrue(freezeManager.isFrozen(),
                "Should be frozen after first activation");
        assertFalse(secondTry[0],
                "Cannot activate while already frozen");
    }


    @Test
    void testFreezeExpiresAfterDuration() throws InterruptedException {
        CountDownLatch callbackLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            freezeManager.tryActivate(callbackLatch::countDown);
        });

        Thread.sleep(100);

        assertTrue(freezeManager.isFrozen(),
                "Should be frozen immediately");

        boolean expired = callbackLatch.await((long)
                        GameConfig.FREEZE_DURATION + 500,
                TimeUnit.MILLISECONDS
        );

        assertTrue(expired,
                "Freeze should expire after FREEZE_DURATION");
        assertFalse(freezeManager.isFrozen(),
                "Should no longer be frozen after expiry");
    }

    @Test
    void testFreezeStillActiveBeforeDuration() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            freezeManager.tryActivate(null);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        // Wait for half the duration
        Thread.sleep((long) GameConfig.FREEZE_DURATION / 2);

        assertTrue(freezeManager.isFrozen(),
                "Should still be frozen halfway through duration");
    }

    @Test
    void testCallbackExecutesOnlyOnce() throws InterruptedException {
        int[] callbackCount = {0};
        CountDownLatch callbackLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            freezeManager.tryActivate(() -> {
                callbackCount[0]++;
                callbackLatch.countDown();
            });
        });

        callbackLatch.await((long) GameConfig.FREEZE_DURATION + 500, TimeUnit.MILLISECONDS);

        Thread.sleep(200);

        assertEquals(1, callbackCount[0],
                "Callback should execute exactly once");
    }

    @Test
    void testResetClearsFrozenState() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            freezeManager.tryActivate(null);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);
        assertTrue(freezeManager.isFrozen());

        freezeManager.reset();

        assertFalse(freezeManager.isFrozen(),
                "isFrozen should be false after reset");
    }

    @Test
    void testResetAllowsReactivation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] firstAttempt = {false};
        boolean[] secondAttempt = {false};
        boolean[] afterReset = {false};

        Platform.runLater(() -> {
            firstAttempt[0] = freezeManager.tryActivate(null);
            secondAttempt[0] = freezeManager.tryActivate(null);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        assertTrue(firstAttempt[0], "First activation should succeed");
        assertFalse(secondAttempt[0], "Second activation should fail before reset");

        freezeManager.reset();

        CountDownLatch latch2 = new CountDownLatch(1);
        Platform.runLater(() -> {
            afterReset[0] = freezeManager.tryActivate(null);
            latch2.countDown();
        });

        latch2.await(1, TimeUnit.SECONDS);

        assertTrue(afterReset[0],
                "Activation should succeed after reset");
        assertTrue(freezeManager.isFrozen(),
                "Should be frozen after successful reactivation");
    }

    @Test
    void testResetBeforeUse() throws InterruptedException {
        freezeManager.reset(); // Reset without ever activating

        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = {false};

        Platform.runLater(() -> {
            result[0] = freezeManager.tryActivate(null);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        assertTrue(result[0],
                "Reset should not prevent first activation");
    }

    @Test
    void testMultipleResetCycles() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = new CountDownLatch(1);
            boolean[] activated = {false};
            boolean[] secondTry = {false};

            Platform.runLater(() -> {
                activated[0] = freezeManager.tryActivate(null);
                secondTry[0] = freezeManager.tryActivate(null);
                latch.countDown();
            });

            latch.await(1, TimeUnit.SECONDS);

            assertTrue(activated[0],
                    "Cycle " + (i + 1) + ": First activation should succeed");
            assertFalse(secondTry[0],
                    "Cycle " + (i + 1) + ": Second activation should fail");

            freezeManager.reset();
            assertFalse(freezeManager.isFrozen(),
                    "Cycle " + (i + 1) + ": Should not be frozen after reset");
        }
    }

    @Test
    void testResetDuringActiveFreeze() throws InterruptedException {
        CountDownLatch activationLatch = new CountDownLatch(1);
        CountDownLatch callbackLatch = new CountDownLatch(1);
        boolean[] callbackExecuted = {false};

        Platform.runLater(() -> {
            freezeManager.tryActivate(() -> {
                callbackExecuted[0] = true;
                callbackLatch.countDown();
            });
            activationLatch.countDown();
        });

        activationLatch.await(1, TimeUnit.SECONDS);

        assertTrue(freezeManager.isFrozen(),
                "Should be frozen before reset");

        freezeManager.reset();

        assertFalse(freezeManager.isFrozen(),
                "Should not be frozen immediately after reset");

        boolean callbackFired = callbackLatch.await((long)
                        GameConfig.FREEZE_DURATION + 500,
                TimeUnit.MILLISECONDS
        );

        assertTrue(callbackFired,
                "Callback should still execute (Timeline already running)");
    }
}