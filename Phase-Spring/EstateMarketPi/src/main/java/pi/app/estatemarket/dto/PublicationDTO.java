package pi.app.estatemarket.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PublicationDTO {
    private int IdPublication;
    private String TitrePub;
    private Date DatePublication;
    private String DescriptionPublication;

}
