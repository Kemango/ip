package natto;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> loadTasks() throws NattoException {
        List<Task> taskList = new ArrayList<>();
        File f = new File(filePath);

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
                LocalDateTime by = LocalDateTime.parse(parts[3].trim());
                task = new Deadline(description, by);
                break;

            case "E":
                String raw = parts[3].trim();

                int lastSpace = raw.lastIndexOf(" ");
                String date = raw.substring(0, lastSpace).trim();
                String time = raw.substring(lastSpace + 1).trim();

                int dash = time.indexOf("-");
                LocalDateTime from = LocalDateTime.parse(date + " " + time.substring(0, dash));
                LocalDateTime to = LocalDateTime.parse(time.substring(dash + 1));

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

    static void saveTasks(List<Task> tasks) throws NattoException {
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
