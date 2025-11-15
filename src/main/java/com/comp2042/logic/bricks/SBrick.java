package com.comp2042.logic.bricks;

import com.comp2042.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

final class SBrick extends GeneralBrick {

    public SBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        });
    }

}
