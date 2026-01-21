import java.util.ArrayList;
import java.util.Scanner;

class Task {
    String name;
    boolean isDone;

    public Task(String name) {
        this.name = name;
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

public class Natto {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String logo = "Natto";
        String exit = "Bye. Hope to see you again soon!";
        ArrayList<Task> tasks = new ArrayList<>();

        // Start - Greeting User
        System.out.println("Hello! I'm " + logo);
        System.out.println("What can I do for you?");

        // Body
        while (true) {
            String line = sc.nextLine();
            if (line.equals("bye")) break;     // Exit
            if (line.equals("list")){          // Print history
                System.out.println("____________________________________________________________");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i+1) + ". " + tasks.get(i));
                }
                System.out.println("____________________________________________________________");
                continue;
            }
            System.out.println("added: " + line);
            tasks.add(new Task(line));
        }

        //Ending
        System.out.println(exit);
    }
}
