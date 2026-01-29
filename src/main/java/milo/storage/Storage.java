package milo.storage;

import milo.task.Task;
import milo.task.Todo;
import milo.task.Event;
import milo.task.Deadline;
import milo.exception.MiloException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeParseException;

/**
 * Handles the loading and saving of task data to the hard drive.
 */
public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the current list of tasks to the file specified in the file path.
     *
     * @param tasks The list of tasks to be saved.
     * @throws IOException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(Paths.get("./data"));
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileFormat());
        }
        Files.write(Paths.get(filePath), lines);
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return An ArrayList of tasks loaded from the file.
     * @throws MiloException If there is an error reading the file data.
     */
    public ArrayList<Task> load() throws MiloException {
        Path path = Paths.get(filePath);
        ArrayList<Task> taskList = new ArrayList<>();

        if (!Files.exists(path)) {
            return taskList;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split(" \\| ");
                Task task = parseLineToTask(parts);

                if (task != null) {
                    if (parts[1].equals("1")) {
                        task.markAsDone();
                    }
                    taskList.add(task);
                }
            }
        } catch (IOException e) {
            throw new MiloException("Error: Could not load data from " + filePath);
        }
        return taskList;
    }

    private Task parseLineToTask(String[] parts) {
        try {
            switch (parts[0]) {
                case "T":
                    return new Todo(parts[2]);
                case "D":
                    return new Deadline(parts[2], parts[3]);
                case "E":
                    return new Event(parts[2], parts[3], parts[4]);
                default:
                    return null;
            }
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            return null; // Skip corrupted lines
        }
    }
}