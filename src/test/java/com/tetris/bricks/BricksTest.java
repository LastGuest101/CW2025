package com.tetris.bricks;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;
/**
 * Test suite for verifying the structural integrity and rotation data of all Tetris brick types.
 * <p>
 * This class contains unit tests for each concrete implementation of the {@link Brick} interface
 * (I, J, L, O, S, T, Z).
 *
 * @author Jacob Villegas
 */
class BricksTest {
    @Test
    void ObrickTest() {
        Brick Brick = new OBrick();
        List<int[][]> Bricks = Brick.getShapeMatrix();
        int[][] expectedBrick = {
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals( expectedBrick, Bricks.get(0));
    }

    @Test
    void IbrickTest() {
        Brick Brick = new IBrick();
        List<int[][]> Bricks = Brick.getShapeMatrix();
        int[][] expectedBrick = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] expectedBrick2 = {
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        };
        assertArrayEquals( expectedBrick, Bricks.get(0));
        assertArrayEquals( expectedBrick2, Bricks.get(1));
    }

    @Test
    void JbrickTest() {
        Brick brick = new JBrick();
        List<int[][]> bricks = brick.getShapeMatrix();

        int[][] expected0 = {
                {0, 0, 0, 0},
                {2, 2, 2, 0},
                {0, 0, 2, 0},
                {0, 0, 0, 0}
        };
        int[][] expected1 = {
                {0, 0, 0, 0},
                {0, 2, 2, 0},
                {0, 2, 0, 0},
                {0, 2, 0, 0}
        };
        int[][] expected2 = {
                {0, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 2, 2, 2},
                {0, 0, 0, 0}
        };
        int[][] expected3 = {
                {0, 0, 2, 0},
                {0, 0, 2, 0},
                {0, 2, 2, 0},
                {0, 0, 0, 0}
        };

        assertArrayEquals(expected0, bricks.get(0));
        assertArrayEquals(expected1, bricks.get(1));
        assertArrayEquals(expected2, bricks.get(2));
        assertArrayEquals(expected3, bricks.get(3));
    }

    @Test
    void LbrickTest() {
        Brick brick = new LBrick();
        List<int[][]> bricks = brick.getShapeMatrix();

        int[][] expected0 = {
                {0, 0, 0, 0},
                {0, 3, 3, 3},
                {0, 3, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] expected1 = {
                {0, 0, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 3, 0},
                {0, 0, 3, 0}
        };
        int[][] expected2 = {
                {0, 0, 0, 0},
                {0, 0, 3, 0},
                {3, 3, 3, 0},
                {0, 0, 0, 0}
        };
        int[][] expected3 = {
                {0, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 0, 0}
        };

        assertArrayEquals(expected0, bricks.get(0));
        assertArrayEquals(expected1, bricks.get(1));
        assertArrayEquals(expected2, bricks.get(2));
        assertArrayEquals(expected3, bricks.get(3));
    }

    @Test
    void SbrickTest() {
        Brick brick = new SBrick();
        List<int[][]> bricks = brick.getShapeMatrix();

        int[][] expected0 = {
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] expected1 = {
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        };

        assertArrayEquals(expected0, bricks.get(0));
        assertArrayEquals(expected1, bricks.get(1));
    }

    @Test
    void TbrickTest() {
        Brick brick = new TBrick();
        List<int[][]> bricks = brick.getShapeMatrix();

        int[][] expected0 = {
                {0, 0, 0, 0},
                {6, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] expected1 = {
                {0, 6, 0, 0},
                {0, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] expected2 = {
                {0, 6, 0, 0},
                {6, 6, 6, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] expected3 = {
                {0, 6, 0, 0},
                {6, 6, 0, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        };

        assertArrayEquals(expected0, bricks.get(0));
        assertArrayEquals(expected1, bricks.get(1));
        assertArrayEquals(expected2, bricks.get(2));
        assertArrayEquals(expected3, bricks.get(3));
    }

    @Test
    void ZbrickTest() {
        Brick brick = new ZBrick();
        List<int[][]> bricks = brick.getShapeMatrix();

        int[][] expected0 = {
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        };
        int[][] expected1 = {
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        };

        assertArrayEquals(expected0, bricks.get(0));
        assertArrayEquals(expected1, bricks.get(1));
    }


}