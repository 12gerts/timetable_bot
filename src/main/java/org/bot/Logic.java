package org.bot;

import org.bot.Http.HttpRequest;
import org.bot.Http.IHttpRequest;
import org.bot.Telegram.Keyboards.ButtonType;
import org.bot.Telegram.Keyboards.KeyboardType;
import org.bot.Telegram.Telegram;

import java.io.IOException;
import java.util.Arrays;
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
    private final String[] COMMAND_WEEK = {"/week", "/weeks"};
    private final String[] COMMAND_BUTTON_DAYS = {"/day", "/add"};
    private final String[] COMMAND_WITH_GROUP = {"/day", "/add", "/week", "/weeks", "/change"};
    private final IGroup group;
    private final IWeek week;
    private final IHttpRequest request;

    public ButtonType getButtonType() {
        return buttonType;
    }

    private ButtonType buttonType = NONE;

    /**
     * Конструктор для тестирования
     *
     * @param request MockHttpRequest
     * @param week    MockWeek
     * @param group   MockGroup
     */

    public Logic(IHttpRequest request, IWeek week, IGroup group) {
        this.request = request;
        this.week = week;
        this.group = group;
    }

    /**
     * Конструктор по умолчанию
     */
    public Logic() {
        this.request = new HttpRequest();
        this.week = new Week();
        this.group = new Group();
    }

    public Enum<KeyboardType> getKeyboardType(String textMsg) {
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

    public String getNonSpecialHandler(String textMsg, String chatId) {
        String response = null;
        // Переделать
        if (week.isValid(textMsg) && Telegram.map.get(chatId) != null) {
            if (Objects.equals(lastMessage, "/day")) {
                // подается дата в /day, когда задана группа
                return getReport(textMsg, Telegram.map.get(chatId));
            } else if (Objects.equals(lastMessage, "/add")) {
                // подается дата в /add, когда задана группа
                return Report.SCHEDULE + textMsg;
            }
        } else if (Objects.equals(lastMessage, "/change")) {
            //подается группа в /change для смены
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            response = group.checkGroupChange(chatId);
        } else if (Arrays.asList(COMMAND_WEEK).contains(lastMessage) && Telegram.map.get(chatId) == null) {
            //подается группа в /week(s)
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            if (Telegram.map.get(chatId) == null) {
                response = Report.REQUEST_ERROR;
            } else {
                response = getReport(lastMessage, Telegram.map.get(chatId));
            }
        } else if (Arrays.asList(COMMAND_BUTTON_DAYS).contains(lastMessage) && Telegram.map.get(chatId) == null) {
            //подается группа в /add, /day
            group.convertAndUpdateNumberOfGroup(textMsg, chatId);
            if (Telegram.map.get(chatId) == null) {
                response = Report.REQUEST_ERROR;
            } else {
                response = Report.CHOOSE_DAY;
            }
        } else {
            response = Report.DEFAULT_REPORT;
        }
        return response;
    }


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
            Telegram.map.replace(chatId, null);
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
        } else if (Arrays.asList(COMMAND_WITH_GROUP).contains(textMsg) && Telegram.map.get(chatId) == null) {
            // обработка /week(s) /day /add, если нет группы
            response = Report.AUTHORIZATION_REPORT;
        } else if (Arrays.asList(COMMAND_WEEK).contains(textMsg)) {
            // обработка /week(s), если есть группа
            response = getReport(textMsg, Telegram.map.get(chatId));
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

    public List<String> getReportKey(String innerNumber, String date) {
        String calendar = request.getSchedule(innerNumber);
        return week.today(calendar, week.parseDate(date));
    }
}

