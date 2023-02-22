package pi.app.estatemarket.Services;

import org.springframework.web.bind.annotation.PathVariable;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.dto.PublicationDTO;
import pi.app.estatemarket.dto.UserDTO;

import java.util.List;

public interface IServicePublication {
   // List<Publication> retrieveAllPublications();

    List<PublicationDTO> getAllPublications();

    Publication updatePublication (Publication pb);

    public Publication addPublication (Publication pb);

    Publication retrievePublication (Integer IdPublication);

    void removePublication (Integer IdPublication);

    public void AffectUserToPub (Long userID, int IdPublication);

    public void ajouterEtAffecterPublicationAuser(Publication publication, Long userID);

    public Long countCommentsByPublicationId(Integer idPublication);

    public Publication getPublicationWithComments(int id);
    public List<Publication> getMostCommentedPublications();
}
