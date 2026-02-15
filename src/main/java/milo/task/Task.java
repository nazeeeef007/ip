package milo.task;
import java.time.LocalDateTime;

/**
 * Represents a generic task in the Milo application.
 * Serves as a base class for specific task types like Todo, Deadline, and Event.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * The task is initialized as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void unmarkDone() {
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task.
     * "X" indicates the task is done, while a space indicates it is not.
     *
     * @return A string representing the completion status.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns the task data formatted for file storage.
     *
     * @return A string formatted for the storage text file.
     */
    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the date/time associated with the task.
     * Overridden by subclasses that have dates (Deadline, Event).
     * * @return LocalDateTime of the task, or null if none exists.
     */
    public LocalDateTime getDateTime() {
        return null;
    }
}