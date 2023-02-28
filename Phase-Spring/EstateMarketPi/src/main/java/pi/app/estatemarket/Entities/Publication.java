package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    private int likePub;

@JsonIgnore
    @ManyToOne
    private User userPub;

//@JsonIgnore //au cas où lezemesh les commentaires yokhrjou maa les listes de pubs
    @OneToMany(mappedBy = "commPub")
    private Set<Comment>commentsPub;

    @JsonIgnore
    @OneToMany(mappedBy = "pubL", cascade = CascadeType.ALL)
    private Set<Likee> likes ;


    @JsonIgnore
    @OneToMany(mappedBy = "pubDL", cascade = CascadeType.ALL)
    private Set<Dislike> dislikes ;


    private Integer views; // attribut pour stocker le nombre de vues du post


    public void incrementViews() {
        this.views++;
    }

    @PrePersist
    protected void onCreate() {
        DatePublication = new Date();
    }

}
