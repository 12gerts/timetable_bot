import org.bot.*;
import org.bot.Http.HttpRequest;
import org.bot.Reader;
import org.bot.Telegram.Telegram;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LogicTest {
    Logic logic = new Logic();
    Week week = new Week();
    Group group = new Group();
    Reader reader = new Reader();
    HttpRequest request = new HttpRequest();
    String fixDay = "11.10.2022";
    Date fixDate = new SimpleDateFormat("dd.MM.yyyy").parse(fixDay);

    LogicTest() throws ParseException {
    }


    @BeforeAll
    public static void putMap() {
        Telegram.map.put("1", null);
        Telegram.map.put("2", null);
        Telegram.map.put("3", "54627");
        Telegram.map.put("4", null);
        Telegram.map.put("5", null);
        Telegram.map.put("6", null);
        Telegram.map.put("7", null);
    }

    @Test
    void getHello() {
        String actual = logic.parseMessage("/start", null);
        String expected = Report.START_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getReportHelp() {
        String actual = logic.parseMessage("/help", null);
        String expected = Report.HELP_REPORT;
        assertEquals(expected, actual);
    }


    @Test
    void getReportDefault() {
        String actual = logic.parseMessage("/non-exist", null);
        String expected = Report.DEFAULT_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getInnerNumber() {
        String actual = group.convertAndUpdateNumberOfGroup("мен-210204", "1");
        String expected = "54627";
        assertEquals(expected, actual);
    }

    @Test
    void getIncorrectInnerNumber() {
        String actual = group.convertAndUpdateNumberOfGroup("мен-000000", "1");
        assertNull(actual);
    }

    @Test
    void repeatTodayCommand() {
        logic.parseMessage("/today", "2");
        String actual = logic.parseMessage("/today", "2");
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getCalendar() {
        String actual = request.getSchedule("54627").substring(0, 15);
        String expected = "BEGIN:VCALENDAR";
        assertEquals(expected, actual);
    }

    @Test
    void getToday() throws IOException {
        week.today = fixDate;
        String calendar = reader.readFile("src/test/calendar.ics");
        String expected = reader.readFile("src/test/today.txt");
        String actual = week.today(calendar);
        assertEquals(expected, actual);
    }

    @Test
    public void itShouldThrowNullPointerExceptionWhenBlahBlah() {
        assertThrows(IllegalStateException.class, () -> week.nameOfDay(8));
    }


    @Test
    void getTomorrow() throws IOException {
        week.today = fixDate;
        String calendar = reader.readFile("src/test/calendar.ics");
        String expected = reader.readFile("src/test/tomorrow.txt");
        String actual = week.tomorrow(calendar);
        assertEquals(expected, actual);
    }

    @Test
    void getWeek() throws IOException {
        week.today = fixDate;
        String calendar = reader.readFile("src/test/calendar.ics");
        String expected = reader.readFile("src/test/week.txt");
        String actual = week.week(calendar, 7);
        assertEquals(expected, actual);
    }

    @Test
    void getWeeks() throws IOException {
        week.today = fixDate;
        String calendar = reader.readFile("src/test/calendar.ics");
        String expected = reader.readFile("src/test/weeks.txt");
        String actual = week.week(calendar, 14);
        assertEquals(expected, actual);
    }


    @Test
    void readFile() throws IOException {
        String expected = "Test 1\nTest 2";
        String actual = reader.readFile("src/test/test_reader.txt");
        assertEquals(expected, actual);
    }

    @Test
    void getBotUsername() {
        Telegram telegram = new Telegram();
        String expected = telegram.getBotUsername();
        assertNotEquals(null, expected);
    }

    @Test
    void getBotToken() {
        Telegram telegram = new Telegram();
        String expected = telegram.getBotToken();
        assertNotEquals(null, expected);
    }

    @Test
    void getChange() {
        String actual = logic.parseMessage("/change", null);
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getIncorrectLastChange() {
        logic.parseMessage("/change", "1");
        String actual = logic.parseMessage("1234567890", "1");
        String expected = Report.REQUEST_ERROR;
        assertEquals(expected, actual);
    }

    @Test
    void getLastChange() {
        logic.parseMessage("/change", "1");
        String actual = logic.parseMessage("мен-210201", "1");
        String expected = Report.GROUP_CHANGE;
        assertEquals(expected, actual);
    }

    @Test
    void getLastToday() {
        logic.parseMessage("/today", "4");
        String schedule = logic.parseMessage("мен-210201", "4");
        int index = schedule.indexOf("\n");
        String actual = schedule.substring(0, index);
        String expected = week.nameOfDay(week.getNumberOfWeekDay(new Date()));
        assertEquals(expected, actual);
    }

    @Test
    void getLastTomorrow() {
        logic.parseMessage("/tomorrow", "2");
        String schedule = logic.parseMessage("мен-210201", "2");
        int index = schedule.indexOf("\n");
        String actual = schedule.substring(0, index);
        String expected = week.nameOfDay(week.getNumberOfWeekDay(week.getNextDay(new Date())));
        assertEquals(expected, actual);
    }

    @Test
    void getLastWeek() {
        logic.parseMessage("/week", "5");
        String schedule = logic.parseMessage("мен-210201", "5");
        int index = schedule.indexOf("\n");
        String actual = schedule.substring(0, index);
        String expected = week.nameOfDay(week.getNumberOfWeekDay(new Date()));
        assertEquals(expected, actual);
    }

    @Test
    void getLastWeeks() {
        logic.parseMessage("/weeks", "6");
        String schedule = logic.parseMessage("мен-210201", "6");
        int index = schedule.indexOf("\n");
        String actual = schedule.substring(0, index);
        String expected = week.nameOfDay(week.getNumberOfWeekDay(new Date()));
        assertEquals(expected, actual);
    }

    @Test
    void getWrongGroup() {
        logic.parseMessage("/weeks", "7");
        String actual = logic.parseMessage("мен-000000", "7");
        String expected = Report.REQUEST_ERROR;
        assertEquals(expected, actual);
    }

    @Test
    void getWrongHttpRequest() {
        String actual = request.getSchedule("00000000000");
        assertNull(actual);
    }

}