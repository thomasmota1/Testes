package com.tms.aula8.bingo;

import java.util.List;

public interface Caller {
    int callNumber();

    List<Integer> getCalledNumbers();

    boolean hasMoreNumbers();

    void reset();
}
