package natto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StartTest {
    @Test
    public void constructor_noInput_setsGreetingAsLastOutput() {
        Natto natto = new Natto();
        String greeting = natto.getGreeting();
        String expected = "Hello! I'm Natto \uD83D\uDE38\n"
                + "What can I do for you? \uD83D\uDE3AMeow~";
        assertEquals(expected, greeting);
    }
}

