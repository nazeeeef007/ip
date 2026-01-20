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

        while (true) {
            String input = scanner.nextLine();
            System.out.println("____________________________________________________________");

            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskList.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + taskList.get(i));
                    }
                } else if (input.startsWith("mark")) {
                    handleMarkUnmark(input, taskList, true);
                } else if (input.startsWith("unmark")) {
                    handleMarkUnmark(input, taskList, false);
                } else if (input.startsWith("todo")) {

                    if (input.trim().length() <= 4) {
                        throw new MiloException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    String desc = input.substring(5);
                    addTask(taskList, new Todo(desc));
                } else if (input.startsWith("deadline")) {

                    if (!input.contains(" /by ")) {
                        throw new MiloException("OOPS!!! Deadlines must include ' /by ' followed by the time.");
                    }
                    String[] parts = input.substring(9).split(" /by ");
                    if (parts.length < 2 || parts[0].trim().isEmpty()) {
                        throw new MiloException("OOPS!!! The description or time of a deadline cannot be empty.");
                    }
                    addTask(taskList, new Deadline(parts[0], parts[1]));
                } else if (input.startsWith("event")) {

                    if (!input.contains(" /from ") || !input.contains(" /to ")) {
                        throw new MiloException("OOPS!!! Events must include ' /from ' and ' /to '. ");
                    }
                    String[] parts = input.substring(6).split(" /from ");
                    String desc = parts[0];
                    String[] timeParts = parts[1].split(" /to ");
                    if (timeParts.length < 2 || desc.trim().isEmpty()) {
                        throw new MiloException("OOPS!!! The event description or time range is incomplete.");
                    }
                    addTask(taskList, new Event(desc, timeParts[0], timeParts[1]));
                } else {

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
}