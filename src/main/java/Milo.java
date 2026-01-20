import java.util.Scanner;


public class Milo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
            }
            System.out.println(" " + input);
            System.out.println("____________________________________________________________");
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
}