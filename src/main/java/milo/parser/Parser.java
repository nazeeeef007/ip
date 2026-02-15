package milo.parser;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import milo.command.Command;
import milo.exception.MiloException;
import milo.storage.Storage;
import milo.task.Deadline;
import milo.task.Event;
import milo.task.Task;
import milo.task.TaskList;
import milo.task.Todo;
import milo.ui.Ui;

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
     * @return The response message to be displayed in the GUI.
     * @throws MiloException If the command is unrecognized or arguments are invalid.
     * @throws IOException If there is an error saving data to the file.
     */
    public static String parse(String fullCommand, TaskList tasks, Ui ui, Storage storage)
            throws MiloException, IOException {

        assert tasks != null : "TaskList tasks should not be null";
        assert ui != null : "Ui ui should not be null";
        assert storage != null : "Storage storage should not be null";

        String[] words = fullCommand.split(" ", 2);
        assert words.length > 0 : "Split command should have at least one part";

        Command command = Command.fromString(words[0]);
        assert command != null : "Command should never be null";

        String response;

        switch (command) {
            case BYE:
                return ui.showExit();

            case LIST:
                response = ui.showTaskList(tasks);
                break;

            case MARK:
                response = handleMarkUnmark(words, tasks, ui, true);
                break;

            case UNMARK:
                response = handleMarkUnmark(words, tasks, ui, false);
                break;

            case TODO:
                response = handleTodo(words, tasks, ui);
                break;

            case DEADLINE:
                response = handleDeadline(words, tasks, ui);
                break;

            case EVENT:
                response = handleEvent(words, tasks, ui);
                break;

            case DELETE:
                response = handleDelete(words, tasks, ui);
                break;

            case FIND_DATE:
                response = handleFindDate(words, tasks, ui);
                break;

            case FIND:
                response = handleFind(words, tasks, ui);
                break;

            case SORT:
                response = handleSort(words, tasks, ui);
                break;

            case UNKNOWN:
            default:
                throw new MiloException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }

        // Save after every command that modifies the list
        storage.save(tasks.getTasks());
        return response;
    }

    /**
     * Handles the sorting of tasks (C-Sort extension).
     */
    private static String handleSort(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new MiloException("Please specify sort type: 'sort name' or 'sort date'.");
        }

        String sortType = words[1].trim().toLowerCase();
        if (sortType.equals("name")) {
            tasks.sortAlphabetically();
            return "Sorted tasks alphabetically by description!";
        } else if (sortType.equals("date")) {
            tasks.sortChronologically();
            return "Sorted tasks chronologically by date! (Tasks without dates are at the bottom)";
        } else {
            throw new MiloException("Unknown sort type. Please use 'sort name' or 'sort date'.");
        }
    }

    /**
     * Handles the creation and addition of a Todo task.
     */
    private static String handleTodo(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new MiloException("The description of a todo cannot be empty.");
        }

        Task task = new Todo(words[1]);
        tasks.addTask(task);

        assert tasks.getSize() > 0 : "TaskList size should increase after adding a task";
        return ui.showAddedTask(task, tasks.getSize());
    }

    /**
     * Handles the creation and addition of a Deadline task.
     */
    private static String handleDeadline(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || !words[1].contains(" /by ")) {
            throw new MiloException("Deadlines must include description and ' /by ' [yyyy-mm-dd].");
        }

        try {
            String[] parts = words[1].split(" /by ", 2);
            assert parts.length == 2 : "Deadline split should result in 2 parts";

            Task task = new Deadline(parts[0], parts[1]);
            tasks.addTask(task);
            return ui.showAddedTask(task, tasks.getSize());
        } catch (DateTimeParseException e) {
            throw new MiloException("Please use the format YYYY-MM-DD for the date.");
        }
    }

    /**
     * Handles the creation and addition of an Event task.
     */
    private static String handleEvent(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || !words[1].contains(" /from ") || !words[1].contains(" /to ")) {
            throw new MiloException("Events must include description, ' /from ' and ' /to ' [yyyy-mm-dd].");
        }

        try {
            String[] eParts = words[1].split(" /from ", 2);
            assert eParts.length == 2 : "Event split /from should result in 2 parts";

            String[] timeParts = eParts[1].split(" /to ", 2);
            assert timeParts.length == 2 : "Event split /to should result in 2 parts";

            Task task = new Event(eParts[0], timeParts[0], timeParts[1]);
            tasks.addTask(task);
            return ui.showAddedTask(task, tasks.getSize());
        } catch (DateTimeParseException e) {
            throw new MiloException("Please use YYYY-MM-DD for event dates.");
        }
    }

    /**
     * Handles the marking or unmarking of a task as done.
     */
    private static String handleMarkUnmark(String[] words, TaskList tasks, Ui ui, boolean isMark) throws MiloException {
        if (words.length < 2) {
            throw new MiloException("Please specify the task number.");
        }
        try {
            int index = Integer.parseInt(words[1]) - 1;
            assert tasks.getSize() >= 0 : "TaskList size cannot be negative";

            Task task = tasks.getTask(index);

            if (isMark) {
                task.markAsDone();
                return ui.showStatusChange(task, true);
            } else {
                task.unmarkDone();
                return ui.showStatusChange(task, false);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new MiloException("Invalid task number.");
        }
    }

    /**
     * Handles the deletion of a task from the list.
     */
    private static String handleDelete(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2) {
            throw new MiloException("Please specify the task number to delete.");
        }

        try {
            int index = Integer.parseInt(words[1]) - 1;
            int initialSize = tasks.getSize();

            Task removedTask = tasks.deleteTask(index);
            assert tasks.getSize() == initialSize - 1 : "TaskList size should decrease by 1";

            return ui.showRemovedTask(removedTask, tasks.getSize());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new MiloException("Invalid task number.");
        }
    }

    /**
     * Handles searching for tasks occurring on a specific date.
     */
    private static String handleFindDate(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2) {
            throw new MiloException("Please specify a date in YYYY-MM-DD format.");
        }
        String dateStr = words[1].trim();
        assert !dateStr.isEmpty() : "Search date string should not be empty after trim";

        return ui.showTasksByDate(dateStr, tasks.getTasks());
    }

    /**
     * Handles searching for tasks by keyword.
     */
    private static String handleFind(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new MiloException("Please specify a keyword to find.");
        }

        String keyword = words[1].trim();
        assert !keyword.isEmpty() : "Keyword should not be empty for search";

        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        return ui.showMatchingTasks(matchingTasks);
    }
}