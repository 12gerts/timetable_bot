package org.bot.Telegram;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface IMessageSender {
    void sendMessage(String chatId, String text) throws TelegramApiException;
}

