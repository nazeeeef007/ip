import java.util.Scanner;

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
}