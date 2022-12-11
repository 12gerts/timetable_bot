package org.bot;

import org.bot.Entity.Notification;
import org.bot.Http.HttpRequest;
import org.bot.Http.IHttpRequest;
import org.bot.Repository.INotificationRepository;
import org.bot.Notification.UserAnswer;
import org.bot.Parser.ParserJson;
import org.bot.Services.INotificationService;
import org.bot.Repository.GroupRepository;
import org.bot.Telegram.Keyboards.ButtonType;
import org.bot.Telegram.Keyboards.KeyboardType;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.bot.Telegram.Keyboards.ButtonType.*;
import static org.bot.Telegram.Keyboards.KeyboardType.INLINE;
import static org.bot.Telegram.Keyboards.KeyboardType.REPLY;

/**
 * Класс, реализующий базовую логику бота
 */
public class Logic {
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * Поле, хранящее последнее сообщение пользователя
     */
    private String lastMessage = null;
    private UserAnswer userAnswer;
    private final String[] COMMAND_WEEK = {"/week", "/weeks"};
    private final String[] COMMAND_BUTTON_DAYS = {"/day", "/add"};
    private final String[] COMMAND_WITH_GROUP = {"/day", "/add", "/week", "/weeks", "/change"};
    private final IGroup group;
    private final INotificationService notificationService;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final IWeek week;
    private final IHttpRequest request;
    private final INotificationRepository notificationRepository;
    private final GroupRepository groupRepository;


    public ButtonType getButtonType() {
        return buttonType;
    }

    private ButtonType buttonType = NONE;

    /**
     * Конструктор для тестирования
     *
     * @param request                MockHttpRequest
     * @param week                   MockWeek
     * @param group                  MockGroup
     * @param notificationService    MockNtf
     * @param notificationRepository
     */

    public Logic(IHttpRequest request, IWeek week, IGroup group, INotificationService notificationService, INotificationRepository notificationRepository, GroupRepository groupRepository) {
        this.request = request;
        this.week = week;
        this.group = group;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.groupRepository = groupRepository;
    }

    /**
     * Конструктор по умолчанию
     */
    public Logic(GroupRepository groupRepository, INotificationRepository notificationRepository, INotificationService notificationService) {
        this.request = new HttpRequest();
        this.week = new Week();
        this.group = new Group(new HttpRequest(), new ParserJson(), groupRepository);
        this.userAnswer = new UserAnswer();
        this.notificationService = notificationService;
        this.groupRepository = groupRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * Метод, определяющий тип клавиатуры
     *
     * @param textMsg сообщение пользователя
     * @return тип клавиатуры
     */
    public KeyboardType getKeyboardType(String textMsg) {
        KeyboardType keyboardType;
        if (Objects.equals(textMsg, Report.CHOOSE_DAY)) {
            keyboardType = INLINE;
            buttonType = DAYS;
        } else if (Objects.equals(textMsg.substring(0, 14), Report.SCHEDULE)) {
            keyboardType = INLINE;
            buttonType = SCHEDULE;
        } else {
            keyboardType = REPLY;
            buttonType = NONE;
        }
        return keyboardType;
    }

    /**
     * Замена сообщений на специальные команды
     *
     * @param textMsg сообщение
     * @return специальная команда
     */

    private String handle(String textMsg) {
        return switch (textMsg) {
            case "Расписание на 1 день" -> "/day";
            case "Четность недели" -> "/evenness";
            case "Расписание звонков" -> "/call";
            case "Добавить уведомление" -> "/add";
            case "Изменить номер группы" -> "/change";
            case "1 неделя" -> "/week";
            case "2 недели" -> "/weeks";
            default -> textMsg;
        };
    }

    /**
     * Метод, обрабатывающий сообщения пользователя
     *
     * @param textMsg сообщение пользователя
     * @param chatId  внутренний номер чата
     * @return ответ пользователю
     */
    public String parseMessage(String textMsg, String chatId) {
        textMsg = handle(textMsg);
        if (textMsg.startsWith("/")) {
            return getSpecialHandler(textMsg, chatId);
        } else {
            return getNonSpecialHandler(textMsg, chatId);
        }
    }

    /**
     * Обработка неспециальных команд
     *
     * @param textMsg сообщение пользователя
     * @param chatId  внутренний номер чата
     * @return ответ пользователю
     */
    public String getNonSpecialHandler(String textMsg, String chatId) {
        String response = null;
        if (textMsg.length() > 3 && Objects.equals(textMsg.substring(0, 4), Report.SUBJECT)) {
            userAnswer.setSubject(textMsg.substring(4));
            lastMessage = Report.SUBJECT;
            response = Report.ASK_DATE;
        } else if (Objects.equals(lastMessage, Report.SUBJECT)) {
            Date currentDate = week.parseDate(textMsg, dateFormat);
            if (currentDate != null) {
                userAnswer.setDate(currentDate);
                lastMessage = Report.ASK_MESSAGE;
                response = Report.ASK_MESSAGE;
            } else {
                response = Report.RETRY_DATE;
            }
        } else if (Objects.equals(lastMessage, Report.ASK_MESSAGE)) {
            userAnswer.setMessage(textMsg);
            Notification notification = Notification.createNotification(
                    userAnswer.getMessage(),
                    Long.valueOf(chatId),
                    userAnswer.getDate(),
                    userAnswer.getSubject()
            );
            notificationService.saveNotification(notification);
            notificationRepository.put(userAnswer.getDate(), notification.getId());
            response = Report.DONE;
            lastMessage = null;
        } else if (week.isValid(textMsg) && groupRepository.getGroupNumber(chatId) != null) {
            if (Objects.equals(lastMessage, "/day")) {
                // подается дата в /day, когда задана группа
                return getReport(textMsg, groupRepository.getGroupNumber(chatId));
            } else if (Objects.equals(lastMessage, "/add")) {
                // подается дата в /add, когда задана группа
                return Report.SCHEDULE + textMsg;
            }
        } else if (Objects.equals(lastMessage, "/change")) {
            //подается группа в /change для смены
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            response = group.checkGroupChange(chatId);
        } else if (Arrays.asList(COMMAND_WEEK).contains(lastMessage) && groupRepository.getGroupNumber(chatId) == null) {
            //подается группа в /week(s)
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            if (groupRepository.getGroupNumber(chatId) == null) {
                response = Report.REQUEST_ERROR;
            } else {
                response = getReport(lastMessage, groupRepository.getGroupNumber(chatId));
            }
        } else if (Arrays.asList(COMMAND_BUTTON_DAYS).contains(lastMessage) && groupRepository.getGroupNumber(chatId) == null) {
            //подается группа в /add, /day
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            if (groupRepository.getGroupNumber(chatId) == null) {
                response = Report.REQUEST_ERROR;
            } else {
                response = Report.CHOOSE_DAY;
            }
        } else {
            response = Report.DEFAULT_REPORT;
        }
        return response;
    }


    /**
     * Обработка специальных команд
     *
     * @param textMsg сообщение пользователя
     * @param chatId  внутренний номер чата
     * @return ответ пользователю
     */
    public String getSpecialHandler(String textMsg, String chatId) {
        String response;
        if (textMsg.equals("/start")) {
            // обработка /start
            response = getHello();
        } else if (textMsg.equals("/help")) {
            // обработка /help
            response = Report.HELP_REPORT;
        } else if (textMsg.equals("/change")) {
            // обработка /change
            groupRepository.setGroupNumber(chatId, null);
            response = Report.AUTHORIZATION_REPORT;
        } else if (textMsg.equals("/call")) {
            Reader reader = new Reader();
            try {
                response = reader.readFile("src/main/resources/call.txt");
            } catch (IOException e) {
                return null;
            }
        } else if (textMsg.equals("/evenness")) {
            if (week.evenness()) {
                response = Report.ODD;
            } else {
                response = Report.EVEN;
            }
        } else if (Arrays.asList(COMMAND_WITH_GROUP).contains(textMsg) && groupRepository.getGroupNumber(chatId) == null) {
            // обработка /week(s) /day /add, если нет группы
            response = Report.AUTHORIZATION_REPORT;
        } else if (Arrays.asList(COMMAND_WEEK).contains(textMsg)) {
            // обработка /week(s), если есть группа
            response = getReport(textMsg, groupRepository.getGroupNumber(chatId));
        } else if (Arrays.asList(COMMAND_BUTTON_DAYS).contains(textMsg)) {
            // обработка /day, /add если есть группа
            response = Report.CHOOSE_DAY;
        } else {
            // неверная команда
            response = Report.DEFAULT_REPORT;
        }
        lastMessage = textMsg;
        return response;
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
     * Метод, выдающий расписание и обрабатывающий неверный ввод
     *
     * @param innerGroup внутренний номер группы
     * @param report     команда пользователя
     * @return ответ пользователю: расписание или предупреждающее сообщение
     */
    public String getReport(String report, String innerGroup) {
        String calendar = request.getSchedule(innerGroup);
        return switch (report) {
            case "/week" -> week.week(calendar, 7);
            case "/weeks" -> week.week(calendar, 14);
            default -> week.day(calendar, week.parseDate(report));
        };
    }

    /**
     * Метод, возвращающий массив, элементами которого являются предметы
     *
     * @param innerNumber внутренний номер чата
     * @param date        дата, на которую составляется расписание
     */
    public List<String> getReportKey(String innerNumber, String date) {
        String calendar = request.getSchedule(innerNumber);
        return week.today(calendar, week.parseDate(date));
    }
}

