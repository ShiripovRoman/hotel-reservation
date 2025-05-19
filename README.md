# Hotel Reservation CLI

A small Java CLI app for checking hotel room availability and searching over a date range.

---

## Requirements

* Java 21
* IntelliJ IDEA (or any IDE that supports Java/Gradle)

---

## How to Run

This application is designed to be run directly from an IDE.

1. Clone the repository
2. Open it in IntelliJ IDEA (or any Java-supporting IDE)
3. Navigate to `Main.java` (`com.shiriro.Main`)
4. Right-click → Run

This setup is intentional: there's no need to overengineer a launcher or add additional runtime dependencies. Since the primary goal of this project is to demonstrate design and code structure, running it inside an IDE is the most straightforward and practical approach. It also makes it easier to inspect and evaluate the code in a real development environment.

If needed, the project could be extended later with a build script and launcher for command-line execution.

The app will read from:

* `hotels.json`
* `bookings.json`

These files should be located in the `resources` folder.

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

* **Repositories** are read-only and file-based (`hotels.json`, `bookings.json`).
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
