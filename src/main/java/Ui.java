import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ui {
    private final Scanner scanner;
    private static final String DIVIDER = "____________________________________________________________";

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(DIVIDER + "\n Hello! I'm Milo\n What can I do for you?\n" + DIVIDER);
    }

    public void showLine() {
        System.out.println(DIVIDER);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showError(String message) {
        System.out.println(" " + message);
    }

    public void showExit() {
        System.out.println(" Bye. Hope to see you again soon!");
    }

    public void showTaskList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.getSize(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.getTask(i));
        }
    }

    public void showAddedTask(Task t, int size) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + size + " tasks in the list.");
    }

    public void showRemovedTask(Task t, int size) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + size + " tasks in the list.");
    }

    public void showStatusChange(Task t, boolean isMark) {
        if (isMark) {
            System.out.println(" Nice! I've marked this task as done:\n   " + t);
        } else {
            System.out.println(" OK, I've marked this task as not done yet:\n   " + t);
        }
    }

    public void showTasksByDate(String dateStr, ArrayList<Task> list) {
        try {
            LocalDate queryDate = LocalDate.parse(dateStr);
            System.out.println(" Here are the tasks occurring on " + queryDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
            int count = 0;
            for (Task t : list) {
                boolean isMatch = false;
                if (t instanceof Deadline && ((Deadline) t).getBy().equals(queryDate)) isMatch = true;
                if (t instanceof Event && ((Event) t).getFrom().equals(queryDate)) isMatch = true;

                if (isMatch) {
                    count++;
                    System.out.println(" " + count + "." + t);
                }
            }
            if (count == 0) System.out.println(" No tasks found for this date.");
        } catch (Exception e) {
            System.out.println(" OOPS!!! Please use the format YYYY-MM-DD for searching.");
        }
    }
}