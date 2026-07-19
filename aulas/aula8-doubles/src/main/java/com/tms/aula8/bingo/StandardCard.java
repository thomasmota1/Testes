package com.tms.aula8.bingo;

public class StandardCard implements Card {
    private static final int SIZE = 5;

    private final int[][] numbers;
    private final boolean[][] marked = new boolean[SIZE][SIZE];

    public StandardCard(int[][] numbers) {
        if (numbers == null || numbers.length != SIZE) {
            throw new IllegalArgumentException("Card must have five rows");
        }
        this.numbers = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            if (numbers[row] == null || numbers[row].length != SIZE) {
                throw new IllegalArgumentException("Card must have five columns per row");
            }
            System.arraycopy(numbers[row], 0, this.numbers[row], 0, SIZE);
        }
        marked[2][2] = true;
    }

    @Override
    public boolean mark(int number) {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (numbers[row][column] == number) {
                    marked[row][column] = true;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasBingo() {
        for (int index = 0; index < SIZE; index++) {
            if (rowComplete(index) || columnComplete(index)) {
                return true;
            }
        }
        return diagonalComplete() || antiDiagonalComplete();
    }

    @Override
    public int[][] getNumbers() {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(numbers[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }

    @Override
    public boolean[][] getMarkedState() {
        boolean[][] copy = new boolean[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(marked[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }

    private boolean rowComplete(int row) {
        for (int column = 0; column < SIZE; column++) {
            if (!marked[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean columnComplete(int column) {
        for (int row = 0; row < SIZE; row++) {
            if (!marked[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean diagonalComplete() {
        for (int index = 0; index < SIZE; index++) {
            if (!marked[index][index]) {
                return false;
            }
        }
        return true;
    }

    private boolean antiDiagonalComplete() {
        for (int index = 0; index < SIZE; index++) {
            if (!marked[index][SIZE - 1 - index]) {
                return false;
            }
        }
        return true;
    }
}
