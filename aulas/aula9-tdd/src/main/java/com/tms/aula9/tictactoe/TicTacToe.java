package com.tms.aula9.tictactoe;

public class TicTacToe {
    private final char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private char winner = ' ';
    private int moves;

    public TicTacToe() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                board[row][column] = ' ';
            }
        }
    }

    public void play(int row, int column) {
        validatePosition(row, column);
        if (isFinished()) {
            throw new IllegalStateException("Game already finished");
        }
        if (board[row][column] != ' ') {
            throw new IllegalStateException("Cell already occupied");
        }
        board[row][column] = currentPlayer;
        moves++;
        if (hasLine(currentPlayer)) {
            winner = currentPlayer;
        } else {
            currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
        }
    }

    public char getCell(int row, int column) {
        validatePosition(row, column);
        return board[row][column];
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean hasWinner() {
        return winner != ' ';
    }

    public char getWinner() {
        return winner;
    }

    public boolean isDraw() {
        return moves == 9 && !hasWinner();
    }

    private boolean isFinished() {
        return hasWinner() || isDraw();
    }

    private boolean hasLine(char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player)
                    || (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player)
                || (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private static void validatePosition(int row, int column) {
        if (row < 0 || row > 2 || column < 0 || column > 2) {
            throw new IllegalArgumentException("Position outside board");
        }
    }
}
