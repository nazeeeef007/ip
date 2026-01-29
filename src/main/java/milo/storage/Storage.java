package milo.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import milo.exception.MiloException;
import milo.task.Deadline;
import milo.task.Event;
import milo.task.Task;
import milo.task.Todo;

/**
 * Handles loading and saving tasks to a file on the hard drive.
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
     *
     * @return An ArrayList of tasks loaded from the file.
     * @throws MiloException If there is an error creating the file structure.
     */
    public ArrayList<Task> load() throws MiloException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                return tasks;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    tasks.add(parseTaskFromFile(line));
                } catch (MiloException e) {
                    // Ignore corrupted lines
                }
            }
            scanner.close();
        } catch (IOException e) {
            throw new MiloException("Error loading storage file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves the current list of tasks to the storage file.
     *
     * @param tasks The list of tasks to be saved.
     * @throws IOException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(Paths.get(filePath).getParent());
        FileWriter writer = new FileWriter(filePath);
        for (Task task : tasks) {
            writer.write(task.toFileFormat() + System.lineSeparator());
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
        if (parts.length < 3) {
            throw new MiloException("Corrupted task line.");
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                throw new MiloException("Unknown task type.");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}