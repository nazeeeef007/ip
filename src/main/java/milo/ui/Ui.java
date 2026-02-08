package milo.ui;

import milo.task.Task;
import milo.task.TaskList;
import milo.task.Event;
import milo.task.Deadline;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Handles the user interface of the application.
 * In this GUI version, methods return Strings to be displayed in the chat interface
 * instead of printing directly to the console.
 */
public class Ui {

    /**
     * Constructs a Ui object.
     */
    public Ui() {
        // Scanner is removed as input is now handled by the JavaFX TextField.
    }

    /**
     * Returns the welcome message to the user.
     * * @return The welcome string.
     */
    public String showWelcome() {
        return "Hello! I'm Milo\nWhat can I do for you?";
    }

    /**
     * Returns a horizontal divider line string (optional for GUI).
     * * @return The divider string.
     */
    public String showLine() {
        return "____________________________________________________________";
    }

    /**
     * Returns an error message to the user.
     *
     * @param message The error message to be returned.
     * @return Formatted error string.
     */
    public String showError(String message) {
        return " " + message;
    }

    /**
     * Returns the exit message when the application terminates.
     * * @return The exit string.
     */
    public String showExit() {
        return " Bye. Hope to see you again soon!";
    }

    /**
     * Returns a string containing all tasks currently in the task list.
     *
     * @param tasks The TaskList containing tasks to be displayed.
     * @return Formatted list of tasks.
     */
    public String showTaskList(TaskList tasks) {
        if (tasks.getSize() == 0) {
            return " Your list is currently empty.";
        }
        StringBuilder sb = new StringBuilder(" Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.getSize(); i++) {
            sb.append(" ").append(i + 1).append(".").append(tasks.getTask(i));
            if (i < tasks.getSize() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns matching tasks found via the 'find' command as a string.
     * * @param matchingTasks The list of tasks that matched the search.
     * @return Formatted string of matching tasks.
     */
    public String showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            return " No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder(" Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            sb.append(" ").append(i + 1).append(".").append(matchingTasks.get(i));
            if (i < matchingTasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns a message confirming a task was added.
     * * @param task The task that was added.
     * @param size The current size of the task list.
     * @return Feedback string.
     */
    public String showAddedTask(Task task, int size) {
        return " Got it. I've added this task:\n"
                + "   " + task + "\n"
                + " Now you have " + size + " tasks in the list.";
    }

    /**
     * Returns a message confirming a task was removed.
     * * @param task The task that was removed.
     * @param size The current size of the task list.
     * @return Feedback string.
     */
    public String showRemovedTask(Task task, int size) {
        return " Noted. I've removed this task:\n"
                + "   " + task + "\n"
                + " Now you have " + size + " tasks in the list.";
    }

    /**
     * Returns a message confirming a task's status was changed.
     * * @param task The task that was updated.
     * @param isMark True if marked as done, false otherwise.
     * @return Feedback string.
     */
    public String showStatusChange(Task task, boolean isMark) {
        if (isMark) {
            return " Nice! I've marked this task as done:\n   " + task;
        } else {
            return " OK, I've marked this task as not done yet:\n   " + task;
        }
    }

    /**
     * Finds and returns tasks that occur on a specific date.
     *
     * @param dateStr The date string in YYYY-MM-DD format.
     * @param list The list of tasks to search through.
     * @return Formatted string of tasks on that date.
     */
    public String showTasksByDate(String dateStr, ArrayList<Task> list) {
        try {
            LocalDate queryDate = LocalDate.parse(dateStr);
            StringBuilder sb = new StringBuilder(" Here are the tasks occurring on "
                    + queryDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":\n");
            int count = 0;
            for (Task task : list) {
                boolean isMatch = false;
                if (task instanceof Deadline && ((Deadline) task).getBy().equals(queryDate)) {
                    isMatch = true;
                }
                if (task instanceof Event && ((Event) task).getFrom().equals(queryDate)) {
                    isMatch = true;
                }

                if (isMatch) {
                    count++;
                    sb.append(" ").append(count).append(".").append(task).append("\n");
                }
            }
            if (count == 0) {
                return " No tasks found for this date.";
            }
            return sb.toString().trim();
        } catch (Exception e) {
            return " OOPS!!! Please use the format YYYY-MM-DD for searching.";
        }
    }
}