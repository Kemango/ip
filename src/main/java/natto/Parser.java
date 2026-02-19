package natto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Parses user input into commands and task details.
 */
public class Parser {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HHmm");
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
     * Parses an index from commands like "done 2" or "delete 3".
     *
     * @param input Full user input.
     * @param size  Current number of tasks (for index validation).
     * @return Zero-based index of the task to operate on.
     * @throws NattoException If the index is missing, not a number, or out of bounds.
     */
    public static int parseIndex(String input, int size) throws NattoException {
        String indexString = extractIndexArgument(input);
        int userIndex = parseInteger(indexString);
        return validateAndConvertIndex(userIndex, size);
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

        LocalDateTime by = parseDateTime(byString);
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
        ensureHasFromTo(input);

        String desc = between(input, "event ", "/from").trim();
        String fromString = between(input, "/from", "/to").trim();
        String toString = after(input, "/to").trim();

        if (desc.isEmpty()) {
            throw new NattoException("The description of an event cannot be empty.");
        }

        LocalDateTime from = parseDateTime(fromString);
        LocalDateTime to = parseEventTo(toString, from);

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
    /** Extracts a datetime in either "yyyy-mm-dd" or "yyyy-mm-dd HHmm". */
    private static LocalDateTime parseDateTime(String s) throws NattoException {
        if (s.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(s).atStartOfDay();
        }
        if (s.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
            return LocalDateTime.parse(s, DATE_TIME_FORMATTER);
        }
        throw new NattoException("Invalid date format. Use yyyy-mm-dd or yyyy-mm-dd HHmm");
    }
    /** Helper method to extract the value after a prefix (e.g. "p/") and ensure it's not empty */
    private static String extractAfter(String args, String prefix) throws NattoException {
        String[] parts = args.split(prefix);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new NattoException("Missing or empty value for " + prefix);
        }
        return parts[1].split(" ")[0].trim();
    }
    /** Similar to extractAfter but returns empty string if the field is optional and not provided */
    private static String extractAfterOptional(String args, String prefix) {
        String[] parts = args.split(prefix);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            return "";
        }
        return parts[1].split(" ")[0].trim();
    }
    /** Extracts the substring between two markers. */
    private static String between(String input, String left, String right) {
        int start = input.indexOf(left) + left.length();
        int end = input.indexOf(right);
        return input.substring(start, end);
    }
    /** Extracts the substring after a marker. */
    private static String after(String input, String prefix) {
        return input.substring(input.indexOf(prefix) + prefix.length());
    }
    /** Ensures that the input contains both "/from" and "/to" for event parsing. */
    private static void ensureHasFromTo(String input) throws NattoException {
        if (!input.contains("/from") || !input.contains("/to")) {
            throw new NattoException("Natto.Event must have /from and /to.");
        }
    }
    /** Parses the "to" part of an event, which can be either a time (HHmm) or a full datetime. */
    private static LocalDateTime parseEventTo(String toString, LocalDateTime from) throws NattoException {
        if (toString.matches("\\d{4}")) {
            LocalTime t = LocalTime.parse(toString, TIME_FORMATTER);
            return LocalDateTime.of(from.toLocalDate(), t);
        }
        return parseDateTime(toString);
    }
    /** Extracts the index argument from commands like "done 2" or "delete 3". */
    private static String extractIndexArgument(String input) throws NattoException {
        String[] parts = input.trim().split("\\s+");
        ensureHasArgument(parts);
        return parts[1];
    }
    /** Parses a string into an integer, throwing a NattoException if it's not a valid number. */
    private static int parseInteger(String s) throws NattoException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new NattoException("Index must be a number.");
        }
    }
    /** Validates that the user-provided index is within bounds and converts it to zero-based. */
    private static int validateAndConvertIndex(int userIndex, int size) throws NattoException {
        int zeroBased = userIndex - 1;

        if (zeroBased < 0 || zeroBased >= size) {
            throw new NattoException("No such task exists.");
        }

        return zeroBased;
    }
    /** Ensures that the command has an argument (e.g. "done 2" has "2" as an argument). */
    private static void ensureHasArgument(String[] parts) throws NattoException {
        if (parts.length < 2) {
            throw new NattoException("Please specify a task index.");
        }
    }
}
