package natto;

import java.util.List;
import java.util.Scanner;

public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a command line from the user.
     *
     * @return Trimmed user input, or null if there is no more input.
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    /**
     * Prints the greeting message shown when the program starts.
     */
    public void printGreeting() {
        System.out.println("Hello! I'm Natto");
        System.out.println("What can I do for you?");
    }

    public void printLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints the goodbye message shown when the program exits.
     */
    public void printGoodbye(){
        System.out.println("\nBye. Hope to see you again soon!");
    }

    /**
     * Prints an error message to the user.
     *
     * @param message Error message to display.
     */
    public void printError(String message) {
        System.out.println(message);
    }

    /**
     * Prints the list of tasks currently in the task list.
     *
     * @param tasks List of tasks to display.
     */
    public void printList(List<Task> tasks){
        printLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        printLine();
    }

    /**
     * Prints confirmation that a task has been marked as done.
     *
     * @param tasks List of tasks.
     * @param index Index of the marked task (0-based).
     */
    public void printMark(List<Task> tasks,  int index){
        System.out.println("\nNice! I've marked this task as done:");
        System.out.println("  " + tasks.get(index));
    }

    /**
     * Prints confirmation that a task has been unmarked (set to not done).
     *
     * @param tasks List of tasks.
     * @param index Index of the unmarked task (0-based).
     */
    public void printUnmark(List<Task> tasks, int index){
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(index));
    }

    /**
     * Prints confirmation that a task has been deleted.
     *
     * @param removed The task that was removed.
     * @param size Updated number of tasks after deletion.
     */
    public void printDelete(Task removed, int size) {
        System.out.println("\nNoted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Prints confirmation that a task has been added.
     *
     * @param added The task that was added.
     * @param size Updated number of tasks after adding.
     */
    public void printAdd(Task added, int size) {
        System.out.println("\nGot it. I've added this task:");
        System.out.println("  " + added);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

}
