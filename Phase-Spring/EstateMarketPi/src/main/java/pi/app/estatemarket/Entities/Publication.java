package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity(name = "publication")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Publication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdPublication;
    private String TitrePub;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    //updatable = false empêche toute modification de la date de publication une fois que l'objet a été créé
    private Date DatePublication;
    private String DescriptionPublication;

    private int nombreLike=0;

    @JsonIgnore
    @ManyToOne
    private UserApp userAppPub;

    //----------
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Likee> likes = new ArrayList<>();


    //@JsonIgnore //au cas où lezemesh les commentaires yokhrjou maa les listes de pubs
    @OneToMany(mappedBy = "commPub")
    private Set<Comment>commentsPub;

    @PrePersist
    protected void onCreate() {
        DatePublication = new Date();
    }

    private Integer views; // attribut pour stocker le nombre de vues du post


    public void incrementViews() {
        this.views++;
    }



    //-------


}
