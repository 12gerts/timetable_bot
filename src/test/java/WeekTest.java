import org.bot.Reader;
import org.bot.Week;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeekTest {
    private final Reader reader = new Reader();
    private final Date date = new SimpleDateFormat("dd.MM.yyyy").parse("11.10.2022");
    private final Week week = new Week(date);

    public WeekTest() throws ParseException {
    }



    @Test
    void getTomorrow() throws IOException {
        String calendar = reader.readFile("src/test/resources/calendar.ics");
        String expected = reader.readFile("src/test/resources/tomorrow.txt");
        String actual = week.tomorrow(calendar);
        assertEquals(expected, actual);
    }

    @Test
    void getWeek() throws IOException {
        String calendar = reader.readFile("src/test/resources/calendar.ics");
        String expected = reader.readFile("src/test/resources/week.txt");
        String actual = week.week(calendar, 7);
        assertEquals(expected, actual);
    }

    @Test
    void getWeeks() throws IOException {
        String calendar = reader.readFile("src/test/resources/calendar.ics");
        String expected = reader.readFile("src/test/resources/weeks.txt");
        String actual = week.week(calendar, 14);
        assertEquals(expected, actual);
    }
}
