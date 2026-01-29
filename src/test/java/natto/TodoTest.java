package natto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TodoTest {

    @Test
    public void todoCorrectFormat() {
        Todo t = new Todo("write report");
        assertEquals("[T][ ] write report", t.toString());
    }

    @Test
    public void todoMarkCorrectFormat() {
        Todo t = new Todo("write report");
        t.mark();
        assertEquals("[T][X] write report", t.toString());
    }

    @Test
    public void todoUnmarkCorrectFormat() {
        Todo t = new Todo("write report");
        t.mark();
        t.unmark();
        assertEquals("[T][ ] write report", t.toString());
    }
}

