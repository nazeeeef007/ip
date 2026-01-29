package milo.ui;

import milo.task.Task;
import milo.task.TaskList;
import milo.task.Event;
import milo.task.Deadline;

import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Handles the user interface of the application.
 * Responsible for reading user input and displaying messages to the user.
 */
public class Ui {
    private final Scanner scanner;
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Constructs a Ui object and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows the welcome message to the user.
     */
    public void showWelcome() {
        System.out.println(DIVIDER + "\n Hello! I'm Milo\n What can I do for you?\n" + DIVIDER);
    }

    /**
     * Prints a horizontal divider line.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Reads the next line of input from the user.
     *
     * @return The raw string input entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Shows the exit message when the application terminates.
     */
    public void showExit() {
        System.out.println(" Bye. Hope to see you again soon!");
    }

    /**
     * Prints all tasks currently in the task list.
     *
     * @param tasks The TaskList containing tasks to be displayed.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.getSize(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.getTask(i));
        }
    }

    /**
     * Confirms that a task has been successfully added.
     *
     * @param t The task that was added.
     * @param size The new total number of tasks in the list.
     */
    public void showAddedTask(Task t, int size) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + size + " tasks in the list.");
    }

    /**
     * Confirms that a task has been successfully removed.
     *
     * @param t The task that was removed.
     * @param size The remaining total number of tasks in the list.
     */
    public void showRemovedTask(Task t, int size) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + size + " tasks in the list.");
    }

    /**
     * Displays the status change of a task (marked/unmarked).
     *
     * @param t The task whose status was changed.
     * @param isMark True if the task was marked as done, false if unmarked.
     */
    public void showStatusChange(Task t, boolean isMark) {
        if (isMark) {
            System.out.println(" Nice! I've marked this task as done:\n   " + t);
        } else {
            System.out.println(" OK, I've marked this task as not done yet:\n   " + t);
        }
    }

    /**
     * Finds and displays tasks that occur on a specific date.
     *
     * @param dateStr The date string in YYYY-MM-DD format.
     * @param list The list of tasks to search through.
     */
    public void showTasksByDate(String dateStr, ArrayList<Task> list) {
        try {
            LocalDate queryDate = LocalDate.parse(dateStr);
            System.out.println(" Here are the tasks occurring on "
                    + queryDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
            int count = 0;
            for (Task t : list) {
                boolean isMatch = false;
                if (t instanceof Deadline && ((Deadline) t).getBy().equals(queryDate)) {
                    isMatch = true;
                }
                if (t instanceof Event && ((Event) t).getFrom().equals(queryDate)) {
                    isMatch = true;
                }

                if (isMatch) {
                    count++;
                    System.out.println(" " + count + "." + t);
                }
            }
            if (count == 0) {
                System.out.println(" No tasks found for this date.");
            }
        } catch (Exception e) {
            System.out.println(" OOPS!!! Please use the format YYYY-MM-DD for searching.");
        }
    }
}