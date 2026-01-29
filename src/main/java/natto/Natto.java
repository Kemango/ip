package natto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an application-specific exception for Natto.
 */
class NattoException extends Exception {
    public NattoException(String message) {
        super(message);
    }
}

/**
 * Represents a task with a name and completion status.
 */
class Task {
    private String name;
    private boolean isDone;

    /**
     * Creates a task with the given name.
     *
     * @param name Name/description of the task.
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    /**
     * Marks the task as done.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns the string representation of the task for display.
     *
     * @return Display string of the task.
     */
    public boolean isDone() {
        return isDone;
    }

    public String getName() {
        return name;
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

/**
 * Represents a task that must be done by a specific date/time.
 */
class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Creates a deadline task with a description and due date/time.
     *
     * @param description Description of the deadline task.
     * @param by Due date/time.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the string representation of a deadline for display.
     *
     * @return Display string of the deadline.
     */
    @Override
    public String toString() {
        DateTimeFormatter schoolDateFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return "[D]" + super.toString() + " (by: " + by.format(schoolDateFormat) + ")";
    }
}

/**
 * Represents a simple to-do task without any date/time.
 */
class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

/**
 * Represents an event task with a start and end date/time.
 */
class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates an event with a description, start time, and end time.
     *
     * @param description Description of the event.
     * @param from Start date/time.
     * @param to End date/time.
     */
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

    /**
     * Handles the list command and prints all tasks.
     *
     * @param input Full user input string.
     * @param tasks Task list to display.
     * @param ui UI used for printing output.
     * @throws NattoException If the list command has extra arguments.
     */
    static void implementList(String input, TaskList tasks, Ui ui) throws NattoException {
        String[] parts = input.split(" ");
        if (parts.length > 1) {
            throw new NattoException("list keyword works alone");
        }
        ui.printList(tasks.getAll());
    }

    /**
     * Handles the mark command and saves the updated task list.
     *
     * @param input Full user input string.
     * @param tasks Task list to modify.
     * @param ui UI used for printing output.
     * @param storage Storage used for saving tasks.
     * @throws NattoException If the index is invalid.
     */
    static void implementMark(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        tasks.get(index).mark();
        ui.printMark(tasks.getAll(), index);
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the unmark command and saves the updated task list.
     *
     * @param input Full user input string.
     * @param tasks Task list to modify.
     * @param ui UI used for printing output.
     * @param storage Storage used for saving tasks.
     * @throws NattoException If the index is invalid.
     */
    static void implementUnmark(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        tasks.get(index).unmark();
        ui.printUnmark(tasks.getAll(), index);
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the delete command and saves the updated task list.
     *
     * @param input Full user input string.
     * @param tasks Task list to modify.
     * @param ui UI used for printing output.
     * @param storage Storage used for saving tasks.
     * @throws NattoException If the index is invalid.
     */
    static void implementDelete(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        Task removed = tasks.remove(index);
        ui.printDelete(removed, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the todo command and saves the updated task list.
     *
     * @param input Full user input string.
     * @param tasks Task list to modify.
     * @param ui UI used for printing output.
     * @param storage Storage used for saving tasks.
     * @throws NattoException If the description is missing/invalid.
     */
    static void implementTodo(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        String desc = Parser.parseTodo(input);
        Todo todo = new Todo(desc);

        tasks.add(todo);
        ui.printAdd(todo, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the deadline command and saves the updated task list.
     *
     * @param input Full user input string.
     * @param tasks Task list to modify.
     * @param ui UI used for printing output.
     * @param storage Storage used for saving tasks.
     * @throws NattoException If the deadline format is invalid.
     */
    static void implementDeadline(String input, TaskList tasks, Ui ui, Storage storage) throws NattoException {
        Deadline deadline = Parser.parseDeadline(input);

        tasks.add(deadline);
        ui.printAdd(deadline, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the event command and saves the updated task list.
     *
     * @param input Full user input string.
     * @param tasks Task list to modify.
     * @param ui UI used for printing output.
     * @param storage Storage used for saving tasks.
     * @throws NattoException If the event format is invalid.
     */
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
