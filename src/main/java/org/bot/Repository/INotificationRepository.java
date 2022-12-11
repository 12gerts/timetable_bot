package org.bot.Repository;

import java.util.Date;

public interface INotificationRepository {
    Long getIdByDate(Date date);

    void put(Date date, Long id);

    Date earliestDate();

    void remove(Date date);
}
