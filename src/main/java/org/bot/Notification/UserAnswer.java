package org.bot.Notification;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserAnswer {

    private Date date;

    private String message;

    private String subject;
}
