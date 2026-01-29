import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NattoException extends Exception {
    public NattoException(String message) {
        super(message);
    }
}

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
        Scanner scanner = new Scanner(System.in);
        boolean isComplete = false;
        ArrayList<Task> tasks;
        try {
            tasks = new ArrayList<>(loadTasks());
        } catch (NattoException e) {
            System.out.println(e.getMessage());
            tasks = new ArrayList<>();
        }

        System.out.println("Hello! I'm Natto");
        System.out.println("What can I do for you?");

        while (!isComplete) {
            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String commandWord = input.split(" ")[0];

            try {
                switch (commandWord) {
                case "bye":
                    System.out.println("\nBye. Hope to see you again soon!");
                    isComplete = true;
                    break;

                case "list":
                    implementList(input, tasks);
                    break;

                case "mark":
                    implementMark(input, tasks);
                    break;

                case "unmark":
                    implementUnmark(input, tasks);
                    break;

                case "delete":
                    implementDelete(input, tasks);
                    break;

                case "todo":
                    implementTodo(input, tasks);
                    break;

                case "deadline":
                    implementDeadline(input, tasks);
                    break;

                case "event":
                    implementEvent(input, tasks);
                    break;

                default:
                    throw new NattoException("Please use a keyword like: todo, deadline, event, list");
                }
            } catch (NattoException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static int indexChecker(String input, ArrayList<Task> tasks) throws NattoException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new NattoException("Please specify a task index.");
        }

        int index;
        try {
            index = Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new NattoException("Index must be a number.");
        }

        if (index < 0 || index >= tasks.size()) {
            throw new NattoException("No such task exists.");
        }
        return index;
    }

    static void implementList(String input, ArrayList<Task> tasks) throws NattoException {
        String[] parts = input.split(" ");
        if (parts.length > 1) {
            throw new NattoException("list keyword works alone");
        }
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }

    static void implementMark(String input, ArrayList<Task> tasks) throws NattoException {
        int index = indexChecker(input, tasks);
        tasks.get(index).mark();
        System.out.println("\nNice! I've marked this task as done:");
        System.out.println("  " + tasks.get(index));
        saveTasks(tasks);
    }

    static void implementUnmark(String input, ArrayList<Task> tasks) throws NattoException {
        int index = indexChecker(input, tasks);
        tasks.get(index).unmark();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(index));
        saveTasks(tasks);
    }

    static void implementDelete(String input, ArrayList<Task> tasks) throws NattoException {
        int index = indexChecker(input, tasks);
        Task removed = tasks.remove(index);
        System.out.println("\nNoted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        saveTasks(tasks);
    }

    static void implementTodo(String input, ArrayList<Task> tasks) throws NattoException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new NattoException("The description of a todo cannot be empty.");
        }
        Todo todo = new Todo(parts[1].trim());
        tasks.add(todo);
        System.out.println("\nGot it. I've added this task:");
        System.out.println("  " + todo);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        saveTasks(tasks);
    }

    static void implementDeadline(String input, ArrayList<Task> tasks) throws NattoException {
        if (!input.contains("/by")) {
            throw new NattoException("Deadline must have /by. Example: deadline hw /by tomorrow");
        }

        String desc = input.substring(9, input.indexOf("/by")).trim();
        String by = input.substring(input.indexOf("/by") + 3).trim();

        if (desc.isEmpty()) {
            throw new NattoException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new NattoException("The /by date cannot be empty.");
        }
        Deadline deadline = new Deadline(desc, by);
        tasks.add(deadline);
        System.out.println("\nGot it. I've added this task:");
        System.out.println("  " + deadline);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        saveTasks(tasks);
    }

    static void implementEvent(String input, ArrayList<Task> tasks) throws NattoException {
        if (!input.contains("/from") || !input.contains("/to")) {
            throw new NattoException("Event must have /from and /to.");
        }

        String desc = input.substring(6, input.indexOf("/from")).trim();
        String from = input.substring(input.indexOf("/from") + 5, input.indexOf("/to")).trim();
        String to = input.substring(input.indexOf("/to") + 3).trim();

        if (desc.isEmpty()) {
            throw new NattoException("The description of an event cannot be empty.");
        }

        Event event = new Event(desc, from, to);
        tasks.add(event);
        System.out.println("\nGot it. I've added this task:");
        System.out.println("  " + event);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        saveTasks(tasks);
    }

    static List<Task> loadTasks() throws NattoException {
        List<Task> taskList = new ArrayList<>();
        File f = new File("data/NatData.txt");

        if (!f.exists()) {
            return taskList;
        }

        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) {
                    taskList.add(loadTaskArray(line));
                }
            }
            return taskList;
        } catch (Exception e) {
            throw new NattoException("Error loading tasks from file.");
        }
    }
    static Task loadTaskArray(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Corrupted data: " + line);
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task;

        switch (type) {
            case "T":
                task = new Todo(description);
                break;

            case "D":
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Corrupted deadline data: " + line);
                }
                String by = parts[3].trim();
                task = new Deadline(description, by);
                break;

            case "E":
                String raw = parts[3].trim();

                int lastSpace = raw.lastIndexOf(" ");
                String date = raw.substring(0, lastSpace).trim();
                String time = raw.substring(lastSpace + 1).trim();

                int dash = time.indexOf("-");
                String from = date + " " + time.substring(0, dash);
                String to = time.substring(dash + 1);

                task = new Event(description, from, to);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (isDone) {
            task.mark();
        }
        return task;
    }

    static String taskToFile(Task task) throws NattoException {
        String done = task.isDone ? "1" : "0";

        if (task instanceof Todo) {
            return "T | " + done + " | " + task.name;
        }

        if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return "D | " + done + " | " + d.name + " | " + d.by;
        }

        if (task instanceof Event) {
            Event e = (Event) task;
            String time = e.from + "-" + e.to;
            return "E | " + done + " | " + e.name + " | " + time;
        }
        throw new IllegalArgumentException("Unknown task type");
    }

    static void saveTasks(ArrayList<Task> tasks) throws NattoException {
        File dir = new File("data");
        File f = new File("data" + File.separator + "NatData.txt");

        if (!dir.exists()) {
            dir.mkdir();
        }
        try (FileWriter fw = new FileWriter(f)) {
            for (Task t : tasks) {
                fw.write(taskToFile(t));
                fw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new NattoException("Error saving tasks to file.");
        }
    }
}