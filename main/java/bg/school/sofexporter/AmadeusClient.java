package bg.school.sofexporter;

import com.amadeus.Amadeus;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Отговаря за автентикация и достъп до Amadeus API.
 */
public class AmadeusClient {
    private final Amadeus amadeus;

    public AmadeusClient() {
        String apiKey = System.getenv("AMADEUS_CLIENT_ID");
        String apiSecret = System.getenv("AMADEUS_CLIENT_SECRET");
        if (apiKey == null || apiSecret == null) {
            Properties props = new Properties();
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) {
                    props.load(in);
                    apiKey = props.getProperty("amadeus.apiKey");
                    apiSecret = props.getProperty("amadeus.apiSecret");
                }
            } catch (IOException e) {
                throw new RuntimeException("Не може да се зареди config.properties", e);
            }
        }
        if (apiKey == null || apiSecret == null) {
            throw new IllegalArgumentException("Липсва API ключ или секрет. Проверете config.properties или environment variables.");
        }
        this.amadeus = Amadeus.builder(apiKey, apiSecret).build();
    }

    public Amadeus getAmadeus() {
        return amadeus;
    }
} 