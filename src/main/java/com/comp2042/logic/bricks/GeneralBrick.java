package com.comp2042.logic.bricks;

import com.comp2042.MatrixOperations;

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
