package com.tms.aula8.examples;

import java.time.LocalTime;

public class TimeDisplay {
    private final TimeProvider timeProvider;

    public TimeDisplay(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public String getCurrentTimeAsHtmlFragment() {
        LocalTime time = timeProvider.now();
        if (time.getHour() == 0 && time.getMinute() == 0) {
            return "<span class=\"tinyBoldText\">Midnight</span>";
        }
        return "<span class=\"tinyBoldText\">" + time + "</span>";
    }
}
