package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.catalina.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "appointment")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdAppointment;

    @Temporal(TemporalType.DATE)
    private Date date;

    private boolean AppointmentStatus;

    @JsonIgnore
    @ManyToOne
    private Announcement announcementApp;

    @JsonIgnore
    @ManyToMany
    private List<UserApp> users;

    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    /* @Enumerated(EnumType.STRING)
     private DayOfWeek dayOfWeek;*/
    @Column(name = "day_of_week")
    private String dayOfWeek;




}