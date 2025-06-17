package bg.school.sofexporter;

import com.amadeus.resources.DatedFlight;
import org.apache.commons.cli.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Главен клас за стартиране на приложението.
 */
public class Main {
    private static final LocalDate MIN_DATE = LocalDate.of(2025, 9, 1);
    private static final int DEFAULT_DAYS = 10;

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("from", true, "Начална дата (yyyy-MM-dd)");
        options.addOption("to", true, "Крайна дата (yyyy-MM-dd)");

        CommandLineParser parser = new DefaultParser();
        LocalDate from, to;
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("from")) {
                from = LocalDate.parse(cmd.getOptionValue("from"));
            } else {
                from = LocalDate.now();
            }
            if (cmd.hasOption("to")) {
                to = LocalDate.parse(cmd.getOptionValue("to"));
            } else {
                to = from.plusDays(DEFAULT_DAYS - 1);
            }
            if (from.isBefore(MIN_DATE)) {
                System.err.println("Началната дата не може да е преди 01.09.2025");
                System.exit(2);
            }
            if (to.isBefore(from)) {
                System.err.println("Крайната дата не може да е преди началната!");
                System.exit(2);
            }
        } catch (ParseException | DateTimeParseException e) {
            System.err.println("Грешка при парсване на датите: " + e.getMessage());
            System.exit(2);
            return;
        }

        try {
            AmadeusClient amadeusClient = new AmadeusClient();
            FlightService flightService = new FlightService(amadeusClient);
            CsvExporter exporter = new CsvExporter();

            List<DatedFlight> departures = flightService.getFlights("SOF", from, to, "DEPARTURE");
            List<DatedFlight> arrivals = flightService.getFlights("SOF", from, to, "ARRIVAL");

            String fromStr = from.toString();
            String toStr = to.toString();
            Path depPath = Path.of(String.format("departures-SOF-%s-%s.csv", fromStr, toStr));
            Path arrPath = Path.of(String.format("arrivals-SOF-%s-%s.csv", fromStr, toStr));

            exporter.export(departures, depPath);
            exporter.export(arrivals, arrPath);

            System.out.println("Готово! Файловете са записани.");
        } catch (Exception e) {
            System.err.println("Грешка: " + e.getMessage());
            System.exit(1);
        }
    }
} 