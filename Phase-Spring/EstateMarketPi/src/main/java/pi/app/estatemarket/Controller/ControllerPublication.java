package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.Likee;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Repository.PublicationRepository;
import pi.app.estatemarket.Services.IServicePublication;

import java.util.List;

@AllArgsConstructor
@RestController
public class ControllerPublication {
    IServicePublication iServicePublication;
    PublicationRepository publicationRepository;

    @GetMapping("/RetrieveAllPublications")
    List<Publication> retrieveAllPublications() {

        return iServicePublication.getAllPublications();
    }

    @PutMapping("/UpdatePublication/{ID}")
    Publication updatePublication(@PathVariable int ID, @RequestBody Publication publication) {

        return iServicePublication.updatePublication(ID, publication);
    }

    @GetMapping("/RetrievePublication/{IdPublication}")
    public ResponseEntity<Publication> RetrievePublication(@PathVariable Integer IdPublication) {
        try {
            Publication publication = iServicePublication.retrievePublication(IdPublication);
            return ResponseEntity.ok(publication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/DeletePublication/{IdPublication}")
    public ResponseEntity<String> removePublication(@PathVariable Integer IdPublication) {
        try {
            iServicePublication.removePublication(IdPublication);
            return ResponseEntity.ok("Publication with Id " + IdPublication + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Publication with Id " + IdPublication + " does not exist.");
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


    @PostMapping("/addlike/{idPost}/{idUser}")
    public ResponseEntity<String> addLikeToPost(@RequestBody Likee likee,  @PathVariable Integer idPost, @PathVariable Long idUser) throws Exception{
        try {
            iServicePublication.addLikeToPost(likee, idPost, idUser);
            return ResponseEntity.ok("Like added to post with Id " + idPost + " by user with Id " + idUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding like: " + e.getMessage());
        }
    }

    @PostMapping("/Disable comments/{publicationId}/{userID}")
    public ResponseEntity<String> interdireCommentaires(@PathVariable int publicationId, @PathVariable Long userID) {
        try {
            Publication publication = publicationRepository.findById(publicationId).orElseThrow(() -> new Exception("Publication with ID " + publicationId + " does not exist."));
            if (!publication.getCommentsEnabled()) {
                throw new Exception("Comments are already disabled for this post.");
            }

            iServicePublication.interdireCommentaires(publicationId, userID);

            return ResponseEntity.ok("Comments disabled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }


}