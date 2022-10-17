import org.bot.Parser.ParserJson;
import org.bot.Reader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserJsonTest {
    private final Reader reader = new Reader();
    private final String notEmptyJson = reader.readFile("src/test/resources/not_empty_json.txt");
    private final String emptyJson = reader.readFile("src/test/resources/empty_json.txt");
    private final ParserJson jsonEmpty = new ParserJson();
    private final ParserJson jsonNotEmpty = new ParserJson();

    public ParserJsonTest() throws IOException {
    }


    @Test
    public void getDataNotEmptyJson() {
        jsonNotEmpty.parseJson(notEmptyJson);
        String actual = jsonNotEmpty.getData();
        String expected = "54627";
        assertEquals(expected, actual);
    }

    @Test
    public void getValueNotEmptyJson() {
        jsonNotEmpty.parseJson(notEmptyJson);
        String actual = jsonNotEmpty.getValue();
        String expected = "МЕН-210204";
        assertEquals(expected, actual);
    }

    @Test
    public void getDataEmptyJson() {
        jsonEmpty.parseJson(emptyJson);
        assertThrows(ArrayIndexOutOfBoundsException.class, jsonEmpty::getData);
    }

    @Test
    public void getValueEmptyJson() {
        jsonEmpty.parseJson(emptyJson);
        assertThrows(ArrayIndexOutOfBoundsException.class, jsonEmpty::getValue);
    }

    @Test
    public void isNotEmptyForNotEmptyJson() {
        jsonNotEmpty.parseJson(notEmptyJson);
        Boolean actual = jsonNotEmpty.notEmpty();
        Boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void isNotEmptyForEmptyJson() {
        jsonEmpty.parseJson(emptyJson);
        Boolean actual = jsonEmpty.notEmpty();
        Boolean expected = false;
        assertEquals(expected, actual);
    }
}
