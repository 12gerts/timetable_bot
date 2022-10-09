import org.bot.Logic;
import org.bot.Report;
import org.bot.Week;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogicTest {
    Logic logic = new Logic();
    Week week = new Week();
    @Test
    void getHello() {
        String actual = logic.getHello();
        String expected = Report.START_REPORT;
        assertEquals(expected, actual);
    }

    @Test
    void getReportHelp() {
        String actual = logic.getReport("/help");
        String expected = Report.HELP_REPORT;
        assertEquals(expected, actual);
    }
    @Test
    void getReportToday() {
        String actual = logic.getReport("/today");
        String expected = week.today();
        assertEquals(expected, actual);
    }
    @Test
    void getReportTomorrow() {
        String actual = logic.getReport("/tomorrow");
        String expected = week.tomorrow();
        assertEquals(expected, actual);
    }
    @Test
    void getReportWeek() {
        String actual = logic.getReport("/week");
        String expected = week.week();
        assertEquals(expected, actual);
    }
    @Test
    void getReportDefault() {
        String actual = logic.getReport("/jfgshjkdfghdsjkgh");
        String expected = Report.DEFAULT_REPORT;
        assertEquals(expected, actual);
    }
}