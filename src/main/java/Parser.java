import java.io.IOException;
import java.time.format.DateTimeParseException;

public class Parser {

    /**
     * Parses the user input and executes the command.
     * * @param fullCommand The raw input from the user.
     * @param tasks The TaskList to modify.
     * @param ui The Ui to handle display.
     * @param storage The Storage to handle saving.
     * @return boolean true if it's an exit command, false otherwise.
     * @throws MiloException If command is invalid or arguments are missing.
     */
    public static boolean parse(String fullCommand, TaskList tasks, Ui ui, Storage storage) throws MiloException, IOException {
        String[] words = fullCommand.split(" ", 2);
        Command command = Command.fromString(words[0]);

        switch (command) {
            case BYE:
                return true; // Signal to exit the loop

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

    private static void handleTodo(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new MiloException("The description of a todo cannot be empty.");
        }
        Task t = new Todo(words[1]);
        tasks.addTask(t);
        ui.showAddedTask(t, tasks.getSize());
    }

    private static void handleDeadline(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2 || !words[1].contains(" /by ")) {
            throw new MiloException("Deadlines must include description and ' /by ' [yyyy-mm-dd].");
        }
        try {
            String[] parts = words[1].split(" /by ", 2);
            Task t = new Deadline(parts[0], parts[1]);
            tasks.addTask(t);
            ui.showAddedTask(t, tasks.getSize());
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
            Task t = new Event(eParts[0], timeParts[0], timeParts[1]);
            tasks.addTask(t);
            ui.showAddedTask(t, tasks.getSize());
        } catch (DateTimeParseException e) {
            throw new MiloException("Please use YYYY-MM-DD for event dates.");
        }
    }

    private static void handleMarkUnmark(String[] words, TaskList tasks, Ui ui, boolean isMark) throws MiloException {
        if (words.length < 2) throw new MiloException("Please specify the task number.");
        try {
            int idx = Integer.parseInt(words[1]) - 1;
            Task t = tasks.getTask(idx);
            if (isMark) {
                t.markAsDone();
                ui.showStatusChange(t, true);
            } else {
                t.unmarkDone();
                ui.showStatusChange(t, false);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new MiloException("Invalid task number.");
        }
    }

    private static void handleDelete(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2) throw new MiloException("Please specify the task number to delete.");
        try {
            int idx = Integer.parseInt(words[1]) - 1;
            Task removed = tasks.deleteTask(idx);
            ui.showRemovedTask(removed, tasks.getSize());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new MiloException("Invalid task number.");
        }
    }

    private static void handleFindDate(String[] words, TaskList tasks, Ui ui) throws MiloException {
        if (words.length < 2) throw new MiloException("Please specify a date in YYYY-MM-DD format.");
        ui.showTasksByDate(words[1].trim(), tasks.getTasks());
    }
}