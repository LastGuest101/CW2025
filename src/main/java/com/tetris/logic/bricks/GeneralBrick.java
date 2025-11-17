package com.tetris.logic.bricks;

import com.tetris.gameLogic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

public class GeneralBrick implements Brick
{
    protected final List<int[][]> brickMatrix = new ArrayList<>();

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
