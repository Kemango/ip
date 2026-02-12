package natto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Parses user input into commands and task details.
 */
public class Parser {

    /**
     * Extracts the command word (first token) from the user input.
     *
     * @param input Full user input.
     * @return Command word (e.g. "todo", "deadline", "event").
     */
    public static String getCommandWord(String input) {
        return input.trim().split(" ")[0];
    }

    /**
     * Parses the task index from commands like "mark 2" and converts it to 0-based index.
     *
     * @param input Full user input.
     * @param size Current number of tasks (used for bounds checking).
     * @return 0-based task index.
     * @throws NattoException If the index is missing, not a number, or out of range.
     */
    public static int parseIndex(String input, int size) throws NattoException {
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

        if (index < 0 || index >= size) {
            throw new NattoException("No such task exists.");
        }
        return index;
    }

    /**
     * Parses a todo command and extracts its description.
     *
     * @param input Full user input.
     * @return Description of the todo.
     * @throws NattoException If the description is missing or empty.
     */
    public static String parseTodo(String input) throws NattoException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new NattoException("The description of a todo cannot be empty.");
        }
        return parts[1].trim();
    }

    /**
     * Parses a deadline command into a {@link Deadline} task.
     * Accepted date formats: yyyy-mm-dd or yyyy-mm-dd HHmm.
     *
     * @param input Full user input.
     * @return A Deadline task.
     * @throws NattoException If "/by" is missing, description is empty, or date format is invalid.
     */
    public static Deadline parseDeadline(String input) throws NattoException {
        if (!input.contains("/by")) {
            throw new NattoException("Natto.Deadline must have /by. "
                    + "Example: deadline [something] /by [yyyy-mm-dd HH]  ");
        }

        String desc = input.substring(9, input.indexOf("/by")).trim();
        String byString = input.substring(input.indexOf("/by") + 3).trim();

        if (desc.isEmpty()) {
            throw new NattoException("The description of a deadline cannot be empty.");
        }

        LocalDateTime by;
        if (byString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            by = LocalDate.parse(byString).atStartOfDay();
        } else if (byString.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            by = LocalDateTime.parse(byString, f);
        } else {
            throw new NattoException("Invalid date format. Use yyyy-mm-dd or yyyy-mm-dd HHmm");
        }

        return new Deadline(desc, by);
    }

    /**
     * Parses an event command into an {@link Event} task.
     * Accepted formats:
     * <ul>
     *   <li>/from: yyyy-mm-dd or yyyy-mm-dd HHmm</li>
     *   <li>/to: HHmm or yyyy-mm-dd HHmm</li>
     * </ul>
     *
     * @param input Full user input.
     * @return An Event task.
     * @throws NattoException If "/from" or "/to" is missing, description is empty, or date format is invalid.
     */
    public static Event parseEvent(String input) throws NattoException {
        if (!input.contains("/from") || !input.contains("/to")) {
            throw new NattoException("Natto.Event must have /from and /to.");
        }

        String desc = input.substring(6, input.indexOf("/from")).trim();
        String fromString = input.substring(input.indexOf("/from") + 5, input.indexOf("/to")).trim();
        String toString = input.substring(input.indexOf("/to") + 3).trim();

        if (desc.isEmpty()) {
            throw new NattoException("The description of an event cannot be empty.");
        }

        LocalDateTime from;
        LocalDateTime to;

        if (fromString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            from = LocalDate.parse(fromString).atStartOfDay();
        } else if (fromString.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            from = LocalDateTime.parse(fromString, f);
        } else {
            throw new NattoException("Invalid date format. Use yyyy-mm-dd or yyyy-mm-dd HHmm");
        }

        if (toString.matches("\\d{4}")) {
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("HHmm");
            LocalTime t = LocalTime.parse(toString, tf);
            to = LocalDateTime.of(from.toLocalDate(), t);
        } else if (toString.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            to = LocalDateTime.parse(toString, f);
        } else {
            throw new NattoException("Invalid date format. Use yyyy-mm-dd or yyyy-mm-dd HHmm");
        }

        return new Event(desc, from, to);
    }

    /**
     * Parses a find command and extracts the keyword.
     *
     * @param input Full user input.
     * @return Keyword to search for.
     * @throws NattoException If the keyword is missing or empty.
     */
    public static String parseFind(String input) throws NattoException {
        String[] parts = input.split(" ", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new NattoException("Please provide a keyword to find.");
        }

        return parts[1].trim();
    }
    /**
     * Parses a contact command into a {@link Contact}.
     * Format: contact NAME p/PHONE e/EMAIL a/ADDRESS(optional)
     *
     * @param input Full user input.
     * @return A Contact object.
     * @throws NattoException If required fields are missing or empty.
     */
    public static Contact parseContact(String input) throws NattoException {
        // contact NAME p/PHONE e/EMAIL a/ADDRESS(optional)
        String args = input.substring("contact".length()).trim();
        if (args.isEmpty()) {
            throw new NattoException("Usage: contact NAME p/PHONE e/EMAIL [a/ADDRESS]");
        }

        String name = args.split(" p/")[0].trim();
        if (name.isEmpty()) {
            throw new NattoException("Contact name cannot be empty.");
        }

        String phone = extractAfter(args, "p/");
        String email = extractAfter(args, "e/");
        String address = extractAfterOptional(args, "a/");

        return new Contact(name, phone, email, address);
    }
    
    private static String extractAfter(String args, String prefix) throws NattoException {
        String[] parts = args.split(prefix);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new NattoException("Missing or empty value for " + prefix);
        }
        return parts[1].split(" ")[0].trim();
    }
    
    private static String extractAfterOptional(String args, String prefix) {
        String[] parts = args.split(prefix);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            return "";
        }
        return parts[1].split(" ")[0].trim();
    }
}
