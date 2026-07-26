package com.tms.aula9.katas;

import java.util.ArrayList;
import java.util.List;

public class BowlingGame {
    private final List<Integer> rolls = new ArrayList<>();

    public void roll(int pins) {
        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("Pins must be between 0 and 10");
        }
        rolls.add(pins);
    }

    public int score() {
        int score = 0;
        int rollIndex = 0;
        for (int frame = 0; frame < 10; frame++) {
            if (isStrike(rollIndex)) {
                score += 10 + rolls.get(rollIndex + 1) + rolls.get(rollIndex + 2);
                rollIndex++;
            } else if (isSpare(rollIndex)) {
                score += 10 + rolls.get(rollIndex + 2);
                rollIndex += 2;
            } else {
                score += rolls.get(rollIndex) + rolls.get(rollIndex + 1);
                rollIndex += 2;
            }
        }
        return score;
    }

    private boolean isStrike(int rollIndex) {
        return rolls.get(rollIndex) == 10;
    }

    private boolean isSpare(int rollIndex) {
        return rolls.get(rollIndex) + rolls.get(rollIndex + 1) == 10;
    }
}
