package com.comp2042.logic.bricks;

import com.comp2042.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

final class OBrick extends GeneralBrick {

    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

}
