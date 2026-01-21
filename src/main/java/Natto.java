import java.util.ArrayList;
import java.util.Scanner;

public class Natto {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String logo = "Natto";
        String exit = "Bye. Hope to see you again soon!";
        ArrayList<String> history = new ArrayList<>();

        // Start - Greeting User
        System.out.println("Hello! I'm " + logo + "\n"
                + "What can I do for you?");
        // Body
        while (true) {
            String line = sc.nextLine();
            if (line.equals("bye")) break;     // Exit
            if (line.equals("list")){          // Print history
                for (int i = 0; i < history.size(); i++) {
                    System.out.println((i+1) + ". " + history.get(i));
                }
                continue;
            }
            System.out.println("added: " + line);
            history.add(line);
        }
        //Ending
        System.out.println(exit);
    }
}
