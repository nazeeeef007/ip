package milo.storage;

import milo.task.Task;
import milo.exception.MiloException;
import milo.task.Todo;
import milo.task.Event;
import milo.task.Deadline;
import milo.task.Event;
import milo.exception.MiloException;
import milo.exception.MiloException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading and saving tasks to a file on the hard drive.
 * Responsible for converting task data between file format and application objects.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a Storage object with a specified file path.
     *
     * @param filePath The path of the file where tasks are stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     * If the file or directory does not exist, they will be created.
     *
     * @return An ArrayList of tasks loaded from the file.
     * @throws IOException If there is an error reading the file or creating the directory.
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return tasks;
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                tasks.add(parseTaskFromFile(line));
            } catch (MiloException e) {
                // Skips corrupted lines in the data file
            }
        }
        scanner.close();
        return tasks;
    }

    /**
     * Saves the current list of tasks to the storage file.
     *
     * @param tasks The list of tasks to be saved.
     * @throws IOException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(Paths.get("./data"));
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toFileFormat());
        }
        writer.close();
    }

    /**
     * Parses a single line from the storage file into a Task object.
     *
     * @param line The raw string line from the file.
     * @return The corresponding Task object.
     * @throws MiloException If the file format is unrecognized or corrupted.
     */
    private Task parseTaskFromFile(String line) throws MiloException {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task t;
        switch (type) {
            case "T":
                t = new Todo(description);
                break;
            case "D":
                t = new Deadline(description, parts[3]);
                break;
            case "E":
                t = new Event(description, parts[3], parts[4]);
                break;
            default:
                throw new MiloException("Unknown task type in file.");
        }
        return taskList;
    }

        if (isDone) {
            t.markAsDone();
        }
        return t;
    }
}