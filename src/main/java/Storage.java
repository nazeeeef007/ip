import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeParseException;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void save(ArrayList<Task> tasks) {
        try {
            Files.createDirectories(Paths.get("./data"));
            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                lines.add(t.toFileFormat());
            }
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            System.out.println(" Error saving tasks: " + e.getMessage());
        }
    }

    public ArrayList<Task> load() throws MiloException {
        Path path = Paths.get(filePath);
        ArrayList<Task> taskList = new ArrayList<>();
        if (!Files.exists(path)) return taskList;

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split(" \\| ");
                Task task = parseLineToTask(parts);
                if (task != null) {
                    if (parts[1].equals("1")) task.markAsDone();
                    taskList.add(task);
                }
            }
        } catch (IOException e) {
            throw new MiloException("Could not load file.");
        }
        return taskList;
    }

    private Task parseLineToTask(String[] parts) {
        try {
            switch (parts[0]) {
                case "T": return new Todo(parts[2]);
                case "D": return new Deadline(parts[2], parts[3]);
                case "E": return new Event(parts[2], parts[3], parts[4]);
                default: return null;
            }
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}