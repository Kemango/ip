package natto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void deadlineTest1CorrectFormat() {
        LocalDateTime by = LocalDateTime.of(2026, 1, 1, 12, 0);
        Deadline d = new Deadline("submit report", by);

        String result = d.toString();

        assertTrue(result.contains("[D]"));
        assertTrue(result.contains("submit report"));
        assertTrue(result.contains("2026"));
    }

    @Test
    public void deadlineTest2CorrectFormat() {
        LocalDateTime by = LocalDateTime.of(2026, 1, 29, 12, 0);
        Deadline d = new Deadline("Finish IP", by);
        d.mark();

        String result = d.toString();
        assertTrue(result.contains("[D]"));
        assertTrue(result.contains("Finish IP"));
        assertTrue(result.contains("[X]"));
    }
}
