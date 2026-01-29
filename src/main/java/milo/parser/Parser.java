package milo.parser;

import milo.task.Task;
import milo.ui.Ui;
import milo.storage.Storage;
import milo.task.TaskList;
import milo.task.Task;
import milo.task.Todo;
import milo.task.Event;
import milo.task.Deadline;
import milo.exception.MiloException;
import milo.command.Command;

import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * Deciphers user input and translates it into specific actions for Milo.
 * Responsible for validating command arguments and coordinating with TaskList, Ui, and Storage.
 */
public class Parser {

    /**
     * Parses the user input and executes the corresponding command.
     *
     * @param fullCommand The raw input string provided by the user.
     * @param tasks The TaskList to be manipulated based on the command.
     * @param ui The user interface to handle output messages.
     * @param storage The storage component to save changes after execution.
     * @return true if the command signals an exit (bye), false otherwise.
     * @throws MiloException If the command is unrecognized or arguments are invalid.
     * @throws IOException If there is an error saving data to the file.
     */
    public static boolean parse(String fullCommand, TaskList tasks, Ui ui, Storage storage)
            throws MiloException, IOException {
        String[] words = fullCommand.split(" ", 2);
        Command command = Command.fromString(words[0]);

        switch (command) {
            case BYE:
                return true;

            case LIST:
                ui.showTaskList(tasks);
                break;

            case MARK:
                handleMarkUnmark(words, tasks, ui, true);
                break;

            case UNMARK:
                handleMarkUnmark(words, tasks, ui, false);
                break;

            case TODO:
                handleTodo(words, tasks, ui);
                break;

            case DEADLINE:
                handleDeadline(words, tasks, ui);
                break;

            case EVENT:
                handleEvent(words, tasks, ui);
                break;

            case DELETE:
                handleDelete(words, tasks, ui);
                break;

            case FIND_DATE:
                handleFindDate(words, tasks, ui);
                break;

            case UNKNOWN:
            default:
                throw new MiloException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }

        // Save after every command that isn't BYE
        storage.save(tasks.getTasks());
        return false;
    }

    /**
     * Handles the creation and addition of a Todo task.
     *
     * @param words The split input containing the command and description.
     * @param tasks The TaskList to add the Todo to.
     * @param ui The user interface to show feedback.
     * @throws MiloException If the description is empty.
     */
    private static void handleTodo(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new MiloException("The description of a todo cannot be empty.");
        }

        Task task = new Todo(words[1]);
        tasks.addTask(task);
        ui.showAddedTask(task, tasks.getSize());
    }

    /**
     * Handles the creation and addition of a Deadline task.
     *
     * @param words The split input containing description and date.
     * @param tasks The TaskList to add the Deadline to.
     * @param ui The user interface to show feedback.
     * @throws MiloException If the format is incorrect or date is missing.
     */
    private static void handleDeadline(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || !words[1].contains(" /by ")) {
            throw new MiloException("Deadlines must include description and ' /by ' [yyyy-mm-dd].");
        }

        try {
            String[] parts = words[1].split(" /by ", 2);
            Task task = new Deadline(parts[0], parts[1]);
            tasks.addTask(task);
            ui.showAddedTask(task, tasks.getSize());
        } catch (DateTimeParseException e) {
            throw new MiloException("Please use the format YYYY-MM-DD for the date.");
        }
    }

    /**
     * Handles the creation and addition of an Event task.
     *
     * @param words The split input containing description and time range.
     * @param tasks The TaskList to add the Event to.
     * @param ui The user interface to show feedback.
     * @throws MiloException If the format is incorrect or dates are missing.
     */
    private static void handleEvent(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || !words[1].contains(" /from ") || !words[1].contains(" /to ")) {
            throw new MiloException("Events must include description, ' /from ' and ' /to ' [yyyy-mm-dd].");
        }

        try {
            String[] eParts = words[1].split(" /from ", 2);
            String[] timeParts = eParts[1].split(" /to ", 2);
            Task task = new Event(eParts[0], timeParts[0], timeParts[1]);
            tasks.addTask(task);
            ui.showAddedTask(task, tasks.getSize());
        } catch (DateTimeParseException e) {
            throw new MiloException("Please use YYYY-MM-DD for event dates.");
        }
    }

    /**
     * Handles the marking or unmarking of a task as done.
     *
     * @param words The split input containing the task index.
     * @param tasks The TaskList to search for the task.
     * @param ui The user interface to show feedback.
     * @param isMark True to mark as done, false to unmark.
     * @throws MiloException If the index is missing or invalid.
     */
    private static void handleMarkUnmark(String[] words, TaskList tasks, Ui ui, boolean isMark) throws MiloException {
        if (words.length < 2) {
            throw new MiloException("Please specify the task number.");
        }
        try {
            int index = Integer.parseInt(words[1]) - 1;
            Task task = tasks.getTask(index);

            if (isMark) {
                task.markAsDone();
                ui.showStatusChange(task, true);
            } else {
                task.unmarkDone();
                ui.showStatusChange(task, false);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new MiloException("Invalid task number.");
        }
    }

    /**
     * Handles the deletion of a task from the list.
     *
     * @param words The split input containing the task index to delete.
     * @param tasks The TaskList to remove the task from.
     * @param ui The user interface to show feedback.
     * @throws MiloException If the index is missing or invalid.
     */
    private static void handleDelete(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2) {
            throw new MiloException("Please specify the task number to delete.");
        }

        try {
            int index = Integer.parseInt(words[1]) - 1;
            Task removedTask = tasks.deleteTask(index);
            ui.showRemovedTask(removedTask, tasks.getSize());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new MiloException("Invalid task number.");
        }
    }

    /**
     * Handles searching for tasks occurring on a specific date.
     *
     * @param words The split input containing the search date.
     * @param tasks The TaskList to search within.
     * @param ui The user interface to display matching tasks.
     * @throws MiloException If the date is missing.
     */
    private static void handleFindDate(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2) {
            throw new MiloException("Please specify a date in YYYY-MM-DD format.");
        }

        if (words.length < 2) {
            throw new MiloException("Please specify a date in YYYY-MM-DD format.");
        }
        ui.showTasksByDate(words[1].trim(), tasks.getTasks());
    }
}