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
        assert name != null : "Task name should not be null";
        assert !name.trim().isEmpty() : "Task name should not be empty";
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
        assert by != null : "Deadline 'by' should not be null";
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
        assert from != null : "Event 'from' should not be null";
        assert to != null : "Event 'to' should not be null";
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

/**
 * Represents the main application logic for Natto.
 */
public class Natto {
    private static final String SAVE_PATH = "data/NatData.txt";

    private static final String CREATOR_NAME = "Kemango";
    private static final String CREATOR_NUM = "8283 6964";
    private static final String CREATOR_EMAIL = "e1398747@u.nus.edu";

    private final Ui ui = new Ui();
    private final Storage storage = new Storage(SAVE_PATH);
    private TaskList tasks;

    /**
     * Constructs a Natto instance and loads stored tasks.
     * If loading fails, an empty task list is created.
     */
    public Natto() {
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (NattoException e) {
            tasks = new TaskList();
        }
    }
    /**
     * Returns the greeting message from the UI.
     *
     * @return Greeting message.
     */
    public String getGreeting() {
        ui.printGreeting();
        return ui.getLastOutput();
    }
    public static void main(String[] args) {
        new Natto().run();
    }
    /**
     * Runs the main application loop, processing user commands until "bye" is received.
     */
    public void run() {
        getGreeting();
        boolean isComplete = false;

        while (!isComplete) {
            String input = ui.readCommand();

            if (input == null || input.trim().isEmpty()) {
                continue;
            }

            getResponse(input);

            if ("bye".equals(input.trim())) {
                isComplete = true;
            }
        }
    }
    /**
     * Processes a user input command and returns the corresponding response.
     *
     * @param input User input command.
     * @return Response message after processing the command.
     */
    public String getResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String commandWord = Parser.getCommandWord(input);

        try {
            switch (commandWord) {
            case "bye":
                ui.printGoodbye();
                return ui.getLastOutput();

            case "list":
                implementList(input);
                return ui.getLastOutput();

            case "mark":
                implementMark(input);
                return ui.getLastOutput();

            case "unmark":
                implementUnmark(input);
                return ui.getLastOutput();

            case "delete":
                implementDelete(input);
                return ui.getLastOutput();

            case "todo":
                implementTodo(input);
                return ui.getLastOutput();

            case "deadline":
                implementDeadline(input);
                return ui.getLastOutput();

            case "event":
                implementEvent(input);
                return ui.getLastOutput();

            case "find":
                implementFind(input);
                return ui.getLastOutput();

            case "creator":
                contactCreator(input);
                return ui.getLastOutput();

            case "contact":
                implementContact(input);
                return ui.getLastOutput();

            default:
                throw new NattoException("Please use a keyword like: todo, deadline, event, list");
            }
        } catch (NattoException e) {
            ui.printError("Error: " + e.getMessage());
            return ui.getLastOutput();
        }
    }
    /**
     * Handles the list command to display all tasks.
     *
     * @param input Full user input string.
     * @throws NattoException If the input format is invalid.
     */
    private void implementList(String input) throws NattoException {
        if (!input.trim().equals("list")) {
            throw new NattoException("list keyword works alone");
        }
        ui.printList(tasks.getAll());
    }

    /**
     * Handles the mark command and saves the updated task list.
     *
     * @param input Full user input string.
     * @throws NattoException If the index is invalid.
     */
    private void implementMark(String input) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        assert index >= 0 && index < tasks.size() : "Parser should return a valid index";

        tasks.get(index).mark();
        ui.printMark(tasks.getAll(), index);
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the unmark command and saves the updated task list.
     *
     * @param input Full user input string.
     * @throws NattoException If the index is invalid.
     */
    private void implementUnmark(String input) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        tasks.get(index).unmark();
        ui.printUnmark(tasks.getAll(), index);
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the delete command and saves the updated task list.
     *
     * @param input Full user input string.
     * @throws NattoException If the index is invalid.
     */
    private void implementDelete(String input) throws NattoException {
        int index = Parser.parseIndex(input, tasks.size());
        Task removed = tasks.remove(index);
        ui.printDelete(removed, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the todo command and saves the updated task list.
     *
     * @param input Full user input string.
     * @throws NattoException If the todo description is missing.
     */
    private void implementTodo(String input) throws NattoException {
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
     * @throws NattoException If the deadline format is invalid.
     */
    private void implementDeadline(String input) throws NattoException {
        Deadline deadline = Parser.parseDeadline(input);

        tasks.add(deadline);
        ui.printAdd(deadline, tasks.size());
        storage.saveTasks(tasks.getAll());
    }
    /**
     * Handles the event command and saves the updated task list.
     *
     * @param input Full user input string.
     * @throws NattoException If the event format is invalid.
     */
    private void implementEvent(String input) throws NattoException {
        Event event = Parser.parseEvent(input);

        tasks.add(event);
        ui.printAdd(event, tasks.size());
        storage.saveTasks(tasks.getAll());
    }

    /**
     * Handles the find command to search for tasks containing a keyword.
     *
     * @param input Full user input string.
     * @throws NattoException If the find format is invalid.
     */
    private void implementFind(String input) throws NattoException {
        String keyword = Parser.parseFind(input);
        ui.printFind(tasks, keyword);
    }

    /**
     * Handles the contact command to display creator's contact information.
     *
     * @param input Full user input string.
     * @throws NattoException If the input format is invalid.
     */
    private void contactCreator(String input) throws NattoException {
        if (!input.trim().equals("contact")) {
            throw new NattoException("contact keyword works alone");
        }

        ui.printContactCreator(CREATOR_NAME, CREATOR_NUM, CREATOR_EMAIL);
    }

    private void implementContact(String input) throws NattoException {
        Contact c = Parser.parseContact(input);
        tasks.add(c);
        ui.printAdd(c, tasks.size());
        storage.saveTasks(tasks.getAll());
    }


}
