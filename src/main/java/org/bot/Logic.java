package org.bot;

public class Logic {
    public String getHello() {
        return Report.START_REPORT;
    }
    public String getReport(String report) {
        Week week = new Week();
        return switch (report) {
            case "/today" -> week.today();
            case "/tomorrow" -> week.tomorrow();
            case "/week" -> week.week();
            case "/add", "/auth" -> Report.NO_IMPLEMENTATION;
            case "/help" -> Report.HELP_REPORT;
            default -> Report.DEFAULT_REPORT;
        };
    }
}
