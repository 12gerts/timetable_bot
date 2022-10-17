import org.bot.*;
import org.bot.Telegram.Telegram;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogicTest {

    private final Logic logic = new Logic(
            new GroupTest.MockHttpRequest(),
            new MockWeek(),
            new MockGroup()
    );
    private static final Reader reader = new Reader();
    private static final String today;

    static {
        try {
            today = reader.readFile("src/test/resources/today.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String tomorrow;

    static {
        try {
            tomorrow = reader.readFile("src/test/resources/tomorrow.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String week;

    static {
        try {
            week = reader.readFile("src/test/resources/week.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String weeks;

    static {
        try {
            weeks = reader.readFile("src/test/resources/weeks.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    void getReportChange() {
        String actual = logic.parseMessage("/change", null);
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getCorrectChange() {
        Telegram.map.put("1", "123");
        logic.setLastMessage("/change");

        String actual = logic.parseMessage("мен-210204", "1");
        String expected = Report.GROUP_CHANGE;
        assertEquals(expected, actual);
    }

    @Test
    void getIncorrectChange() {
        Telegram.map.put("2", "123");
        logic.setLastMessage("/change");

        String actual = logic.parseMessage("мен-000000", "2");
        String expected = Report.REQUEST_ERROR;
        assertEquals(expected, actual);
    }

    @Test
    void getDoubleCommand() {
        Telegram.map.put("1", null);
        logic.setLastMessage("/today");

        String actual = logic.parseMessage("/today", "1");
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void addGroupAndGetScheduleWhenCorrect() {
        Telegram.map.put("1", null);
        logic.setLastMessage("/today");

        String actual = logic.parseMessage("мен-210204", "1");
        assertEquals(today, actual);
    }

    @Test
    void addGroupAndGetScheduleWhenIncorrect() {
        Telegram.map.put("1", null);
        logic.setLastMessage("/today");

        String actual = logic.parseMessage("мен-000000", "1");
        String expected = Report.REQUEST_ERROR;
        assertEquals(expected, actual);
    }

    @Test
    void getReportDefault() {
        String actual = logic.getReport("/non-exist", null);
        String expected = Report.DEFAULT_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getAuthorizationReportWhenTodayAndNull() {
        String actual = logic.getReport("/today", null);
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getAuthorizationReportWhenTomorrowAndNull() {
        String actual = logic.getReport("/tomorrow", null);
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getAuthorizationReportWhenWeekAndNull() {
        String actual = logic.getReport("/week", null);
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getAuthorizationReportWhenWeeksAndNull() {
        String actual = logic.getReport("/weeks", null);
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getAuthorizationReportWhenTodayAndNotNull() {
        String actual = logic.getReport("/today", "1");
        assertEquals(today, actual);
    }

    @Test
    void getAuthorizationReportWhenTomorrowAndNotNull() {
        String actual = logic.getReport("/tomorrow", "1");
        assertEquals(tomorrow, actual);
    }

    @Test
    void getAuthorizationReportWhenWeekAndNotNull() {
        String actual = logic.getReport("/week", "1");
        assertEquals(week, actual);
    }

    @Test
    void getAuthorizationReportWhenWeeksAndNotNull() {
        String actual = logic.getReport("/weeks", "1");
        assertEquals(weeks, actual);
    }

    public static class MockWeek implements IWeek {

        @Override
        public String today(String calendar) {
            return today;
        }

        @Override
        public String tomorrow(String calendar) {
            return tomorrow;
        }

        @Override
        public String week(String calendar, int amountOfDays) {
            if (amountOfDays == 7) {
                return week;
            } else if (amountOfDays == 14) {
                return weeks;
            }
            return null;
        }
    }
    public static class MockGroup implements IGroup {
        @Override
        public String convertAndUpdateNumberOfGroup(String group, String chatId) {
            if (Objects.equals(group, "мен-210204")) {
                Telegram.map.replace(chatId, "54627");
            } else {
                Telegram.map.replace(chatId, null);
            }
            return null;
        }

        @Override
        public String checkGroupChange(String chatId) {
            if (Objects.equals(chatId, "1")) {
                return Report.GROUP_CHANGE;
            } else {
                return Report.REQUEST_ERROR;
            }
        }
    }
}
