package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "comments")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdComment;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date DateComment;
    private String DescriptionCommentaire;


    @ManyToOne
    @JsonIgnore
    private UserApp userAppComment;


    @JsonIgnore
    @ManyToOne
    private Publication commPub;


    @PrePersist
    protected void onCreate() {
        DateComment = new Date();
    }

    //-----------

    @ElementCollection
    private Set<Long> reportedBy = new HashSet<>();

    private int signalCount = 0;
}
