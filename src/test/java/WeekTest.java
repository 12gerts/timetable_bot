import org.bot.Reader;
import org.bot.Week;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeekTest {
    private final Reader reader = new Reader();
    private final Date date = new SimpleDateFormat("dd.MM.yyyy").parse("11.10.2022");
    private final Week week = new Week(date);
    String calendar = reader.readFile("src/test/resources/calendar.ics");

    public WeekTest() throws ParseException, IOException {
    }

    @Test
    void nameOfDay() {
        assertEquals("Понедельник", week.nameOfDay(1));
        assertEquals("Вторник", week.nameOfDay(2));
        assertEquals("Среда", week.nameOfDay(3));
        assertEquals("Четверг", week.nameOfDay(4));
        assertEquals("Пятница", week.nameOfDay(5));
        assertEquals("Суббота", week.nameOfDay(6));
        assertEquals("Воскресенье", week.nameOfDay(7));
    }

    @Test
    void today() throws IOException, ParseException {
        List<String> actualList = week.today(calendar, new SimpleDateFormat("dd.MM.yyyy").parse("11.10.2022"));
        String actual = String.join("\n", actualList);
        String expected = reader.readFile("src/test/resources/another_today.txt");
        assertEquals(expected, actual);
    }

    @Test
    void getNumberOfWeekDay() throws ParseException {
        assertEquals(1, week.getNumberOfWeekDay(new SimpleDateFormat("dd.MM.yyyy").parse("10.10.2022")));
        assertEquals(2, week.getNumberOfWeekDay(new SimpleDateFormat("dd.MM.yyyy").parse("11.10.2022")));
        assertEquals(3, week.getNumberOfWeekDay(new SimpleDateFormat("dd.MM.yyyy").parse("12.10.2022")));
        assertEquals(4, week.getNumberOfWeekDay(new SimpleDateFormat("dd.MM.yyyy").parse("13.10.2022")));
        assertEquals(5, week.getNumberOfWeekDay(new SimpleDateFormat("dd.MM.yyyy").parse("14.10.2022")));
        assertEquals(6, week.getNumberOfWeekDay(new SimpleDateFormat("dd.MM.yyyy").parse("15.10.2022")));
        assertEquals(7, week.getNumberOfWeekDay(new SimpleDateFormat("dd.MM.yyyy").parse("16.10.2022")));
    }

    @Test
    void isValid() {
        assertTrue(week.isValid("11.10.2022"));
        assertTrue(week.isValid("10.10.2022"));
        assertFalse(week.isValid("a.10.2022"));
        assertFalse(week.isValid("1@.$.2022"));
    }

    @Test
    void parseDate() throws ParseException {
        Date actual = week.parseDate("11.10.2022");
        Date expected = new SimpleDateFormat("dd.MM.yyyy").parse("11.10.2022");
        assertEquals(expected, actual);
    }

    @Test
    void getNextDay() throws ParseException {
        Date actual = week.getNextDay(new SimpleDateFormat("dd.MM.yyyy").parse("10.10.2022"));
        Date expected = new SimpleDateFormat("dd.MM.yyyy").parse("11.10.2022");
        assertEquals(expected, actual);
    }

    @Test
    void day() throws IOException, ParseException {
        Date date = week.getNextDay(new SimpleDateFormat("dd.MM.yyyy").parse("10.10.2022"));
        String actual = week.day(calendar, date);
        String expected = reader.readFile("src/test/resources/today.txt");
        assertEquals(expected, actual);
    }

    @Test
    void getWeek() throws IOException {
        String expected = reader.readFile("src/test/resources/week.txt");
        String actual = week.week(calendar, 7);
        assertEquals(expected, actual);
    }

    @Test
    void getWeeks() throws IOException {
        String expected = reader.readFile("src/test/resources/weeks.txt");
        String actual = week.week(calendar, 14);
        assertEquals(expected, actual);
    }
}
