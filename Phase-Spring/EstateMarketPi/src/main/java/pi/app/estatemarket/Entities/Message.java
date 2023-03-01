package pi.app.estatemarket.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "chat_message")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdMessage;
    private String ContenuMessagesage;

    @ManyToOne
    private UserApp userAppMessage;
}
