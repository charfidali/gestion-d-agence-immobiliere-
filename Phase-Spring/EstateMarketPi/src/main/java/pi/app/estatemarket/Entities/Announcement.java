package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name = "announcement")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Announcement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdAnnouncement;
    @Enumerated(EnumType.STRING)
    private GoodType goodType;
    @Enumerated(EnumType.STRING)
    private LeaseType leaseType;
    private String title;
    private String areaAnn;
    private float PriceAnn;
    private String DescriptionAnn;
    private String AddressAnn;
    private String ImageAnn;
    private Boolean status;
    @Temporal(TemporalType.DATE)
    private Date DisponibiliteAnn;

    @JsonIgnore
    @ManyToOne
    private UserApp userAppAnnouncement;
    @JsonIgnore

    @OneToMany(mappedBy = "announcementApp")
    private Set<Appointment> appointments;

    @JsonIgnore
    @ManyToMany
    private Set<Sponsoring> sponsoringAnnouncement;

    @JsonIgnore
    @OneToMany(mappedBy ="promotion")
    private Set<Promotion> promotions;

    public Announcement(float priceAnn,String descriptionAnn,String imageAnn) {
        PriceAnn = priceAnn;
        DescriptionAnn =descriptionAnn;
        ImageAnn =imageAnn;
    }
    public Announcement(String descriptionAnn) {

        DescriptionAnn =descriptionAnn;

    }
}
