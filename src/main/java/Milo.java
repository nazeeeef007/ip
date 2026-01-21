import java.util.Scanner;
import java.util.ArrayList;

public class Milo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> taskList = new ArrayList<>();
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
                            throw new MiloException("OOPS!!! Deadlines must include description and ' /by ' [time].");
                        }
                        String[] dParts = words[1].split(" /by ", 2);
                        addTask(taskList, new Deadline(dParts[0], dParts[1]));
                        break;

                    case EVENT:
                        if (words.length < 2 || !words[1].contains(" /from ") || !words[1].contains(" /to ")) {
                            throw new MiloException("OOPS!!! Events must include description, ' /from ' [time] and ' /to ' [time].");
                        }
                        String[] eParts = words[1].split(" /from ", 2);
                        String[] timeParts = eParts[1].split(" /to ", 2);
                        addTask(taskList, new Event(eParts[0], timeParts[0], timeParts[1]));
                        break;

                    case DELETE:
                        handleDelete(input, taskList);
                        break;

                    case UNKNOWN:
                    default:
                        throw new MiloException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (MiloException e) {
                System.out.println(" " + e.getMessage());
            }
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
}