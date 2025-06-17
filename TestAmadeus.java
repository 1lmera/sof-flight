import com.amadeus.Amadeus;

public class TestAmadeus {
    public static void main(String[] args) {
        System.out.println("Testing Amadeus SDK...");
        Amadeus amadeus = Amadeus.builder("test", "test").build();
        System.out.println("Amadeus instance created successfully");
    }
} 