package pi.app.estatemarket.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "payment")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    private String paymentId;

    private String paymentMethod;

    private double amount;

    @ManyToOne
    @JsonIgnore
    private UserApp userApp;

}
