import java.util.ArrayList;
import java.util.Scanner;

class Task {
    String name;
    boolean isDone;

    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        if(isDone) {
            return ("[X] " + this.name);
        } else {
            return ("[ ] " + this.name);
        }
    }
}

class Deadline extends Task {

    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Event extends Task {

    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
public class Natto {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String logo = "Natto";
        String exit = "Bye. Hope to see you again soon!";
        ArrayList<Task> tasks = new ArrayList<>();
        int amountOfTasks = 0;

        // Start - Greeting User
        System.out.println("Hello! I'm " + logo);
        System.out.println("What can I do for you?");

        // Body
        while (true) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (line.equals("bye")) break;                            // Exit
            if (parts[0].equals("mark")){                             // Mark
                System.out.println("\nNice! I've marked this task as done:");
                int index = Integer.parseInt(parts[1]) - 1;
                tasks.get(index).mark();
                System.out.println(tasks.get(index).toString());
                continue;
            }
            if (parts[0].equals("unmark")){                           // Unmark
                System.out.println("\nOK, I've marked this task as not done yet:");
                int index = Integer.parseInt(parts[1]) - 1;
                tasks.get(index).unmark();
                System.out.println(tasks.get(index).toString());
                continue;
            }
            if (parts[0].equals("todo")) {                            // Add Todo
                String s = line.substring(5);
                Todo theTask = new Todo(s);
                System.out.println("\nGot it. I've added this task:");
                tasks.add(theTask);
                amountOfTasks = tasks.size();
                System.out.println("   " + theTask);
                System.out.println("Now you have " + amountOfTasks + " tasks in the list.");
                continue;
            }
            if(parts[0].equals("deadline")) {                        // Add Deadline
                String s = line.substring(9, line.indexOf("/by")).trim();
                String date = line.substring(line.indexOf("/by") + 3).trim();
                Deadline theTask = new Deadline(s ,date);
                System.out.println("\nGot it. I've added this task:");
                tasks.add(theTask);
                amountOfTasks = tasks.size();
                System.out.println("   " + theTask);
                System.out.println("Now you have " + amountOfTasks + " tasks in the list.");
            }
            if(parts[0].equals("event")) {
                String s = line.substring(6, line.indexOf("/from")).trim();
                String from = line.substring(line.indexOf("/from") + 5, line.indexOf("/to")).trim();
                String to = line.substring(line.indexOf("/to") + 3).trim();
                Event theTask = new Event(s ,from, to);
                System.out.println("\nGot it. I've added this task:");
                tasks.add(theTask);
                amountOfTasks = tasks.size();
                System.out.println("   " + theTask);
                System.out.println("Now you have " + amountOfTasks + " tasks in the list.");
            }

            if (line.equals("list")){                                 // Print history
                System.out.println("____________________________________________________________");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i+1) + ". " + tasks.get(i));
                }
                System.out.println("____________________________________________________________");
            }
        }

        //Ending
        System.out.println(exit);
    }
}
