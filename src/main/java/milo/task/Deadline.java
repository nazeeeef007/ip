package milo.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 * Contains a description and a date by which the task must be completed.
 */
public class Deadline extends Task {
    protected LocalDate by;

    /**
     * Constructs a Deadline task with a description and a deadline date.
     *
     * @param description The description of the task.
     * @param by The date string in YYYY-MM-DD format.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDate.parse(by.trim());
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " +
                by.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }

    /**
     * Returns the deadline date of the task.
     *
     * @return The LocalDate representing the deadline.
     */
    public LocalDate getBy() {
        return this.by;
    }
}