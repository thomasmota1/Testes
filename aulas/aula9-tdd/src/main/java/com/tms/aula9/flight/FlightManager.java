package com.tms.aula9.flight;

import java.util.HashMap;
import java.util.Map;

public class FlightManager {
    private final Map<String, Flight> flights = new HashMap<>();

    public void registerFlight(Flight flight) {
        flights.put(flight.getId(), flight);
    }

    public boolean bookPassenger(String flightId, Passenger passenger) {
        return findFlight(flightId).addPassenger(passenger);
    }

    public boolean cancelBooking(String flightId, Passenger passenger) {
        return findFlight(flightId).removePassenger(passenger);
    }

    public int getAvailableSeats(String flightId) {
        Flight flight = findFlight(flightId);
        return flight.getMaxPassengers() - flight.getPassengersCount();
    }

    private Flight findFlight(String flightId) {
        Flight flight = flights.get(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("Flight not found: " + flightId);
        }
        return flight;
    }
}
