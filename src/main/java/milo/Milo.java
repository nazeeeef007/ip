package milo;

import java.io.IOException;
import milo.ui.Ui;
import milo.storage.Storage;
import milo.task.TaskList;
import milo.parser.Parser;
import milo.exception.MiloException;

public class Milo {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Initializes the milo.Milo application with a file path for data storage.
     * * @param filePath The path to the file where tasks are saved.
     */
    public Milo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs the main program loop, reading and executing commands until the user exits.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                if (fullCommand.trim().isEmpty()) {
                    continue;
                }

                ui.showLine();
                // This now potentially throws both milo.exception.MiloException and IOException
                isExit = Parser.parse(fullCommand, tasks, ui, storage);

            } catch (MiloException | IOException e) {
                // Multi-catch: handles both custom errors and file system errors
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        ui.showExit();
    }

    public static void main(String[] args) {

        new Milo("./data/milo.txt").run();
    }
}