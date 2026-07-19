package com.tms.aula8.bingo;

import java.util.LinkedHashMap;
import java.util.Map;

public class StandardBingoGame implements Bingo {
    private final Caller caller;
    private final Map<String, Card> cards = new LinkedHashMap<>();
    private boolean started;
    private String winner;

    public StandardBingoGame(Caller caller) {
        this.caller = caller;
    }

    @Override
    public void addCard(String playerId, Card card) {
        if (started) {
            throw new IllegalStateException("Game already started");
        }
        cards.put(playerId, card);
    }

    @Override
    public void startGame() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("At least one card is required");
        }
        started = true;
        winner = null;
        caller.reset();
    }

    @Override
    public int playRound() {
        if (!started) {
            throw new IllegalStateException("Game not started");
        }
        int number = caller.callNumber();
        for (Map.Entry<String, Card> entry : cards.entrySet()) {
            entry.getValue().mark(number);
            if (entry.getValue().hasBingo() && winner == null) {
                winner = entry.getKey();
            }
        }
        return number;
    }

    @Override
    public boolean hasWinner() {
        return winner != null;
    }

    @Override
    public String getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return hasWinner() || !caller.hasMoreNumbers();
    }
}
