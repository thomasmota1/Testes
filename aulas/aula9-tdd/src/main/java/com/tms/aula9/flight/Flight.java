package com.tms.aula9.flight;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Flight {
    private final String id;
    private final int maxPassengers;
    private final Set<Passenger> passengers = new HashSet<>();

    protected Flight(String id, int maxPassengers) {
        if (id == null || id.isBlank() || maxPassengers <= 0) {
            throw new IllegalArgumentException("Invalid flight");
        }
        this.id = id;
        this.maxPassengers = maxPassengers;
    }

    public String getId() {
        return id;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public int getPassengersCount() {
        return passengers.size();
    }

    public Set<Passenger> getPassengers() {
        return Collections.unmodifiableSet(passengers);
    }

    protected boolean addPassengerInternal(Passenger passenger) {
        if (passenger == null || passengers.size() >= maxPassengers || passengers.contains(passenger)) {
            return false;
        }
        return passengers.add(passenger);
    }

    protected boolean removePassengerInternal(Passenger passenger) {
        return passengers.remove(passenger);
    }

    public abstract boolean addPassenger(Passenger passenger);

    public abstract boolean removePassenger(Passenger passenger);
}
