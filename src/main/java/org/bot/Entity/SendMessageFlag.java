package org.bot.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
@Table(name = "sendmessage")
public class SendMessageFlag {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = {@Parameter(name = "property", value = "notification")})
    private Long id;

    private boolean isSend;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Notification notification;

    @Override
    public String toString() {
        return "Id = " + id + "\n isSend = " + isSend + "\n";
    }
}
