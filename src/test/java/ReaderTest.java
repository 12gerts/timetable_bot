import org.bot.Reader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReaderTest {
    private final Reader reader = new Reader();

    @Test
    public void readFile() throws IOException {
        String expected = "Test 1\nTest 2";
        String actual = reader.readFile("src/test/resources/test_reader.txt");
        assertEquals(expected, actual);
    }
}
