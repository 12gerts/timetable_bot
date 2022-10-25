package org.bot;

public class Report {
    public static final String START_REPORT = "Привет!\n" + "Чтобы узнать обо мне введите команду /help";
    public static final String HELP_REPORT = """
            Я умею показывать расписание и добавлять домашнее задание
            
            /help
            вывести это сообщение
            
            /today
            узнать расписание на сегодня
            
            /tomorrow
            узнать расписание на завтра
            
            /week
            узнать расписание на неделю
            
            /weeks
            узнать расписание на 2 недели
            
            /change
            изменить номер группы
            """;
    public static final String AUTHORIZATION_REPORT = "Введите номер группы в формате [код института]-ХХХХХХ";
    public static final String REQUEST_ERROR = "Ошибка\nПроверьте правильность группы или интернет-соединение";
    public static final String DEFAULT_REPORT = "Неизвестная команда :( Для просмотра команд введите команду /help";
    public static final String GROUP_CHANGE = "Номер группы успешно изменен";
    public static final String NO_SCHEDULE = "На данный день расписание отсутствует";
    public static final String CHOOSE_DAY = "Выберите день";
    public static final String SCHEDULE = "Расписание на ";
    public static final String ODD = "Неделя нечетная (верхняя)";
    public static final String EVEN = "Неделя четная (нижняя)";
}

