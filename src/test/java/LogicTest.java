import org.bot.*;
import org.bot.Repository.INotificationRepository;
import org.bot.Repository.NotificationRepository;
import org.bot.Services.INotificationService;
import org.bot.Services.NotificationService;
import org.bot.Repository.GroupRepository;
import org.bot.Telegram.Keyboards.ButtonType;
import org.bot.Telegram.Keyboards.KeyboardType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.bot.Telegram.Keyboards.ButtonType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogicTest {
    private static final Reader reader = new Reader();
    private final GroupRepository groupRepository = new GroupRepository();
    private final INotificationService notificationService = new NotificationService();
    private final INotificationRepository notificationRepository = new NotificationRepository(notificationService);
    private final Logic logic = new Logic(
            new GroupTest.MockHttpRequest(),
            new MockWeek(),
            new MockGroup(groupRepository),
            notificationService,
            notificationRepository,
            groupRepository
    );
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
        groupRepository.setGroupNumber("1", "123");
        logic.setLastMessage("/change");

        String actual = logic.parseMessage("мен-210204", "1");
        String expected = Report.GROUP_CHANGE;
        assertEquals(expected, actual);
    }

    @Test
    void getIncorrectChange() {
        groupRepository.setGroupNumber("2", "123");
        logic.setLastMessage("/change");

        String actual = logic.parseMessage("мен-000000", "2");
        String expected = Report.REQUEST_ERROR;
        assertEquals(expected, actual);
    }

    @Test
    void getDoubleCommand() {
        groupRepository.setGroupNumber("1", null);
        logic.setLastMessage("/day");
        String actual = logic.parseMessage("/day", "1");
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void addGroupAndGetScheduleWhenCorrect() {
        groupRepository.setGroupNumber("1", null);
        logic.setLastMessage("/day");
        String actual = logic.parseMessage("мен-210204", "1");
        assertEquals("Выберите день", actual);
    }

    @Test
    void addGroupAndGetScheduleWhenIncorrect() {
        groupRepository.setGroupNumber("1", null);
        logic.setLastMessage("/day");
        String actual = logic.parseMessage("мен-000000", "1");
        String expected = Report.REQUEST_ERROR;
        assertEquals(expected, actual);
    }

    @Test
    void getReportDefault() {
        String actual = logic.parseMessage("default", null);
        String expected = Report.DEFAULT_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getAuthorizationReportWhenDayAndNull() {
        groupRepository.setGroupNumber("1", "123");
        String actual = logic.parseMessage("/day", "1");
        assertEquals("Выберите день", actual);
    }

    @Test
    void getAuthorizationReportWhenWeekAndNull() {
        groupRepository.setGroupNumber("1", null);
        String actual = logic.parseMessage("/week", "1");
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getAuthorizationReportWhenWeeksAndNull() {
        groupRepository.setGroupNumber("1", null);
        String actual = logic.parseMessage("/weeks", "1");
        String expected = Report.AUTHORIZATION_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getAuthorizationReportWhenDayAndNotNull() {
        groupRepository.setGroupNumber("1", "1");
        String actual = logic.parseMessage("/day", "1");
        assertEquals("Выберите день", actual);
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

    @Test
    void getKeyboardTypeWithChoseDay() {
        KeyboardType actual = logic.getKeyboardType("Выберите день");
        ButtonType expected = DAYS;
        KeyboardType expected2 = KeyboardType.INLINE;
        assertEquals(expected, logic.getButtonType());
        assertEquals(expected2, actual);
    }

    @Test
    void getKeyboardTypeWithSchedule() {
        KeyboardType actual = logic.getKeyboardType("Расписание на ");
        ButtonType expected = SCHEDULE;
        KeyboardType expected2 = KeyboardType.INLINE;
        assertEquals(expected, logic.getButtonType());
        assertEquals(expected2, actual);
    }

    @Test
    void getKeyboardTypeWithElse() {
        KeyboardType actual = logic.getKeyboardType("Стандартный текст");
        ButtonType expected = NONE;
        KeyboardType expected2 = KeyboardType.REPLY;
        assertEquals(expected2, actual);
        assertEquals(expected, logic.getButtonType());
    }

    public static class MockWeek implements IWeek {

        @Override
        public List<String> today(String calendar, Date date) {
            return null;
        }

        @Override
        public String day(String calendar, Date date) {
            return null;
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

        @Override
        public boolean isValid(String dateStr) {
            return false;
        }

        @Override
        public Date parseDate(String report) {
            return null;
        }

        @Override
        public Date parseDate(String report, DateFormat stf) {
            return null;
        }

        @Override
        public boolean evenness() {
            return false;
        }
    }

    public static class MockGroup implements IGroup {
        GroupRepository groupRepository;

        public MockGroup(GroupRepository groupRepository) {
            this.groupRepository = groupRepository;
        }

        @Override
        public String convertAndUpdateNumberOfGroup(String group, String chatId) {
            if (Objects.equals(group, "мен-210204")) {
                groupRepository.setGroupNumber(chatId, "54627");
            } else {
                groupRepository.setGroupNumber(chatId, null);
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
