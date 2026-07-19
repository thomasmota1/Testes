package com.tms.aula8.bingo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomCaller implements Caller {
    private final List<Integer> availableNumbers = new ArrayList<>();
    private final List<Integer> calledNumbers = new ArrayList<>();

    public RandomCaller() {
        reset();
    }

    @Override
    public int callNumber() {
        if (availableNumbers.isEmpty()) {
            throw new IllegalStateException("No numbers left");
        }
        int number = availableNumbers.remove(0);
        calledNumbers.add(number);
        return number;
    }

    @Override
    public List<Integer> getCalledNumbers() {
        return new ArrayList<>(calledNumbers);
    }

    @Override
    public boolean hasMoreNumbers() {
        return !availableNumbers.isEmpty();
    }

    @Override
    public void reset() {
        availableNumbers.clear();
        calledNumbers.clear();
        for (int number = 1; number <= 75; number++) {
            availableNumbers.add(number);
        }
        Collections.shuffle(availableNumbers);
    }
}
