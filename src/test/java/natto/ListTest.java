package natto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ListTest {
    @Test
    public void list_noTasks_showsEmptyMessage() {
        Natto natto = new Natto();

        String response = natto.getResponse("list");

        String expected = "Here are the tasks in your list:\n\n"
                + "\uD83D\uDE3D No tasks yet! \uD83D\uDE3D";

        assertEquals(expected, response);
    }

    @Test
    public void list_afterAddingOneTask_showsTask() {
        Natto natto = new Natto();

        natto.getResponse("todo a");
        String response = natto.getResponse("list");

        String expected = "Here are the tasks in your list:\n\n"
                + "1. [T][ ] a";

        assertEquals(expected, response);
        natto.getResponse("delete 1");
    }
}
