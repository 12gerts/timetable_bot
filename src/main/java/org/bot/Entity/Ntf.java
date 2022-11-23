package org.bot.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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

    @OneToOne(mappedBy = "ntf")
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private SendMessage sendMessage;
}
