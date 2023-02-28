package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    // ...

    public String getFilteredDescriptionCommentaire() {
        String filteredDescription = this.DescriptionCommentaire;
        // Replace any occurrence of bad words with asterisks (*)
        filteredDescription = filteredDescription.replaceAll("(?i)badword1|badword2|badword3", "***");
        return filteredDescription;
    }

    // ...

    @ManyToOne
    private User userComment;
    @JsonIgnore
    @ManyToOne
    private Publication commPub;


    @PrePersist
    protected void onCreate() {
        DateComment = new Date();
    }
}

