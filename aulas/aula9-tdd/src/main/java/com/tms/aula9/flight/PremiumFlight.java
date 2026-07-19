package com.tms.aula9.flight;

public class PremiumFlight extends Flight {
    public PremiumFlight(String id, int maxPassengers) {
        super(id, maxPassengers);
    }

    @Override
    public boolean addPassenger(Passenger passenger) {
        return passenger != null && passenger.isVip() && addPassengerInternal(passenger);
    }

    @Override
    public boolean removePassenger(Passenger passenger) {
        return false;
    }
}
