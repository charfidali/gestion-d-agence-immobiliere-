package pi.app.estatemarket.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Likee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLike;


    @ManyToOne
    private User userL;
    @ManyToOne
    private Publication pubL;
}

