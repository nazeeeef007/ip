import java.util.Scanner;
import java.util.ArrayList;


public class Milo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> taskList = new ArrayList<>();
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
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.println(" " + (i + 1) + ". " + taskList.get(i));
                }
            } else {
                taskList.add(input);
                System.out.println(" added: " + input);
            }

            System.out.println("____________________________________________________________");
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
}