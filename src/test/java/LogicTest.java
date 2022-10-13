import org.bot.*;
import org.bot.Http.HttpRequest;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogicTest {
    Logic logic = new Logic();
    Week week = new Week();
    Group group = new Group();
    Schedule schedule = new Schedule();
    HttpRequest request = new HttpRequest();
    String fixDay = "11.10.2022";
    Date fixDate = new SimpleDateFormat("dd.MM.yyyy").parse(fixDay);

    LogicTest() throws ParseException {
    }

    public String readFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Test
    void getHello() {
        String actual = logic.getHello();
        String expected = Report.START_REPORT;
        assertEquals(expected, actual);
    }
    /*
    @Test
    void getReportHelp() {
        String actual = logic.getReport("/help", group);
        String expected = Report.HELP_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getReportDefault() {
        String actual = logic.getReport("/non-exist", group);
        String expected = Report.DEFAULT_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getInnerNumber() {
        String actual = group.convertAndUpdateNumberOfGroup("мен-210204");
        String expected = "54627";
        assertEquals(expected, actual);
    }

    @Test
    void getIncorrectInnerNumber() {
        String actual = group.convertAndUpdateNumberOfGroup("мен-000000");
        assertNull(actual);
    }
    */
    @Test
    void getTomorrowSchedule() throws IOException, ParseException {
        Date nextDay = week.getNextDay(fixDate);
        String nameOfDay = week.nameOfDay(week.getNumberOfWeekDay(nextDay));
        String calendar = readFile("src/test/calendar.ics");
        String expected = readFile("src/test/tomorrow.txt");
        String actual = nameOfDay + "\n" + schedule.parseCalendar(calendar, nextDay);
        assertEquals(expected, actual);
    }
    @Test
    void getTodaySchedule() throws IOException, ParseException {
        String nameOfDay = week.nameOfDay(week.getNumberOfWeekDay(fixDate));
        String calendar = readFile("src/test/calendar.ics");
        String expected = readFile("src/test/today.txt");
        String actual = nameOfDay + "\n" + schedule.parseCalendar(calendar, fixDate);
        assertEquals(expected, actual);
    }

    /*
    @Test
    void checkGroupChange() {
        String actual = group.checkGroupChange();
        String expected = Report.REQUEST_ERROR;
        assertEquals(expected, actual);
    }
    */
    @Test
    void getInnerDataApi() {
        String actual = request.getInnerNumber("мен-210204");
        String expected = "{\"suggestions\": [{\"value\": \"\\u041c\\u0415\\u041d-210204\", \"data\": 54627}]}\n";
        assertEquals(expected, actual);
    }

}