package org.bot.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Ntf implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long chatId;

    private String content;

    private Date date;

    @OneToOne(mappedBy = "ntf", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SendMessage sendMessage;

    @Override
    public String toString() {
        return "Id = " + id + "\nchatId = " + chatId + "\ncontent = " + content + "\ndate = " + date;
    }
}
