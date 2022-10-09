public class Report {
    public static final String START_REPORT = "Привет!\n" +
            "Чтобы узнать обо мне введите команду /help";
    public static final String HELP_REPORT = """
            Я умею показывать расписание и добавлять домашнее задание
            /help\t\tвыведет это сообщение
            /today\t\tузнать расписание на сегодня
            /week\t\tузнать расписание на неделю
            /tomorrow\tузнать расписание на завтра
            /auth\t\tдобавить номер группы
            /add\t\tдобавить домашнее задание""";
    public static final String AUTHORIZATION_REPORT = "Введите номер группы в формате [код института]-ХХХХХХ";
    public static final String DEFAULT_REPORT = "Неизвестная команда :( Для просмотра команд введите команду /help";
    public static final String NO_IMPLEMENTATION = "На данный момент отсутствует реализация";
    public static final String NO_SCHEDULE = "На данный день расписание отсутствует";
}
