package natto;

import java.util.List;
import java.util.Scanner;

/**
 * Handles user interaction, including reading input and displaying output.
 */
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
        lastOutput = "Hello! I'm Natto \uD83D\uDE38\nWhat can I do for you? \uD83D\uDE3AMeow~";
        System.out.println(lastOutput);
    }


    public void printLine() {
        System.out.println("____________________________________________________________");
    }


    /**
     * Prints the goodbye message shown when the program exits.
     */
    public void printGoodbye() {
        lastOutput = "Bye. Hope to see you again soon!\uD83D\uDE3A";
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
    public void printList(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();

        sb.append("Here are the tasks in your list:\n\n");

        if (tasks.isEmpty()) {
            sb.append("\uD83D\uDE3D No tasks yet! \uD83D\uDE3D");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(i + 1)
                        .append(". ")
                        .append(tasks.get(i))
                        .append("\n");
            }
        }

        lastOutput = sb.toString().trim();
        System.out.println(lastOutput);
    }
    /**
     * Prints confirmation that a task has been marked as done.
     *
     * @param tasks List of tasks.
     * @param index Index of the marked task (0-based).
     */
    public void printMark(List<Task> tasks, int index) {
        lastOutput = "Meow~\uD83D\uDE3A! I've marked this task as done:\n  " + tasks.get(index);
        System.out.println("\n" + lastOutput);
    }

    /**
     * Prints confirmation that a task has been unmarked (set to not done).
     *
     * @param tasks List of tasks.
     * @param index Index of the unmarked task (0-based).
     */
    public void printUnmark(List<Task> tasks, int index) {
        lastOutput = "Meow~\uD83D\uDE3A, I've marked this task as not done yet:\n  " + tasks.get(index);
        System.out.println(lastOutput);
    }

    /**
     * Prints confirmation that a task has been deleted.
     *
     * @param removed The task that was removed.
     * @param size Updated number of tasks after deletion.
     */
    public void printDelete(Task removed, int size) {
        lastOutput = "Meow~\uD83D\uDE3A. I've removed this task:\n  " + removed
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
        lastOutput = "Meow~\uD83D\uDE3A. I've added this task:\n  " + added
                + "\nNow you have " + size + " tasks in the list.";
        System.out.println("\n" + lastOutput);
    }

    /**
     * Prints the tasks that match the given keyword.
     *
     * @param taskList The list of tasks to search.
     * @param keyword  The keyword to search for.
     */
    public void printFind(TaskList taskList, String keyword) {
        StringBuilder sb = new StringBuilder();
        sb.append("Meow~\uD83D\uDE3A. Here are the matching tasks in your list:\n");

        List<Task> matches = taskList.getAll().stream()
                .filter(t -> t.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

        if (matches.isEmpty()) {
            sb.append("No matching tasks found. Meow~\uD83D\uDE3F.");
        } else {
            for (int i = 0; i < matches.size(); i++) {
                sb.append(i + 1).append(". ").append(matches.get(i)).append("\n");
            }
        }

        lastOutput = sb.toString().trim();

        printLine();
        System.out.println(lastOutput);
        printLine();
    }

    /**
     * Prints the contact information of the creator.
     *
     * @param name  Creator's name.
     * @param number Creator's contact number.
     * @param email  Creator's email address.
     */
    public void printContactCreator(String name, String number, String email) {
        lastOutput = "\uD83D\uDC31Creator\uD83D\uDC31: " + name + "\nContact: " + number + "\nEmail: " + email;
        printLine();
        System.out.println(lastOutput);
        printLine();
    }
}
