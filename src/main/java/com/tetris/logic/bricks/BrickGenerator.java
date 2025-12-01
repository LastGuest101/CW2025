package com.tetris.logic.bricks;

import java.util.List;

public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();

    List<Brick> getNextBricks(int count);
}
