package org.bot;

public class Report {
    public static final String START_REPORT = "Привет!\n" + "Чтобы узнать обо мне введите команду /help";
    public static final String HELP_REPORT = """
            Я умею показывать расписание и добавлять домашнее задание
                        
            /help
            вывести это сообщение
                        
            /day
            узнать расписание на любой день в течение 2 недель
                        
            /week
            узнать расписание на неделю
                        
            /weeks
            узнать расписание на 2 недели
                        
            /change
            изменить номер группы
                        
            /evenness
            узнать четность недели
                        
            /call
            расписание звонков
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
    public static final String SUBJECT = "sbj0";
    public static final String ASK_MESSAGE = "Введите текст уведомления";
    public static final String DONE = "Уведомление успешно добавлено";
    public static final String ASK_DATE = "Введите дату и время в формате DD.MM.YYYY HH:MM";
    public static final String RETRY_DATE = "Неверный формат ввода. Повторите попытку";
    public static final String BEFORE_DATE = "Введенная дата уже прошла. Повторите попытку";
}

