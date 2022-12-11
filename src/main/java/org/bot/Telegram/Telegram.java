package org.bot.Telegram;

import org.bot.Logic;
import org.bot.Reader;
import org.bot.Repository.GroupRepository;
import org.bot.Telegram.Keyboards.Keyboards;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

import static org.bot.Telegram.Keyboards.KeyboardType.INLINE;

public class Telegram extends TelegramLongPollingBot implements IMessageSender {
    private final GroupRepository groupRepository;
    private final Logic logic;
    private final Keyboards keyboards;
    private final Reader reader;

    public Telegram(GroupRepository groupRepository, Logic logic, Keyboards keyboards, Reader reader) {
        this.groupRepository = groupRepository;
        this.logic = logic;
        this.keyboards = keyboards;
        this.reader = reader;
    }


    /**
     * Метод, который читает имя бота из файла и присваивает его в BOT_NAME
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        String botName;
        try {
            botName = reader.readFile("src/main/resources/name.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return botName;
    }

    /**
     * Метод, который читает токен бота из файла и присваивает его в BOT_TOKEN
     *
     * @return токен бота
     */
    @Override
    public String getBotToken() {
        String botToken;
        try {
            botToken = reader.readFile("src/main/resources/token.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return botToken;
    }

    /**
     * Метод, который принимает и отправляет сообщения в телеграмм
     *
     * @param update входящее обновление
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText() ||
                    update.hasCallbackQuery() && update.getCallbackQuery().getMessage().hasText()) {
                String chatId;
                String textMessage;

                if (update.hasMessage() && update.getMessage().hasText()) {
                    Message inMess = update.getMessage();
                    chatId = inMess.getChatId().toString();
                    textMessage = inMess.getText();
                } else {
                    CallbackQuery inMess = update.getCallbackQuery();
                    chatId = inMess.getMessage().getChatId().toString();
                    textMessage = inMess.getData();
                }

                groupRepository.setGroupNumberIfNotContains(chatId, null);

                SendMessage outMess = new SendMessage();
                outMess.setChatId(chatId);
                outMess.setReplyMarkup(keyboards.replyKeyboardMarkup());

                String response = logic.parseMessage(textMessage, chatId);

                if (logic.getKeyboardType(response) == INLINE) {
                    outMess.setReplyMarkup(keyboards.inlineKeyBoard(logic.getButtonType(), chatId, textMessage));
                }

                outMess.setText(response);
                execute(outMess);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void sendMessage(String chatId, String message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        execute(sendMessage);
    }
}