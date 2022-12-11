package org.bot.Telegram.Keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static org.bot.Telegram.Keyboards.ButtonType.SCHEDULE;

public class Keyboards {
    private final Button button;

    public Keyboards(Button button) {
        this.button = button;
    }

    public InlineKeyboardMarkup inlineKeyBoard(Enum<ButtonType> type, String chatId, String date) {
        List<List<InlineKeyboardButton>> rowList;
        if (type == SCHEDULE) {
            rowList = button.getInlineButtonSchedule(chatId, date);
        } else {
            rowList = button.getInlineButtonDays();
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public ReplyKeyboardMarkup replyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboardRows = button.getReplyButtonMenu();

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
}