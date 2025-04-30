package model;

import java.util.Arrays;

public enum MapModel {
    MAP_1(new int[][]{
            {3, 4, 4, 3},
            {3, 4, 4, 3},
            {3, 0, 0, 0},
            {3, 0, 0, 0},
            {1, 0, 0, 1}
    }, new int[] {3, 1});

    private int[][] matrix;
    private final int[] success_condition;
    private final int[][] copy;

    MapModel(int[][] matrix, int[] success_condition) {
        this.matrix = matrix;
        this.success_condition = success_condition;
        this.copy = deepCopy(matrix);
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int getId(int row, int col) {
        return matrix[row][col];
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public boolean checkInWidthSize(int col) {
        return col >= 0 && col < matrix[0].length;
    }

    public boolean checkInHeightSize(int row) {
        return row >= 0 && row < matrix.length;
    }

    public int[] getSuccess_condition() {
        return success_condition;
    }

    public int[][] deepCopy(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    public String toString() {
        return Arrays.deepToString(matrix);
    }

    public int[][] getCopy() {
        return copy;
    }
}
