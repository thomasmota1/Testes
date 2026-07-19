package com.tms.aula8.bingo;

public interface Bingo {
    void addCard(String playerId, Card card);

    void startGame();

    int playRound();

    boolean hasWinner();

    String getWinner();
}
