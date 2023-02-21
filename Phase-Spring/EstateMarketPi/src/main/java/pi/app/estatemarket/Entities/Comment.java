package pi.app.estatemarket.Entities;

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
    @Temporal(TemporalType.DATE)
    private Date DateComment;
    private String DescriptionCommentaire;

    @ManyToOne
    private User userComment;

    @ManyToOne
    private Publication commPub;

}

