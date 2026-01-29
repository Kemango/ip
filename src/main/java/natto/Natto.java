package natto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public String getName() {
        return name;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        if (isDone) {
            return "[X] " + this.name;
        } else {
            return "[ ] " + this.name;
        }
    }
}

class Deadline extends Task {

    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        DateTimeFormatter schoolDateFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return "[D]" + super.toString() + " (by: " + by.format(schoolDateFormat) + ")";
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

    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        DateTimeFormatter schoolDateFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");
        DateTimeFormatter schoolTimeFormat = DateTimeFormatter.ofPattern("HH:mm");
        return "[E]" + super.toString()
                + " (from: " + from.format(schoolDateFormat) + " " + from.format(schoolTimeFormat)
                + " to: " + to.format(schoolTimeFormat) + ")";
    }
}

public class Natto {
    public static void main(String[] args) {
        boolean isComplete = false;
        Ui ui = new Ui();
        Storage storage = new Storage("data/NatData.txt");
        TaskList tasks;

        ui.printGreeting();
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (NattoException e) {
            ui.printError(e.getMessage());
            tasks = new TaskList();
        }

        while (!isComplete) {
            String input = ui.readCommand();
            if (input == null) break;
            if (input.isEmpty()) continue;

            String commandWord = Parser.getCommandWord(input);

            try {
                switch (commandWord) {
                case "bye":
                    ui.printGoodbye();
                    isComplete = true;
                    break;

                case "list":
                    implementList(input, tasks, ui);
                    break;

                case "mark":
                    implementMark(input, tasks, ui, storage);
                    break;

                case "unmark":
                    implementUnmark(input, tasks, ui, storage);
                    break;

                case "delete":
                    implementDelete(input, tasks, ui, storage);
                    break;

                case "todo":
                    implementTodo(input, tasks, ui, storage);
                    break;

                case "deadline":
                    implementDeadline(input, tasks, ui, storage);
                    break;

                case "event":
                    implementEvent(input, tasks, ui, storage);
                    break;

                case "find":
                    implementFind(input, tasks, ui);
                    break;
                    
                default:
                    throw new NattoException("Please use a keyword like: todo, deadline, event, list");
                }
            } catch (NattoException e) {
                ui.printError(e.getMessage());
            }
        }
    }

    static void implementList(String input, TaskList tasks, Ui ui) throws NattoException {
        String[] parts = input.split(" ");
        if (parts.length > 1) {
            throw new NattoException("list keyword works alone");
        }
        ui.printList(tasks.getAll());
    }

    static void implementMark(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        tasks.get(index).mark();
        ui.printMark(tasks.getAll(), index);
        storage.saveTasks(tasks.getAll());
    }

    static void implementUnmark(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        tasks.get(index).unmark();
        ui.printUnmark(tasks.getAll(), index);
        storage.saveTasks(tasks.getAll());
    }

    static void implementDelete(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        Task removed = tasks.remove(index);
        ui.printDelete(removed, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    static void implementTodo(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        String desc = Parser.parseTodo(input);
        Todo todo = new Todo(desc);

        tasks.add(todo);
        ui.printAdd(todo, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    static void implementDeadline(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        Deadline deadline = Parser.parseDeadline(input);

        tasks.add(deadline);
        ui.printAdd(deadline, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    static void implementEvent(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        Event event = Parser.parseEvent(input);

        tasks.add(event);
        ui.printAdd(event, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    static void implementFind(String input, TaskList tasks, Ui ui) throws NattoException {
        String keyword = Parser.parseFind(input);
        ui.printFind(tasks, keyword);
    }

}