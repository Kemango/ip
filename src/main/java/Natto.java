import java.util.Scanner;

public class Natto {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String logo = "Natto";
        String exit = "Bye. Hope to see you again soon!";
        // Start - Greeting User
        System.out.println("Hello I'm " + logo + "\n"
                + "What can I do for you?");
        // Body
        while (true) {
            String line = sc.nextLine();
            if (line.equals("bye")) break;     // Exit
            System.out.println(line);
        }
        //Ending
        System.out.println(exit);
    }
}
