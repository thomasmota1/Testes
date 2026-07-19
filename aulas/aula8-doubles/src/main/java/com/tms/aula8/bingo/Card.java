package com.tms.aula8.bingo;

public interface Card {
    boolean mark(int number);

    boolean hasBingo();

    int[][] getNumbers();

    boolean[][] getMarkedState();
}
