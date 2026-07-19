package com.tms.aula9.flight;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightTest {
    @Nested
    class EconomyFlightTest {
        @Test
        void shouldAddAndRemoveRegularAndVipPassengers() {
            Flight flight = new EconomyFlight("EC001", 3);
            Passenger regular = new Passenger("John", false);
            Passenger vip = new Passenger("Jane", true);

            assertTrue(flight.addPassenger(regular));
            assertTrue(flight.addPassenger(vip));
            assertTrue(flight.removePassenger(regular));
            assertTrue(flight.removePassenger(vip));
        }

        @Test
        void shouldRejectDuplicateAndPassengerAboveLimit() {
            Flight flight = new EconomyFlight("EC001", 1);
            Passenger passenger = new Passenger("John", false);

            assertTrue(flight.addPassenger(passenger));
            assertFalse(flight.addPassenger(passenger));
            assertFalse(flight.addPassenger(new Passenger("Jane", false)));
            assertEquals(1, flight.getPassengersCount());
        }
    }

    @Nested
    class PremiumFlightTest {
        @Test
        void shouldAcceptOnlyVipAndNeverRemoveFromPremium() {
            Flight flight = new PremiumFlight("PR001", 2);
            Passenger regular = new Passenger("John", false);
            Passenger vip = new Passenger("Jane", true);

            assertFalse(flight.addPassenger(regular));
            assertTrue(flight.addPassenger(vip));
            assertFalse(flight.removePassenger(vip));
            assertEquals(1, flight.getPassengersCount());
        }
    }

    @ParameterizedTest
    @CsvSource({"EC001, false, true", "EC001, true, true", "PR001, false, false", "PR001, true, true"})
    void shouldApplyPassengerAdditionRules(String flightId, boolean vip, boolean expected) {
        Flight flight = flightId.startsWith("EC") ? new EconomyFlight(flightId, 10) : new PremiumFlight(flightId, 10);

        assertEquals(expected, flight.addPassenger(new Passenger("Test", vip)));
    }
}
