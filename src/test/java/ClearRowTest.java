import com.tetris.gameLogic.ClearRow;
import com.tetris.gameLogic.MatrixOperations;
import com.tetris.gameLogic.Score;
import org.junit.jupiter.api.Test;

import static com.tetris.gameLogic.MatrixOperations.checkRemoving;
import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {
    @Test
    void noClearTest() {
       int[][] initialMatrix = {
               {1, 1, 1, 0},
               {1, 1, 0, 1},
               {1, 0, 1, 1},
               {0, 1, 1, 1}
       };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        int linesRemoved = clearRow.getLinesRemoved(); ;
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = clearRow.getScoreBonus();

        assertEquals(0,linesRemoved);
        assertArrayEquals(initialMatrix, newMatrix);
        assertEquals(0, scoreBonus);


    }

    @Test
    void oneLineClearTest() {
        int[][] initialMatrix = {
                {1, 0, 1, 0},
                {1, 1, 0, 1},
                {1, 1, 1, 1},
                {0, 1, 1, 1}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 1},
                {0, 1, 1, 1}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        int linesRemoved = clearRow.getLinesRemoved(); ;
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = clearRow.getScoreBonus();

        assertEquals(1,linesRemoved);
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(50, scoreBonus);
    }

    @Test
    void multipleLineClearTest() {
        int[][] initialMatrix = {
                {1, 0, 1, 0},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 0, 1, 0}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        int linesRemoved = clearRow.getLinesRemoved(); ;
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = clearRow.getScoreBonus();

        assertEquals(3,linesRemoved);
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(450, scoreBonus);
    }

    @Test
    void bottomRowClearTest() {
        int[][] initialMatrix = {
                {0, 0, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 1},
                {1, 1, 1, 1}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 0, 1, 0},
                {1, 1, 0, 1}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        int linesRemoved = clearRow.getLinesRemoved(); ;
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = clearRow.getScoreBonus();

        assertEquals(1,linesRemoved);
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(50, scoreBonus);
    }

    @Test
    void topLineClearTest() {
        int[][] initialMatrix = {
                {1, 1, 1, 1},
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 0, 1}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 0, 1}
        };
        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);
        int linesRemoved = clearRow.getLinesRemoved(); ;
        int [][] newMatrix = clearRow.getNewMatrix();
        int scoreBonus = clearRow.getScoreBonus();

        assertEquals(1,linesRemoved);
        assertArrayEquals(expectedMatrix, newMatrix);
        assertEquals(50, scoreBonus);
    }

    @Test
    void nonContiguousRowsClearTest() {
        int[][] initialMatrix = {
                {1, 1, 1, 1},
                {0, 1, 0, 1},
                {1, 1, 1, 1},
                {1, 0, 1, 0}
        };

        int[][] expectedMatrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);

        assertEquals(2, clearRow.getLinesRemoved());
        assertArrayEquals(expectedMatrix, clearRow.getNewMatrix());
        assertEquals(200, clearRow.getScoreBonus());;
    }

    @Test
    void  fourLineTetrisTest() {
        int[][] initialMatrix = {
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1}
        };

        int[][] expectedMatrix = {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(initialMatrix);

        assertEquals(4, clearRow.getLinesRemoved());
        assertArrayEquals(expectedMatrix, clearRow.getNewMatrix());
        assertEquals(800, clearRow.getScoreBonus());
    }
}
