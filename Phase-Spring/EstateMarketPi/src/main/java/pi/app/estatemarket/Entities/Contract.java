package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "contract")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdContract;
    @Temporal(TemporalType.DATE)
    private Date StartDateContract;
    @Temporal(TemporalType.DATE)
    private Date EndDateContract;
    private String TypeContract;
    private String status;
    private String signature;
    @JsonIgnore

    @ManyToOne
    private UserApp userAppContract;


    public boolean isContractActive() {
        Date currentDate = new Date();
        System.out.println("Current date: " + currentDate);
        System.out.println("End date: " + this.EndDateContract);
        boolean isActive = this.EndDateContract.after(currentDate);
        System.out.println("Is active: " + isActive);
        return isActive;
    }
}
