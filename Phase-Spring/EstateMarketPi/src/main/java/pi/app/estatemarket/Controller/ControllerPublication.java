package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pi.app.estatemarket.Entities.Likee;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Services.IServicePublication;

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
    void removePublication(@PathVariable Integer IdPublication) throws Exception {
        try {
            iServicePublication.removePublication(IdPublication);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication with ID " + IdPublication + " does not exist.", e);
        }

    }

    @PostMapping("/addAndAffectPublicationTouser/{userID}")
    public ResponseEntity<String> ajouterEtAffecterPublicationAuser(@RequestBody Publication publication, @PathVariable Long userID) throws Exception{
        try {

            iServicePublication.addAndAffectPublicationTouser(publication, userID);
            return ResponseEntity.ok("Publication added by user Id"+ userID);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding publication: " + e.getMessage());
        }
    }


    @GetMapping("countCommentsByPublication/{idPublication}")
    public Long countCommentsByPublicationId(@PathVariable Integer idPublication) {
        return iServicePublication.countCommentsByPublicationId(idPublication);
    }

    @GetMapping("/getPublicationWithComments/{id}")
    public Publication getPublicationWithComments(@PathVariable int id) {
        return iServicePublication.getPublicationWithComments(id);
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

    //--------------------------


    /*    @GetMapping("/Afficher toutes les publications avec les commentaires décroissants")
    public List<Publication> getPopularPublications() {
        return iServicePublication.getMostCommentedPublications();
    }*/
}