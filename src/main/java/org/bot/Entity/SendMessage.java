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
public class SendMessage {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = {@Parameter(name = "property", value = "ntf")})
    private Long id;

    private boolean isSend;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Ntf ntf;

    @Override
    public String toString() {
        return "Id = " + id + "\n isSend = " + isSend + "\n";
    }
}
