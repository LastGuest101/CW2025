import com.tetris.bricks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BrickGenerator interface and RandomBrickGenerator implementation.
 * Tests the 7-bag randomization system, fairness guarantees, and brick distribution.
 *
 * @author Jacob Villegas
 */
class BrickGeneratorTest {

    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    // ========== Basic Functionality Tests ==========

    @Test
    void testGetBrickReturnsNonNull() {
        Brick brick = generator.getBrick();

        assertNotNull(brick,
                "getBrick() should never return null");
    }

    @Test
    void testGetBrickReturnsValidBrickType() {
        Brick brick = generator.getBrick();

        assertTrue(
                brick instanceof IBrick ||
                        brick instanceof JBrick ||
                        brick instanceof LBrick ||
                        brick instanceof OBrick ||
                        brick instanceof SBrick ||
                        brick instanceof TBrick ||
                        brick instanceof ZBrick,
                "getBrick() should return one of the 7 Tetris brick types"
        );
    }

    @Test
    void testGetNextBrickReturnsNonNull() {
        Brick nextBrick = generator.getNextBrick();

        assertNotNull(nextBrick,
                "getNextBrick() should never return null");
    }

    @Test
    void testGetNextBricksReturnsCorrectCount() {
        List<Brick> nextBricks = generator.getNextBricks(3);

        assertEquals(3, nextBricks.size(),
                "getNextBricks(3) should return exactly 3 bricks");
    }

    @Test
    void testGetNextBricksWithZeroCount() {
        List<Brick> nextBricks = generator.getNextBricks(0);

        assertEquals(0, nextBricks.size(),
                "getNextBricks(0) should return empty list");
    }

    @Test
    void testEvery7BricksContainsAllTypes() {
        Map<String, Integer> brickCounts = new HashMap<>();

        // Get 7 bricks (one full bag)
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            String type = brick.getClass().getSimpleName();
            brickCounts.put(type, brickCounts.getOrDefault(type, 0) + 1);
        }

        assertEquals(7, brickCounts.size(),
                "First 7 bricks should contain all 7 types (I, J, L, O, S, T, Z)");

        assertTrue(brickCounts.containsKey("IBrick"), "Should contain I-brick");
        assertTrue(brickCounts.containsKey("JBrick"), "Should contain J-brick");
        assertTrue(brickCounts.containsKey("LBrick"), "Should contain L-brick");
        assertTrue(brickCounts.containsKey("OBrick"), "Should contain O-brick");
        assertTrue(brickCounts.containsKey("SBrick"), "Should contain S-brick");
        assertTrue(brickCounts.containsKey("TBrick"), "Should contain T-brick");
        assertTrue(brickCounts.containsKey("ZBrick"), "Should contain Z-brick");

        // Each type should appear exactly once
        for (int count : brickCounts.values()) {
            assertEquals(1, count,
                    "Each brick type should appear exactly once in a bag");
        }
    }

    @Test
    void testBagRefillsWhenEmpty() {
        Map<String, Integer> firstBagCounts = new HashMap<>();
        Map<String, Integer> secondBagCounts = new HashMap<>();

        // First bag (7 bricks)
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            String type = brick.getClass().getSimpleName();
            firstBagCounts.put(type, firstBagCounts.getOrDefault(type, 0) + 1);
        }

        // Second bag (next 7 bricks)
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            String type = brick.getClass().getSimpleName();
            secondBagCounts.put(type, secondBagCounts.getOrDefault(type, 0) + 1);
        }

        assertEquals(7, firstBagCounts.size(), "First bag should have all 7 types");
        assertEquals(7, secondBagCounts.size(), "Second bag should have all 7 types");
    }

    @Test
    void testMultipleBagsAllContainAllTypes() {
        for (int bag = 0; bag < 5; bag++) { // Test 5 bags (35 bricks)
            Map<String, Integer> brickCounts = new HashMap<>();

            for (int i = 0; i < 7; i++) {
                Brick brick = generator.getBrick();
                String type = brick.getClass().getSimpleName();
                brickCounts.put(type, brickCounts.getOrDefault(type, 0) + 1);
            }

            assertEquals(7, brickCounts.size(),
                    "Bag " + (bag + 1) + " should contain all 7 types");

            for (int count : brickCounts.values()) {
                assertEquals(1, count,
                        "Bag " + (bag + 1) + " should have each type exactly once");
            }
        }
    }



    @Test
    void testGetNextBrickDoesNotConsume() {
        Brick peek1 = generator.getNextBrick();
        Brick peek2 = generator.getNextBrick();

        assertEquals(peek1.getClass(), peek2.getClass(),
                "getNextBrick() should return same brick on multiple calls (peek, not poll)");

        Brick consumed = generator.getBrick();

        assertEquals(peek1.getClass(), consumed.getClass(),
                "getBrick() should return the brick that was peeked");
    }

    @Test
    void testGetNextBricksDoesNotConsume() {
        List<Brick> preview = generator.getNextBricks(3);
        List<String> previewTypes = preview.stream()
                .map(b -> b.getClass().getSimpleName())
                .collect(Collectors.toList());

        // Consume and verify
        for (int i = 0; i < 3; i++) {
            Brick consumed = generator.getBrick();
            assertEquals(previewTypes.get(i), consumed.getClass().getSimpleName(),
                    "Brick " + i + " should match preview");
        }
    }

    @Test
    void testGetNextBricksWithLargeCount() {
        List<Brick> nextBricks = generator.getNextBricks(20);

        assertTrue(nextBricks.size() >= 3,
                "Should return at least 3 bricks (queue might be smaller than requested)");
    }

    @Test
    void testPreviewUpdatesAfterConsumption() {
        Brick first = generator.getNextBrick();
        generator.getBrick(); // Consume first
        Brick second = generator.getNextBrick();

        assertNotEquals(first.getClass(), second.getClass(),
                "Preview should update after consuming brick");
    }

    @Test
    void testConsecutiveGetBrickCalls() {
        List<String> types = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            types.add(generator.getBrick().getClass().getSimpleName());
        }

        assertEquals(100, types.size(),
                "Should successfully generate 100 consecutive bricks");
    }

    // Integration Tests

    @Test
    void testRealisticGameplayScenario() {
        // Simulate 50 bricks being generated and previewed
        int bricksPlaced = 0;

        while (bricksPlaced < 50) {
            // Check preview
            List<Brick> preview = generator.getNextBricks(3);
            assertNotNull(preview, "Preview should always be available");

            // "Place" brick
            Brick current = generator.getBrick();
            assertNotNull(current, "Current brick should always be available");

            bricksPlaced++;
        }

        assertEquals(50, bricksPlaced,
                "Should successfully simulate 50 brick placements");
    }

}