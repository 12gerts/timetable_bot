package org.bot;

import org.bot.Repository.NotificationRepository;
import org.bot.Notification.NotificationSender;
import org.bot.Services.INotificationService;
import org.bot.Services.NotificationService;
import org.bot.Repository.GroupRepository;
import org.bot.Telegram.Keyboards.Button;
import org.bot.Telegram.Keyboards.Keyboards;
import org.bot.Telegram.Telegram;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;



public class Main {
    public static void main(String[] args) {
        INotificationService notificationService = new NotificationService();
        GroupRepository groupRepository = new GroupRepository();
        Reader reader = new Reader();
        NotificationRepository notificationRepository = new NotificationRepository(notificationService);
        Logic logic = new Logic(groupRepository, notificationRepository, notificationService);
        Button button = new Button(groupRepository, logic);
        Keyboards keyboards = new Keyboards(button);
        Telegram telegram = new Telegram(groupRepository, logic, keyboards, reader);
        NotificationSender notificationSender = new NotificationSender(notificationRepository, telegram, notificationService);
        notificationRepository.fillWithNotSentMessages();

        try {
            Thread threadNotification = new Thread(notificationSender);
            threadNotification.start();

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegram);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
