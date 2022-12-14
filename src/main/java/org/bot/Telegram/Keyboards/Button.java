package org.bot.Telegram.Keyboards;

import org.bot.Logic;
import org.bot.Repository.GroupRepository;
import org.bot.Week;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Button {
    private final Logic logic;
    private final GroupRepository groupRepository;

    private final Week week = new Week();

    public Button(GroupRepository groupRepository, Logic logic) {
        this.groupRepository = groupRepository;
        this.logic = logic;
    }

    private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    public List<List<InlineKeyboardButton>> getInlineButtonDays() {
        Date date = new Date();
        List<InlineKeyboardButton> keyboardButtonsRow;
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            keyboardButtonsRow = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText(df.format(date) + " " + week.shortNameOfDay(week.getNumberOfWeekDay(date)));
            inlineKeyboardButton1.setCallbackData(df.format(date));
            keyboardButtonsRow.add(inlineKeyboardButton1);
            rowList.add(keyboardButtonsRow);
            date = week.getNextDay(date);
        }
        return rowList;
    }

    public List<List<InlineKeyboardButton>> getInlineButtonSchedule(String chatId, String date) {
        List<InlineKeyboardButton> keyboardButtonsRow1;
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<String> subjects = logic.getReportKey(groupRepository.getGroupNumber(chatId), date);
        for (String subject: subjects) {
            keyboardButtonsRow1 = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText(subject);
            inlineKeyboardButton1.setCallbackData("sbj0" + subject);
            keyboardButtonsRow1.add(inlineKeyboardButton1);
            rowList.add(keyboardButtonsRow1);
        }
        return rowList;
    }

    public ArrayList<KeyboardRow> getReplyButtonMenu() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("???????????????????? ???? 1 ????????"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("1 ????????????"));
        keyboardSecondRow.add(new KeyboardButton("2 ????????????"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("???????????????? ?????????? ????????????"));

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add(new KeyboardButton("???????????????? ??????????????????????"));

        KeyboardRow keyboardFifthRow = new KeyboardRow();
        keyboardFifthRow.add(new KeyboardButton("???????????????? ????????????"));
        keyboardFifthRow.add(new KeyboardButton("???????????????????? ??????????????"));

        keyboardRows.add(keyboardFirstRow);
        keyboardRows.add(keyboardSecondRow);
        keyboardRows.add(keyboardThirdRow);
        keyboardRows.add(keyboardFourthRow);
        keyboardRows.add(keyboardFifthRow);
        return keyboardRows;
    }

}