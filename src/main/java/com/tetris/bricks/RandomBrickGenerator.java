package com.tetris.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        refillQueue();
    }

    private void refillQueue() {
        while (nextBricks.size() < 7) {
            List<Brick> bag = new ArrayList<>(brickList);
            Collections.shuffle(bag);
            nextBricks.addAll(bag);


        }
    }

    @Override
    public Brick getBrick() {
        refillQueue();
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        refillQueue();
        return nextBricks.peek();
    }

    @Override
    public List<Brick> getNextBricks(int count) {
        refillQueue();
        return nextBricks.stream().limit(count).collect(Collectors.toList());
    }
}