import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.*;
import java.io.*;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class Milo {
    private static final String FILE_PATH = "./data/milo.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> taskList = new ArrayList<>();
        loadTasks(taskList);
        String name = "Milo";

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm " + name + "\n What can I do for you?");
        System.out.println("____________________________________________________________");

        boolean isExit = false;
        while (!isExit) {
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) continue;

            String[] words = input.split(" ", 2);
            Command command = Command.fromString(words[0]);

            System.out.println("____________________________________________________________");
            try {
                switch (command) {
                    case BYE:
                        isExit = true;
                        break;

                    case LIST:
                        System.out.println(" Here are the tasks in your list:");
                        for (int i = 0; i < taskList.size(); i++) {
                            System.out.println(" " + (i + 1) + "." + taskList.get(i));
                        }
                        break;

                    case MARK:
                        handleMarkUnmark(input, taskList, true);
                        break;

                    case UNMARK:
                        handleMarkUnmark(input, taskList, false);
                        break;

                    case TODO:
                        if (words.length < 2 || words[1].trim().isEmpty()) {
                            throw new MiloException("OOPS!!! The description of a todo cannot be empty.");
                        }
                        addTask(taskList, new Todo(words[1]));
                        break;

                    case DEADLINE:
                        if (words.length < 2 || !words[1].contains(" /by ")) {
                            throw new MiloException("OOPS!!! Deadlines must include description and ' /by ' [yyyy-mm-dd].");
                        }
                        String[] dParts = words[1].split(" /by ", 2);
                        try {
                            addTask(taskList, new Deadline(dParts[0], dParts[1]));
                        } catch (DateTimeParseException e) {
                            throw new MiloException("OOPS!!! Please use the format YYYY-MM-DD for the date (e.g., 2026-01-29).");
                        }
                        break;

                    case EVENT:
                        if (words.length < 2 || !words[1].contains(" /from ") || !words[1].contains(" /to ")) {
                            throw new MiloException("OOPS!!! Events must include description, ' /from ' [yyyy-mm-dd] and ' /to ' [yyyy-mm-dd].");
                        }
                        String[] eParts = words[1].split(" /from ", 2);
                        String[] timeParts = eParts[1].split(" /to ", 2);
                        try {
                            addTask(taskList, new Event(eParts[0], timeParts[0], timeParts[1]));
                        } catch (DateTimeParseException e) {
                            throw new MiloException("OOPS!!! Please use YYYY-MM-DD for event dates.");
                        }
                        break;

                    case DELETE:
                        handleDelete(input, taskList);
                        break;

                    case FIND_DATE:
                        if (words.length < 2) {
                            throw new MiloException("OOPS!!! Please specify a date in YYYY-MM-DD format.");
                        }
                        handleFindDate(words[1].trim(), taskList);
                        break;

                    case UNKNOWN:
                    default:
                        throw new MiloException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (MiloException e) {
                System.out.println(" " + e.getMessage());
            }
            saveTasks(taskList);
            System.out.println("____________________________________________________________");
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    private static void addTask(ArrayList<Task> list, Task t) {
        list.add(t);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + list.size() + " tasks in the list.");
    }

    private static void handleMarkUnmark(String input, ArrayList<Task> list, boolean isMark) throws MiloException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new MiloException("OOPS!!! Please specify the task number.");
        }
        try {
            int idx = Integer.parseInt(parts[1]) - 1;
            if (idx < 0 || idx >= list.size()) {
                throw new MiloException("OOPS!!! Task number " + (idx + 1) + " does not exist.");
            }
            if (isMark) {
                list.get(idx).markAsDone();
                System.out.println(" Nice! I've marked this task as done:\n   " + list.get(idx));
            } else {
                list.get(idx).unmarkDone();
                System.out.println(" OK, I've marked this task as not done yet:\n   " + list.get(idx));
            }
        } catch (NumberFormatException e) {
            throw new MiloException("OOPS!!! '" + parts[1] + "' is not a valid task number.");
        }
    }

    private static void handleDelete(String input, ArrayList<Task> list) throws MiloException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new MiloException("OOPS!!! Please specify the task number to delete.");
        }
        try {
            int idx = Integer.parseInt(parts[1]) - 1;
            if (idx < 0 || idx >= list.size()) {
                throw new MiloException("OOPS!!! Task number " + (idx + 1) + " does not exist.");
            }
            Task removedTask = list.remove(idx);
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + removedTask);
            System.out.println(" Now you have " + list.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new MiloException("OOPS!!! '" + parts[1] + "' is not a valid task number.");
        }
    }

    private static void saveTasks(ArrayList<Task> list) {
        try {
            Files.createDirectories(Paths.get("./data")); // Create folder if missing
            List<String> lines = new ArrayList<>();
            for (Task t : list) {
                lines.add(t.toFileFormat());
            }
            Files.write(Paths.get(FILE_PATH), lines);
        } catch (IOException e) {
            System.out.println(" Error saving tasks: " + e.getMessage());
        }
    }

    private static void loadTasks(ArrayList<Task> taskList) {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return;

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split(" \\| ");
                Task task = null;
                try {
                    switch (parts[0]) {
                        case "T":
                            task = new Todo(parts[2]);
                            break;
                        case "D":
                            task = new Deadline(parts[2], parts[3]);
                            break;
                        case "E":
                            task = new Event(parts[2], parts[3], parts[4]);
                            break;
                    }
                } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                    System.out.println(" Skipping corrupted line: " + line);
                    continue;
                }
                if (task != null) {
                    if (parts[1].equals("1")) task.markAsDone();
                    taskList.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println(" Error loading file: " + e.getMessage());
        }
    }

    private static void handleFindDate(String dateStr, ArrayList<Task> list) throws MiloException {
        try {
            LocalDate queryDate = LocalDate.parse(dateStr);
            System.out.println(" Here are the tasks occurring on " + queryDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");

            int count = 0;
            for (Task t : list) {
                boolean isMatch = false;
                if (t instanceof Deadline) {
                    if (((Deadline) t).getBy().equals(queryDate)) isMatch = true;
                } else if (t instanceof Event) {
                    // Check if the query date is the start date
                    if (((Event) t).getFrom().equals(queryDate)) isMatch = true;
                }

                if (isMatch) {
                    count++;
                    System.out.println(" " + count + "." + t);
                }
            }
            if (count == 0) System.out.println(" No tasks found for this date.");
        } catch (DateTimeParseException e) {
            throw new MiloException("OOPS!!! Please use the format YYYY-MM-DD (e.g., 2026-06-15).");
        }
    }
}