# Hotel Reservation CLI

A small Java CLI app for checking hotel room availability and searching over a date range.

---

## Requirements

* Java 21
* IntelliJ IDEA (or any IDE that supports Java/Gradle)

---

## How to Run

This application expects two required arguments:

```bash
java -jar path/to/hotel-reservation-1.0-SNAPSHOT-all.jar --hotels path/to/hotels.json --bookings path/to/bookings.json
```
ex.:
```bash 
java -jar build/libs/hotel-reservation-1.0-SNAPSHOT-all.jar --hotels .\src\main\resources\hotels.json --bookings .\src\main\resources\bookings.json
```

* You must first build the project using Gradle:

```bash
./gradlew shadowJar
```

* This will generate a fat JAR with all dependencies:

```
build/libs/hotel-reservation-1.0-SNAPSHOT-all.jar
```

* The application will read hotel and booking data from the JSON files you provide via command-line arguments.

**Note:**

There is also a commented-out alternative version in `Main.java` that uses files from the `resources` directory for easier launching inside an IDE. This is useful when running without arguments in a development environment, but is disabled by default.

---

## Example Commands (input in console)

```
Availability(H1, 20250901, SGL)
Availability(H1, 20250901-20250903, DBL)
Search(H1, 30, SGL)
```

Leave blank to exit.

---

## Architecture Notes

The project uses a layered design with clear separation between repositories, services, and CLI interaction.

* **Repositories** are read-only and file-based (accepting file paths).
* **Services** contain all business logic (filtering, availability calculations).
* The repository layer is kept minimal and free of logic for easier future replacement (e.g., SQL, NoSQL, caching).
* **No caching** is currently used — data is read from files each time to keep the implementation simple.

This allows the app to be easily adapted to use other data sources, such as:

* SQL databases (PostgreSQL, MySQL)
* NoSQL stores (Redis, MongoDB)
* In-memory caching (if needed)

---

## Error Handling and Logging

There is currently no integrated logging or error reporting layer.

* Exceptions are thrown directly and shown in the console
* If the project grows (e.g., as a backend service), proper logging (SLF4J + Logback) and structured error handling should be added

---

## Console Interface Notes

The `ConsoleAppRunner` class serves as a basic interface to test and demonstrate functionality.

* It supports only minimal command parsing
* Input validation is basic
* It was not designed to be a production-ready CLI tool

In a real-world setting, this could be replaced with a REST API, web UI, or more robust CLI parser.

---

## Testing

The project includes both **unit tests** and **integration tests** (that load data from JSON files and test full interaction).

```bash
./gradlew test
```

---

## Future Improvements

* Input validation and better error feedback
* Logging and structured diagnostics
* Better command parsing (e.g. using a CLI parser library)
* Replace file-based data source with a database
* Expose functionality via HTTP API or frontend UI

---

## Use of AI Tools

General-purpose assistants (such as СhatGPT) were used during development to support:

* Reviewing and refining architectural choices
* Improving clarity in code structure and documentation
* Aligning with widely accepted design principles

These tools served as lightweight, non-intrusive helpers throughout development. Their use supported consistency and simplicity without replacing manual understanding or decision-making.
