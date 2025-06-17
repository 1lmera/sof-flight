package bg.school.sofexporter;

import java.time.LocalDateTime;

/**
 * Simple Flight data class to represent flight information.
 * This replaces the problematic DatedFlight class from Amadeus SDK.
 */
public class Flight {
    private String carrierCode;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private String status;
    private String terminal;
    private String gate;

    public Flight(String carrierCode, String flightNumber, String origin, String destination,
                  LocalDateTime scheduledDeparture, LocalDateTime scheduledArrival, 
                  String status, String terminal, String gate) {
        this.carrierCode = carrierCode;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.status = status;
        this.terminal = terminal;
        this.gate = gate;
    }

    // Getters
    public String getCarrierCode() { return carrierCode; }
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getScheduledArrival() { return scheduledArrival; }
    public String getStatus() { return status; }
    public String getTerminal() { return terminal; }
    public String getGate() { return gate; }

    @Override
    public String toString() {
        return String.format("Flight{carrierCode='%s', flightNumber='%s', origin='%s', destination='%s'}", 
                           carrierCode, flightNumber, origin, destination);
    }
} 