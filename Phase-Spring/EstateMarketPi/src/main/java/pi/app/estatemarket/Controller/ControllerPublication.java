package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.Likee;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Entities.User;
import pi.app.estatemarket.Repository.PublicationRepository;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.Services.IServicePublication;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@AllArgsConstructor
@RestController
public class ControllerPublication {
    IServicePublication iServicePublication;

    @GetMapping("/RetrieveAllPublications")
    List<Publication> retrieveAllPublications() {

        return iServicePublication.getAllPublications();
    }

    @PutMapping("/UpdatePublication/{ID}")
    Publication updatePublication(@PathVariable int ID, @RequestBody Publication publication) {

        return iServicePublication.updatePublication(ID, publication);
    }

    @GetMapping("/RetrievePublication/{IdPublication}")
    ResponseEntity<Publication> retrievePublication(@PathVariable Integer IdPublication) {
        Publication publication = iServicePublication.retrievePublication(IdPublication);
        return ResponseEntity.ok().body(publication);
    }

    @DeleteMapping("/DeletePublication/{IdPublication}")
    void removePublication(@PathVariable Integer IdPublication) {
        iServicePublication.removePublication(IdPublication);
    }

    @PostMapping("/ajouterEtAffecterPublicationAuser/{userID}")
    public void ajouterEtAffecterPublicationAuser(@RequestBody Publication publication, @PathVariable Long userID) {
        iServicePublication.ajouterEtAffecterPublicationAuser(publication, userID);
    }

    @GetMapping("Afficher le nombre de commentaire par publication/{idPublication}")
    public Long countCommentsByPublicationId(@PathVariable Integer idPublication) {
        return iServicePublication.countCommentsByPublicationId(idPublication);
    }

    @GetMapping("/Afficher tous les commentaire d'une publication/{id}")
    public Publication getPublicationWithComments(@PathVariable int id) {
        return iServicePublication.getPublicationWithComments(id);
    }

    @GetMapping("/Afficher toutes les publications avec les commentaires décroissants")
    public List<Publication> getPopularPublications() {
        return iServicePublication.getMostCommentedPublications();
    }

//----------

    @PostMapping("/addlike/{idPost}/{idUser}")
    public ResponseEntity<String> addLikeToPost(@RequestBody Likee likee,  @PathVariable Integer idPost, @PathVariable Long idUser) throws Exception{
        try {
            iServicePublication.addLikeToPost(likee, idPost, idUser);
            return ResponseEntity.ok("Like added to post with Id " + idPost + " by user with Id " + idUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding like: " + e.getMessage());
        }
    }

}
