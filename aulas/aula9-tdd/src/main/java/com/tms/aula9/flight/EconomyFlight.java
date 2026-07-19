package com.tms.aula9.flight;

public class EconomyFlight extends Flight {
    public EconomyFlight(String id, int maxPassengers) {
        super(id, maxPassengers);
    }

    @Override
    public boolean addPassenger(Passenger passenger) {
        return addPassengerInternal(passenger);
    }

    @Override
    public boolean removePassenger(Passenger passenger) {
        return removePassengerInternal(passenger);
    }
}
