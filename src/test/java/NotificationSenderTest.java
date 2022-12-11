import org.bot.Entity.Notification;
import org.bot.Entity.SendMessageFlag;
import org.bot.Repository.INotificationRepository;
import org.bot.Notification.NotificationSender;
import org.bot.Services.INotificationService;
import org.bot.Telegram.IMessageSender;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificationSenderTest {

    @Test
    public void trySendNotification() throws TelegramApiException {
        MockNotificationRepository mockNotificationRepository = new MockNotificationRepository();
        MockMessageSender mockMessageSender = new MockMessageSender();
        MockNotificationService mockNotificationService = new MockNotificationService();
        NotificationSender notificationSender = new NotificationSender(mockNotificationRepository, mockMessageSender, mockNotificationService);
        notificationSender.trySendNotification();
        assertTrue(mockMessageSender.sendMessageCalled);
        assertTrue(mockNotificationService.isMessageSent);
        assertTrue(mockNotificationRepository.isRemoved);
    }

    public static class MockNotificationService implements INotificationService {
        public boolean isMessageSent = false;

        @Override
        public void saveNotification(Notification notification) {

        }

        @Override
        public void setMessageSent(Long id) {
            isMessageSent = true;
        }

        @Override
        public List<SendMessageFlag> getAllNotSentMessages() {
            return null;
        }

        @Override
        public Notification findById(String id) {
            Date date;
            try {
                date = new SimpleDateFormat("dd.MM.yyyy").parse("10.11.2022");
            } catch (Exception e) {
                date = null;
            }
            return Notification.createNotification("content", 1L, date, "subject");
        }
    }

    public static class MockNotificationRepository implements INotificationRepository {
        public boolean isRemoved = false;

        @Override
        public Long getIdByDate(Date date) {
            return 1L;
        }

        @Override
        public void put(Date date, Long id) {

        }

        @Override
        public Date earliestDate() {
            try {
                return new SimpleDateFormat("dd.MM.yyyy").parse("10.11.2022");
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public void remove(Date date) {
            isRemoved = true;
        }
    }

    public static class MockMessageSender implements IMessageSender {
        boolean sendMessageCalled = false;

        @Override
        public void sendMessage(String chatId, String message) {
            sendMessageCalled = true;
        }
    }
}

