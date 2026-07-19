package com.tms.aula9.flight;

import java.util.Objects;

public class Passenger {
    private final String name;
    private final boolean vip;

    public Passenger(String name, boolean vip) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Passenger name required");
        }
        this.name = name;
        this.vip = vip;
    }

    public boolean isVip() {
        return vip;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Passenger passenger)) {
            return false;
        }
        return Objects.equals(name, passenger.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
