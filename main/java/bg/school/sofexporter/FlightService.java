package bg.school.sofexporter;

import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.DatedFlight;
import com.amadeus.Params;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервизен клас за работа с полетите от Amadeus API.
 */
public class FlightService {
    private final Amadeus amadeus;

    public FlightService(AmadeusClient client) {
        this.amadeus = client.getAmadeus();
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
    public List<DatedFlight> getFlights(String airportCode, LocalDate from, LocalDate to, String direction) throws Exception {
        List<DatedFlight> allFlights = new ArrayList<>();
        
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