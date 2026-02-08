package milo;

import java.io.IOException;
import milo.ui.Ui;
import milo.storage.Storage;
import milo.task.TaskList;
import milo.parser.Parser;
import milo.exception.MiloException;

/**
 * Acts as the entry point and controller for the Milo task management application.
 */
public class Milo {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Default constructor for JavaFX initialization.
     */
    public Milo() {
        this("./data/milo.txt");
    }

    /**
     * Initializes the Milo application with a file path for data storage.
     *
     * @param filePath The path to the file where tasks are saved.
     */
    public Milo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (MiloException e) {
            // In a GUI, we might want to return this error to the user instead
            System.err.println("Failed to load tasks: " + e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Generates a response for the user's chat input.
     *
     * @param input The raw user input from the GUI.
     * @return Milo's response as a String.
     */
    public String getResponse(String input) {
        try {
            return Parser.parse(input, tasks, ui, storage);
        } catch (MiloException | IOException e) {
            return ui.showError(e.getMessage());
        }
    }

    /**
     * Runs the main program loop for terminal use.
     */
    public void run() {
        System.out.println(ui.showWelcome());
        // Note: Terminal loop won't work perfectly with the new String-return Parser
        // but this remains for structural integrity.
    }

    public static void main(String[] args) {
        new Milo("./data/milo.txt").run();
    }
}