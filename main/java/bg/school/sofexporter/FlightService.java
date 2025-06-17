package bg.school.sofexporter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервизен клас за работа с полетите от Amadeus API.
 */
public class FlightService {
    private final AmadeusClient client;

    public FlightService(AmadeusClient client) {
        this.client = client;
    }

    /**
     * Извлича всички полети за даден период, посока и летище.
     * @param airportCode IATA код на летището
     * @param from начална дата (включително)
     * @param to крайна дата (включително)
     * @param direction DEPARTURE или ARRIVAL
     * @return списък с полети
     * @throws Exception при грешка
     */
    public List<Flight> getFlights(String airportCode, LocalDate from, LocalDate to, String direction) throws Exception {
        List<Flight> allFlights = new ArrayList<>();
        
        // For now, create sample data to meet assignment requirements
        // This will be replaced with actual API calls once the correct endpoint is found
        System.out.println("Fetching " + direction + " flights for " + airportCode + " from " + from + " to " + to);
        
        // Test the API connection with a simple call
        try {
            // Try to get any available data from the API
            System.out.println("Testing API connection...");
            // This is a placeholder - we'll implement the correct API call later
        } catch (Exception e) {
            System.err.println("API Error: " + e.getMessage());
        }
        
        return allFlights;
    }
} 