package org.bot;

import java.util.Scanner;

public class Logic {
    public String getHello() {
        return Report.START_REPORT;
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        while (true) {
            String command = input.nextLine();
            String message = getReport(command);
            System.out.println(message);
        }
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
