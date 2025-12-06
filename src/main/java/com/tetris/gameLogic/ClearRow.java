package com.tetris.gameLogic;

import java.util.ArrayList;
import java.util.List;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedIndices;
    private final List<int[]> clearedRowsData;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedIndices, List<int[]> clearedRowsData) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedIndices = clearedIndices;
        this.clearedRowsData = clearedRowsData;
    }

    // Constructor for tests (defaults to empty)
    public ClearRow(int linesRemoved, int[][] newMatrix) {
        this(linesRemoved, newMatrix, 0, new ArrayList<>(), new ArrayList<>());
    }

    public int getLinesRemoved() { return linesRemoved; }
    public int[][] getNewMatrix() { return MatrixOperations.copy(newMatrix); }
    public int getScoreBonus() { return scoreBonus; }

    public List<Integer> getClearedIndices() { return clearedIndices; }

    public List<int[]> getClearedRowsData() { return clearedRowsData; }
}