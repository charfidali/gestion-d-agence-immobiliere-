package pi.app.estatemarket.Services;

import pi.app.estatemarket.Entities.Likee;
import pi.app.estatemarket.Entities.Publication;

import java.util.List;

public interface IServicePublication {
   // List<Publication> retrieveAllPublications();

    List<Publication> getAllPublications();

    Publication updatePublication (int ID, Publication publication);


    Publication retrievePublication (Integer IdPublication);

    void removePublication (Integer IdPublication);

    public void ajouterEtAffecterPublicationAuser(Publication publication, Long userID);

    public Long countCommentsByPublicationId(Integer idPublication);

    public Publication getPublicationWithComments(int id);



    //-----------------------------------------

    void addLikeToPost(Likee likee, Integer idPost, Long idUser) throws Exception;


    //  public List<Publication> getMostCommentedPublications();
    // public void AffectUserToPub (Long userID, int IdPublication);
    //    public Publication addPublication (Publication pb);
}
