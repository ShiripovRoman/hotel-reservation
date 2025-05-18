# Hotel Reservation CLI

A small Java CLI app for checking hotel room availability and search over a date range.

---

## Requirements

- Java 21+
- IntelliJ IDEA (or any IDE that supports Gradle)

---

## How to Run

1. Clone this repo
2. Open it in IDE
3. Navigate to `Main.java` (`com.shiriro.Main`)
4. Right-click → Run

The app will read from:

- `hotels.json`
- `bookings.json`

Make sure these files are in the resources of the project.

---

## Example Commands (input in console)
Availability(H1, 20250901, SGL)
Availability(H1, 20250901-20250903, DBL)
Search(H1, 30, SGL)
Leave blank to exit.

---

## Run Tests

./gradlew test