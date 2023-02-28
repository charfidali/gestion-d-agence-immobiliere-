package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Services.IServicePublication;
import pi.app.estatemarket.dto.PublicationDTO;

import java.util.List;

@AllArgsConstructor
@RestController
public class ControllerPublication {
    IServicePublication iServicePublication;

    @GetMapping("/RetrieveAllPublications")
    List<Publication> retrieveAllPublications(){

        return iServicePublication.getAllPublications();
    }
    @PutMapping("/UpdatePublication/{ID}")
    Publication updatePublication (@PathVariable int ID, @RequestBody Publication publication){

        return iServicePublication.updatePublication(ID, publication);
    }

    @GetMapping("/RetrievePublication/{IdPublication}")
    ResponseEntity<Publication> retrievePublication (@PathVariable Integer IdPublication){
        Publication publication = iServicePublication.retrievePublication(IdPublication);
        return ResponseEntity.ok().body(publication);
    }
@DeleteMapping("/DeletePublication/{IdPublication}")
    void removePublication (@PathVariable Integer IdPublication){
        iServicePublication.removePublication(IdPublication);
}

    @PostMapping("/ajouterEtAffecterPublicationAuser/{userID}")
    public void ajouterEtAffecterPublicationAuser(@RequestBody Publication publication, @PathVariable Long userID){
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
        return iServicePublication.getPublicationsOrderByLikes();
    }

    @PostMapping("/like/{IdPublication}/{userID}")
    public ResponseEntity<String> addLikeToPublication(@PathVariable int IdPublication, @PathVariable long userID){
        try {
            iServicePublication.addLikeToPublication(IdPublication, userID);
            return ResponseEntity.ok("Like added to post with Id " + IdPublication + " by user with Id " + userID);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding like: " + e.getMessage());
        }
    }

}
