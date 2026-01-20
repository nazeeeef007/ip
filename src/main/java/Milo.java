import java.util.Scanner;
import java.util.ArrayList;

public class Milo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> taskList = new ArrayList<>();
        String name = "Milo";

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm " + name);
        System.out.println(" What can I do for you?");
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
                // Extracts the number after "mark "
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                taskList.get(taskIndex).markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + taskList.get(taskIndex));
            } else if (input.startsWith("unmark ")) {
                // Extracts the number after "unmark "
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                taskList.get(taskIndex).unmarkDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + taskList.get(taskIndex));
            } else {
                // Standard Level 2 logic: Treat input as a new Task description
                Task newTask = new Task(input);
                taskList.add(newTask);
                System.out.println(" added: " + input);
            }

            System.out.println("____________________________________________________________");
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
}