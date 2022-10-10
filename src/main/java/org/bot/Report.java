package org.bot;

public class Report {
    public static final String START_REPORT = "Привет!\n" +
            "Чтобы узнать обо мне введите команду /help";
    public static final String HELP_REPORT = """
            Я умею показывать расписание и добавлять домашнее задание
            /help\t\tвывести это сообщение
            /today\t\tузнать расписание на сегодня
            /tomorrow\tузнать расписание на завтра
            /week\t\tузнать расписание на неделю
            /weeks\t\tузнать расписание на 2 недели
            /change\t\tизменить номер группы
            /auth\t\tдобавить номер группы
            /add\t\tдобавить домашнее задание""";
    public static final String AUTHORIZATION_REPORT = "Введите номер группы в формате [код института]-ХХХХХХ";
    public static final String AUTHORIZATION_ERROR = "Такой группы не существует" ;
    public static final String DEFAULT_REPORT = "Неизвестная команда :( Для просмотра команд введите команду /help";
    public static final String NO_IMPLEMENTATION = "На данный момент отсутствует реализация";
    public static final String GROUP_CHANGE = "Номер группы успешно изменен";
    public static final String NO_SCHEDULE = "На данный день расписание отсутствует";
}

