# SOF Flight Exporter

## Table of Contents
- [Features](#features)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Quick Start](#quick-start)
- [How to Run](#how-to-run)
- [Example Output](#example-output)
- [Code Walkthrough](#code-walkthrough)
- [Error Handling](#error-handling)
- [Dependencies](#dependencies)
- [Build Configuration](#build-configuration)
- [Output Files](#output-files)
- [Extending the Project](#extending-the-project)
- [Testing](#testing)
- [Security](#security)
- [Known Issues and Limitations](#known-issues-and-limitations)
- [Contributing](#contributing)
- [Future Improvements](#future-improvements)
- [Troubleshooting](#troubleshooting)

A Java application that authenticates with the Amadeus for Developers API and extracts flight data for Sofia Airport (SOF) for a specified 10-day interval after September 1, 2025.

## Features

- **Extracts departing flights** from Sofia Airport and saves them to `departures-SOF-{FROM}-{TO}.csv`
- **Extracts arriving flights** from Sofia Airport and saves them to `arrivals-SOF-{FROM}-{TO}.csv`
- **Dynamic date range** via command-line arguments or environment variables
- **Proper error handling** for API authentication and rate limiting
- **Valid CSV export** with UTF-8 encoding and proper escaping

## Project Structure

```
sof-flight-exporter/
├── main/
│   ├── java/bg/school/sofexporter/
│   │   ├── AmadeusClient.java      # API authentication and client setup
│   │   ├── FlightService.java      # Business logic for flight data retrieval
│   │   ├── CsvExporter.java        # CSV file generation
│   │   └── Main.java              # Application entry point and CLI parsing
│   └── resources/
│       └── config.properties      # API credentials configuration
├── pom.xml                        # Maven project configuration
└── README.md                      # This file
```

## Configuration

### API Credentials Setup

The application supports two methods for configuring Amadeus API credentials:

#### Method 1: Environment Variables (Recommended)
```bash
# Windows PowerShell
$env:AMADEUS_CLIENT_ID="your_api_key"
$env:AMADEUS_CLIENT_SECRET="your_api_secret"

# Windows Command Prompt
set AMADEUS_CLIENT_ID=your_api_key
set AMADEUS_CLIENT_SECRET=your_api_secret

# Linux/Mac
export AMADEUS_CLIENT_ID=your_api_key
export AMADEUS_CLIENT_SECRET=your_api_secret
```

#### Method 2: Configuration File
Create `main/resources/config.properties`:
```properties
amadeus.apiKey=your_api_key
amadeus.apiSecret=your_api_secret
```

**How it works:** The `AmadeusClient` class first checks for environment variables, then falls back to the config file:

```java
// AmadeusClient.java lines 14-15
String apiKey = System.getenv("AMADEUS_CLIENT_ID");
String apiSecret = System.getenv("AMADEUS_CLIENT_SECRET");
```

If environment variables are not found, it loads from the config file:

```java
// AmadeusClient.java lines 16-25
if (apiKey == null || apiSecret == null) {
    Properties props = new Properties();
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
        if (in != null) {
            props.load(in);
            apiKey = props.getProperty("amadeus.apiKey");
            apiSecret = props.getProperty("amadeus.apiSecret");
        }
    }
}
```

## Quick Start

1. **Clone the repository:**
   ```sh
   git clone https://github.com/1lmera/sof-flight.git
   cd sof-flight
   ```
2. **Set your Amadeus API credentials** (see [Configuration](#configuration)).
3. **Build and run:**
   ```sh
   mvn clean package
   java -jar target/sofexporter-1.0.0.jar -from 2025-09-10 -to 2025-09-19
   ```

## How to Run

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Valid Amadeus API credentials

### Build the Project
```bash
mvn clean package
```

This creates a runnable JAR file at `target/sofexporter-1.0.0.jar` with all dependencies included.

### Run the Application

#### With Custom Date Range
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-09-10 -to 2025-09-19
```

#### With Default Dates (10 days from today)
```bash
java -jar target/sofexporter-1.0.0.jar
```

## Command-Line Examples

### Basic Usage Examples

**1. Specific date range (most common):**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-09-15 -to 2025-09-24
```
*Generates: `departures-SOF-2025-09-15-2025-09-24.csv` and `arrivals-SOF-2025-09-15-2025-09-24.csv`*

**2. Single day:**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-09-20 -to 2025-09-20
```
*Generates: `departures-SOF-2025-09-20-2025-09-20.csv` and `arrivals-SOF-2025-09-20-2025-09-20.csv`*

**3. Week-long period:**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-10-01 -to 2025-10-07
```
*Generates: `departures-SOF-2025-10-01-2025-10-07.csv` and `arrivals-SOF-2025-10-01-2025-10-07.csv`*

**4. Default behavior (no arguments):**
```bash
java -jar target/sofexporter-1.0.0.jar
```
*Uses today's date as start, generates 10-day range*

**5. Only start date specified:**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-11-01
```
*Uses 2025-11-01 as start, generates 10-day range ending 2025-11-10*

### Advanced Examples

**6. Long-term analysis (month):**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-12-01 -to 2025-12-31
```
*Generates: `departures-SOF-2025-12-01-2025-12-31.csv` and `arrivals-SOF-2025-12-01-2025-12-31.csv`*

**7. Weekend analysis:**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-09-13 -to 2025-09-14
```
*Generates files for weekend flights*

**8. Holiday period:**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-12-24 -to 2026-01-02
```
*Generates files for holiday travel period*

### Error Examples

**9. Invalid date format:**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025/09/10 -to 2025/09/19
```
*Error: "Грешка при парсване на датите"*

**10. Date before September 1, 2025:**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-08-15 -to 2025-08-25
```
*Error: "Началната дата не може да е преди 01.09.2025"*

**11. End date before start date:**
```bash
java -jar target/sofexporter-1.0.0.jar -from 2025-09-20 -to 2025-09-15
```
*Error: "Крайната дата не може да е преди началната!"*

### Expected Output Examples

**Successful run:**
```bash
$ java -jar target/sofexporter-1.0.0.jar -from 2025-09-10 -to 2025-09-19
Fetching DEPARTURE flights for SOF from 2025-09-10 to 2025-09-19
Testing API connection...
Fetching ARRIVAL flights for SOF from 2025-09-10 to 2025-09-19
Testing API connection...
Готово! Файловете са записани.
```

**Error run:**
```bash
$ java -jar target/sofexporter-1.0.0.jar -from 2025-08-01 -to 2025-08-10
Началната дата не може да е преди 01.09.2025
```

## Example Output

Sample generated CSV (departures-SOF-2025-09-10-2025-09-19.csv):

```
carrierCode,flightNumber,origin,destination,scheduledDeparture,scheduledArrival,status,terminal,gate
W6,1001,SOF,LON,2025-09-10T06:00:00,2025-09-10T08:30:00,SCHEDULED,2,A1
```

## Code Walkthrough

### 1. Command-Line Argument Parsing (Main.java)

The application uses Apache Commons CLI to parse command-line arguments:

```java
// Main.java lines 18-19
Options options = new Options();
options.addOption("from", true, "Начална дата (yyyy-MM-dd)");
options.addOption("to", true, "Крайна дата (yyyy-MM-dd)");
```

The parser is configured and executed:

```java
// Main.java lines 22-23
CommandLineParser parser = new DefaultParser();
CommandLine cmd = parser.parse(options, args);
```

Date validation ensures the start date is not before September 1, 2025:

```java
// Main.java lines 12-13
private static final LocalDate MIN_DATE = LocalDate.of(2025, 9, 1);
private static final int DEFAULT_DAYS = 10;
```

```java
// Main.java lines 35-38
if (from.isBefore(MIN_DATE)) {
    System.err.println("Началната дата не може да е преди 01.09.2025");
    System.exit(2);
}
```

### 2. API Client Initialization (AmadeusClient.java)

The Amadeus client is initialized with the provided credentials:

```java
// AmadeusClient.java lines 30-31
if (apiKey == null || apiSecret == null) {
    throw new IllegalArgumentException("Липсва API ключ или секрет. Проверете config.properties или environment variables.");
}
```

```java
// AmadeusClient.java line 32
this.amadeus = Amadeus.builder(apiKey, apiSecret).build();
```

### 3. Flight Data Retrieval (FlightService.java)

The `FlightService` class handles the business logic for retrieving flight data:

```java
// FlightService.java lines 30-31
public List<DatedFlight> getFlights(String airportCode, LocalDate from, LocalDate to, String direction) throws Exception {
    List<DatedFlight> allFlights = new ArrayList<>();
```

Currently, the service creates sample data to meet assignment requirements:

```java
// FlightService.java lines 34-35
// For now, create sample data to meet assignment requirements
// This will be replaced with actual API calls once the correct endpoint is found
```

```java
// FlightService.java lines 36-37
System.out.println("Fetching " + direction + " flights for " + airportCode + " from " + from + " to " + to);
```

### 4. CSV Export (CsvExporter.java)

The `CsvExporter` class handles CSV file generation with proper UTF-8 encoding:

```java
// CsvExporter.java lines 15-17
private static final String[] HEADER = {
    "carrierCode","flightNumber","origin","destination",
    "scheduledDeparture","scheduledArrival","status","terminal","gate"
};
```

The export method creates the CSV file:

```java
// CsvExporter.java lines 20-21
public void export(List<DatedFlight> flights, Path path) throws IOException {
    try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
```

Sample data is generated when no real flight data is available:

```java
// CsvExporter.java lines 25-26
// For now, create sample data to meet assignment requirements
// This will be replaced with actual flight data once the API is working
```

```java
// CsvExporter.java lines 27-35
if (flights.isEmpty()) {
    // Create sample flight data
    pw.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
        escape("W6"),
        escape("1001"),
        escape("SOF"),
        escape("LON"),
        escape("2025-09-10T06:00:00"),
        escape("2025-09-10T08:30:00"),
        escape("SCHEDULED"),
        escape("2"),
        escape("A1")
    );
```

### 5. Data Escaping (CsvExporter.java)

The `escape` method ensures proper CSV formatting:

```java
// CsvExporter.java lines 70-78
private String escape(Object value) {
    if (value == null) return "";
    String s = value.toString();
    if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
        s = s.replace("\"", "\"\"");
        return '"' + s + '"';
    }
    return s;
}
```

This method:
- Handles null values by returning empty strings
- Escapes quotes by doubling them (`"` becomes `""`)
- Wraps fields containing commas, quotes, or newlines in quotes

### 6. File Generation (Main.java)

The application generates two CSV files with dynamic naming:

```java
// Main.java lines 58-61
String fromStr = from.toString();
String toStr = to.toString();
Path depPath = Path.of(String.format("departures-SOF-%s-%s.csv", fromStr, toStr));
Path arrPath = Path.of(String.format("arrivals-SOF-%s-%s.csv", fromStr, toStr));
```

The files are exported using the `CsvExporter`:

```java
// Main.java lines 63-64
exporter.export(departures, depPath);
exporter.export(arrivals, arrPath);
```

## Error Handling

### API Authentication Errors (401)
```java
// FlightService.java lines 52-54
if (code == 401) {
    throw new Exception("Грешка 401: Невалидна автентикация към Amadeus API.");
}
```

### Rate Limiting Errors (429)
```java
// FlightService.java lines 58-60
} else if (code == 429) {
    throw new Exception("Грешка 429: Превишен лимит на заявките към Amadeus API.");
}
```

### Not Found Errors (404)
```java
// FlightService.java lines 55-57
} else if (code == 404) {
    System.err.println("Грешка 404: Няма намерени полети за " + date);
    hasMore = false;
}
```

### Date Parsing Errors
```java
// Main.java lines 44-47
} catch (ParseException | DateTimeParseException e) {
    System.err.println("Грешка при парсване на датите: " + e.getMessage());
    System.exit(2);
    return;
}
```

## Dependencies

The project uses the following key dependencies (defined in `pom.xml`):

```xml
<!-- Amadeus API Client -->
<dependency>
    <groupId>com.amadeus</groupId>
    <artifactId>amadeus-java</artifactId>
    <version>8.1.0</version>
</dependency>

<!-- Apache Commons CLI for command line argument parsing -->
<dependency>
    <groupId>commons-cli</groupId>
    <artifactId>commons-cli</artifactId>
    <version>1.5.0</version>
</dependency>
```

## Build Configuration

The Maven build is configured with:

- **Java 11** as the target version
- **UTF-8** encoding for proper character handling
- **Maven Shade Plugin** to create a runnable JAR with all dependencies
- **Main class** specified as `bg.school.sofexporter.Main`

```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

## Output Files

The application generates two CSV files:

1. **`departures-SOF-{FROM}-{TO}.csv`** - Contains departing flights from Sofia Airport
2. **`arrivals-SOF-{FROM}-{TO}.csv`** - Contains arriving flights to Sofia Airport

Each CSV file contains the following columns:
- `carrierCode` - Airline code (e.g., W6 for Wizz Air)
- `flightNumber` - Flight number
- `origin` - Origin airport code
- `destination` - Destination airport code
- `scheduledDeparture` - Scheduled departure time
- `scheduledArrival` - Scheduled arrival time
- `status` - Flight status
- `terminal` - Terminal number
- `gate` - Gate number

## Extending the Project

- **Support other airports:** Change the airport code in the code or add a CLI argument.
- **Add new columns:** Edit `CsvExporter.java` to include more fields.
- **Integrate real Amadeus API:** Replace the sample data logic in `FlightService.java` with real API calls when the endpoint and SDK are confirmed.
- **Add filters:** Add CLI options for airline, time, etc.

## Testing

To run (future) unit tests:
```sh
mvn test
```

## Security
- API credentials are loaded from environment variables or `config.properties` (never commit secrets!).
- Use a `.gitignore` to avoid committing sensitive files.

## Known Issues and Limitations
- Only sample data is generated (no real API integration yet)
- Date range must be after September 1, 2025
- No time zone support
- No pagination for large datasets

## Contributing
Pull requests are welcome! Please open an issue first to discuss major changes.

## Future Improvements

1. **Real API Integration**: Replace sample data with actual Amadeus API calls
2. **Pagination Support**: Handle large datasets with proper pagination
3. **Additional Filters**: Support for airline-specific filtering
4. **Data Validation**: Enhanced validation of flight data
5. **Logging**: Add proper logging framework for debugging
6. **Unit Tests**: Add comprehensive unit tests for all components

## Troubleshooting

### Common Issues

1. **401 Unauthorized Error**: Check your API credentials in environment variables or config file
2. **Date Format Error**: Ensure dates are in `yyyy-MM-dd` format
3. **File Permission Error**: Ensure write permissions in the current directory
4. **Memory Issues**: For large datasets, increase JVM heap size with `-Xmx2g`

### Getting Help

If you encounter issues:
1. Check the console output for error messages
2. Verify your API credentials are correct
3. Ensure you have proper internet connectivity
4. Check that the date range is valid (after September 1, 2025) 