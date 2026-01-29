package milo.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that occurs during a specific time period.
 */
public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;

    /**
     * Constructs an Event task with a description, start date, and end date.
     *
     * @param description The description of the event.
     * @param from The start date string in YYYY-MM-DD format.
     * @param to The end date string in YYYY-MM-DD format.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDate.parse(from.trim());
        this.to = LocalDate.parse(to.trim());
    }

    /**
     * Returns the start date of the event.
     *
     * @return The LocalDate representing the start of the event.
     */
    public LocalDate getFrom() {
        return this.from;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: "
                + from.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + " to: " + to.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from + " | " + to;
    }
}