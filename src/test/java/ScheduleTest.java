import org.bot.Parser.Schedule;
import org.bot.Reader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduleTest {
    private final Schedule schedule = new Schedule();
    private final Reader reader = new Reader();
    private final String calendar = reader.readFile("src/test/resources/calendar.ics");
    private final Date date = new SimpleDateFormat("dd.MM.yyyy").parse("11.10.2022");

    public ScheduleTest() throws IOException, ParseException {
    }

    @Test
    void parseCalendar() throws IOException {
        String actual = schedule.parseCalendar(calendar, date);
        String expected = reader.readFile("src/test/resources/today2.txt");
        assertEquals(expected, actual);
    }

    @Test
    void parseCalendarWithList() throws IOException {
        List<String> list = new ArrayList<>();
        List<String> actualList = schedule.parseCalendar(calendar, date, list);
        String actual = String.join("\n", actualList);
        String expected = reader.readFile("src/test/resources/another_today.txt");
        assertEquals(expected, actual);
    }

    @Test
    public void getSchedule() throws IOException {
        String actual = schedule.parseCalendar(calendar, date);
        String expected = reader.readFile("src/test/resources/parse_calendar.txt");
        assertEquals(expected, actual);
    }
}
