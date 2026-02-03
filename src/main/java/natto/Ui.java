package natto;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private String lastOutput = "";
    private final Scanner scanner = new Scanner(System.in);

    public String getLastOutput() {
        return lastOutput;
    }

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
        lastOutput = "Hello! I'm Natto\nWhat can I do for you?";
        System.out.println(lastOutput);
    }


    public void printLine() {
        lastOutput = "____________________________________________________________";
        System.out.println(lastOutput);
    }

    /**
     * Prints the goodbye message shown when the program exits.
     */
    public void printGoodbye() {
        lastOutput = "Bye. Hope to see you again soon!";
        System.out.println("\n" + lastOutput);
    }

    /**
     * Prints an error message to the user.
     *
     * @param message Error message to display.
     */
    public void printError(String message) {
        lastOutput = message;
        System.out.println(lastOutput);
    }

    /**
     * Prints the list of tasks currently in the task list.
     *
     * @param tasks List of tasks to display.
     */
    public void printList(List<Task> tasks){
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        lastOutput = sb.toString().trim();

        printLine();
        System.out.println(lastOutput);
        printLine();
    }


    /**
     * Prints confirmation that a task has been marked as done.
     *
     * @param tasks List of tasks.
     * @param index Index of the marked task (0-based).
     */
    public void printMark(List<Task> tasks, int index){
        lastOutput = "Nice! I've marked this task as done:\n  " + tasks.get(index);
        System.out.println("\n" + lastOutput);
    }

    /**
     * Prints confirmation that a task has been unmarked (set to not done).
     *
     * @param tasks List of tasks.
     * @param index Index of the unmarked task (0-based).
     */
    public void printUnmark(List<Task> tasks, int index){
        lastOutput = "OK, I've marked this task as not done yet:\n  " + tasks.get(index);
        System.out.println(lastOutput);
    }

    /**
     * Prints confirmation that a task has been deleted.
     *
     * @param removed The task that was removed.
     * @param size Updated number of tasks after deletion.
     */
    public void printDelete(Task removed, int size) {
        lastOutput = "Noted. I've removed this task:\n  " + removed
                + "\nNow you have " + size + " tasks in the list.";
        System.out.println("\n" + lastOutput);
    }


    /**
     * Prints confirmation that a task has been added.
     *
     * @param added The task that was added.
     * @param size Updated number of tasks after adding.
     */
    public void printAdd(Task added, int size) {
        lastOutput = "Got it. I've added this task:\n  " + added
                + "\nNow you have " + size + " tasks in the list.";
        System.out.println("\n" + lastOutput);
    }

    public void printFind(TaskList taskList, String keyword) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");

        boolean found = false;
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.getName().toLowerCase().contains(keyword.toLowerCase())) {
                sb.append(i + 1).append(". ").append(task).append("\n");
                found = true;
            }
        }

        if (!found) {
            sb.append("No matching tasks found.");
        }

        lastOutput = sb.toString().trim();

        printLine();
        System.out.println(lastOutput);
        printLine();
    }


}
