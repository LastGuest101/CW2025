package com.tetris.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
/**
 * Handles the generation of Tetris bricks using the "7-Bag" Randomizer system.
 * <p>
 * Instead of generating purely random pieces (which could result in "droughts" of specific pieces),
 * this class creates a "bag" containing one of every shape (I, J, L, O, S, T, Z),
 * shuffles them, and adds them to a queue. This ensures that the player receives
 * every shape at least once every 7 pieces, providing a fair distribution.
 *
 * @author Jacob Villegas
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Constructs a new RandomBrickGenerator.
     * <p>
     * Initializes the prototype list with all 7 Tetris shapes and pre-fills the
     * queue.
     */
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

    /**
     * Ensures the queue has enough bricks for gameplay and previews.
     * <p>
     * If the queue size drops below 7, a new "bag" of unique bricks is shuffled
     * and added to the end of the queue.
     */
    private void refillQueue() {
        while (nextBricks.size() < 7) {
            List<Brick> bag = new ArrayList<>(brickList);
            Collections.shuffle(bag);
            nextBricks.addAll(bag);


        }
    }

    /**
     * Retrieves and removes the next brick from the queue.
     * <p>
     * This method is called when spawning a new piece onto the board.
     *
     * @return The next {@link Brick} in the sequence.
     */
    @Override
    public Brick getBrick() {
        refillQueue();
        return nextBricks.poll();
    }

    /**
     * Peeks at the next brick without removing it from the queue.
     * <p>
     * Useful for showing a single "Next Piece" preview.
     *
     * @return The upcoming {@link Brick}.
     */
    @Override
    public Brick getNextBrick() {
        refillQueue();
        return nextBricks.peek();
    }

    /**
     * Retrieves a list of upcoming bricks without removing them.
     * <p>
     * This is used to display a list of multiple future pieces in the UI sidebar.
     *
     * @param count The number of upcoming bricks to retrieve.
     * @return A list containing the next {@code count} bricks.
     */
    @Override
    public List<Brick> getNextBricks(int count) {
        refillQueue();
        return nextBricks.stream().limit(count).collect(Collectors.toList());
    }
}