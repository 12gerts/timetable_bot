import org.bot.Entity.Notification;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationTest {

    @Test
    void createNotification() {
        Notification notification = Notification.createNotification("content", 1L, new Date(), "subject");
        assertEquals("content", notification.getContent());
        assertEquals(1L, notification.getChatId());
        assertEquals("subject", notification.getSubject());
    }
}
