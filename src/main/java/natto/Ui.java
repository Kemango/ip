package natto;

import java.util.List;
import java.util.Scanner;

public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    public void printGreeting() {
        System.out.println("Hello! I'm Natto");
        System.out.println("What can I do for you?");
    }

    public void printLine() {
        System.out.println("____________________________________________________________");
    }

    public void printGoodbye(){
        System.out.println("\nBye. Hope to see you again soon!");
    }

    public void printError(String message) {
        System.out.println(message);
    }

    public void printList(List<Task> tasks){
        printLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        printLine();
    }

    public void printMark(List<Task> tasks,  int index){
        System.out.println("\nNice! I've marked this task as done:");
        System.out.println("  " + tasks.get(index));
    }

    public void printUnmark(List<Task> tasks, int index){
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(index));
    }

    public void printDelete(Task removed, int size) {
        System.out.println("\nNoted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void printAdd(Task added, int size) {
        System.out.println("\nGot it. I've added this task:");
        System.out.println("  " + added);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void printFind(TaskList taskList, String keyword) {
        printLine();
        System.out.println("Here are the matching tasks in your list:");

        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.getName().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                System.out.println((i + 1) + ". " + task);
            }
        }
        printLine();
    }

}
