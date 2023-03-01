package pi.app.estatemarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pi.app.estatemarket.Repository.PublicationRepository;

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
    private int IdLike;

    @ManyToOne
    @JsonIgnore
    private User userL;
    @ManyToOne
    @JsonIgnore
    private Publication post;
}
