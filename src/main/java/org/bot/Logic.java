package org.bot;

import java.util.Scanner;

/**
 * Класс, реализующий базовую логику бота
 */
public class Logic {
    public void run() {
        Scanner input = new Scanner(System.in);
        Group group = new Group();
        while (true) {
            String command = input.nextLine();
            String message = getReport(command, group);
            System.out.println(message);
        }
    }
    /**
     * Метод, вызывающий приветственное сообщение
     *
     * @return приветственное сообщение
     */
    public String getHello() {
        return Report.START_REPORT;
    }

    /**
     * Метод, обрабатывающий команды, полученные от пользователя
     *
     * @param report команда пользавателя
     * @param group  экземпляр класса Group, хранящий текущую учебную группу
     * @return ответ пользователю: расписание или предупреждающее сообщение
     */
    public String getReport(String report, Group group) {
        Week week = new Week();
        return switch (report) {
            case "/change" -> group.changeGroup();
            case "/today" -> week.today(group);
            case "/tomorrow" -> week.tomorrow(group);
            case "/week" -> week.week(group, 7);
            case "/weeks" -> week.week(group, 14);
            case "/add", "/auth" -> Report.NO_IMPLEMENTATION;
            case "/help" -> Report.HELP_REPORT;
            default -> Report.DEFAULT_REPORT;
        };
    }
}

