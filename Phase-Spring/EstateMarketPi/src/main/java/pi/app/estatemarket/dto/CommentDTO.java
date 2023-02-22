package pi.app.estatemarket.dto;

import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {
    private int IdComment;
    private Date DateComment;
    private String DescriptionCommentaire;

}
