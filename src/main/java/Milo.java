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

            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + taskList.get(i));
                }
            } else if (input.startsWith("mark ")) {
                handleMarkUnmark(input, taskList, true);
            } else if (input.startsWith("unmark ")) {
                handleMarkUnmark(input, taskList, false);
            } else if (input.startsWith("todo ")) {
                String desc = input.substring(5);
                addTask(taskList, new Todo(desc));
            } else if (input.startsWith("deadline ")) {
                // Format: deadline return book /by Sunday
                String[] parts = input.substring(9).split(" /by ");
                addTask(taskList, new Deadline(parts[0], parts[1]));
            } else if (input.startsWith("event ")) {
                // Format: event meeting /from Mon 2pm /to 4pm
                String[] parts = input.substring(6).split(" /from ");
                String desc = parts[0];
                String[] timeParts = parts[1].split(" /to ");
                addTask(taskList, new Event(desc, timeParts[0], timeParts[1]));
            } else {
                addTask(taskList, new Task(input));
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


    private static void handleMarkUnmark(String input, ArrayList<Task> list, boolean isMark) {
        try {
            int idx = Integer.parseInt(input.split(" ")[1]) - 1;
            if (isMark) {
                list.get(idx).markAsDone();
                System.out.println(" Nice! I've marked this task as done:\n   " + list.get(idx));
            } else {
                list.get(idx).unmarkDone();
                System.out.println(" OK, I've marked this task as not done yet:\n   " + list.get(idx));
            }
        } catch (Exception e) {
            System.out.println(" OOPS!!! Please provide a valid task number.");
        }
    }
}