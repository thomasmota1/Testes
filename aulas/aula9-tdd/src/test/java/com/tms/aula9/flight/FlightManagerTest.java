package com.tms.aula9.flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightManagerTest {
    private FlightManager manager;

    @BeforeEach
    void setUp() {
        manager = new FlightManager();
        manager.registerFlight(new EconomyFlight("EC001", 100));
        manager.registerFlight(new PremiumFlight("PR001", 20));
    }

    @Test
    void shouldBookPassengerAndShowAvailableSeats() {
        assertTrue(manager.bookPassenger("EC001", new Passenger("John", false)));
        assertEquals(99, manager.getAvailableSeats("EC001"));
    }

    @Test
    void shouldRejectUnknownFlight() {
        assertThrows(IllegalArgumentException.class, () -> manager.bookPassenger("INVALID", new Passenger("John", false)));
    }
}
