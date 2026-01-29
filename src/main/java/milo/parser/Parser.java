package milo.parser;

import milo.task.Task;
import milo.ui.Ui;
import milo.storage.Storage;
import milo.task.TaskList;
import milo.task.Todo;
import milo.task.Event;
import milo.task.Deadline;
import milo.exception.MiloException;
import milo.command.Command;

import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * Deciphers user input and translates it into specific actions for Milo.
 * Responsible for validating command arguments and coordinating with components.
 */
public class Parser {

    /**
     * Parses the user input and executes the corresponding command.
     *
     * @param fullCommand The raw input from the user.
     * @param tasks The TaskList to modify.
     * @param ui The Ui to handle display.
     * @param storage The Storage to handle saving.
     * @return boolean true if it's an exit command, false otherwise.
     * @throws MiloException If command is invalid or arguments are missing.
     * @throws IOException If there is an error saving data.
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

        storage.save(tasks.getTasks());
        return false;
    }

    private static void handleTodo(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new MiloException("The description of a todo cannot be empty.");
        }

        Task task = new Todo(words[1]);
        tasks.addTask(task);
        ui.showAddedTask(task, tasks.getSize());
    }

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

    private static void handleMarkUnmark(String[] words, TaskList tasks, Ui ui, boolean isMark)
            throws MiloException {
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

    private static void handleFindDate(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2) {
            throw new MiloException("Please specify a date in YYYY-MM-DD format.");
        }

        ui.showTasksByDate(words[1].trim(), tasks.getTasks());
    }
}