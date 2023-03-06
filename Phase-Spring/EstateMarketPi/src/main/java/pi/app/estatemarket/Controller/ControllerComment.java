package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pi.app.estatemarket.Entities.Comment;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Repository.PublicationRepository;
import pi.app.estatemarket.Services.IServiceComment;
import pi.app.estatemarket.dto.CommentDTO;
import pi.app.estatemarket.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
public class ControllerComment {

    IServiceComment iServiceComment;
    PublicationRepository publicationRepository;

    @GetMapping("/RetrieveAllComments")
    List<CommentDTO> retrieveAllComments() {

        return iServiceComment.getAllComments();
    }

    @PutMapping("/UpdateComment/{IdComment}")
    Comment updateComment(@PathVariable int IdComment, @RequestBody Comment comm) throws Exception {
try {
    return iServiceComment.updateComment(IdComment, comm);
} catch (Exception e){
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with ID " + IdComment + " does not exist.", e);
}

    }

    @GetMapping("/retrieveComment/{IdComment}")
    Comment retrieveComment(Integer IdComment) throws Exception {
        try {
            return iServiceComment.retrieveComment(IdComment);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with ID " + IdComment + " does not exist.", e);
        }
    }

    @DeleteMapping("/DeleteComment/{IdComment}")
    ResponseEntity<String> removeComment(Integer IdComment) throws Exception {
try {
    iServiceComment.removeComment(IdComment);
    return ResponseEntity.ok("Comment with with Id " + IdComment + " has been successfully deleted");
} catch (Exception e){
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with ID " + IdComment + " does not exist.", e);
}
    }


    @PostMapping("/ajouterEtAffecterCommentaireAUserEtCommentaire/{userID}/{IdPublication}")
    public ResponseEntity<String> ajouterEtAffecterCommentaireAUserEtCommentaire(@RequestBody Comment comment, @PathVariable Long userID, @PathVariable int IdPublication) {
        try {
            iServiceComment.ajouterEtAffecterCommentaireAUserEtCommentaire(comment, userID, IdPublication);
            return ResponseEntity.ok("Comment added to Pub with Id " + IdPublication + " by user with Id " + userID);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding Comment: " + e.getMessage());
        }
    }

    //-------------------------


    @PutMapping("/reportComment/{idComment}/{userId}")
    public ResponseEntity<String> reportComment(@PathVariable int idComment, @PathVariable long userId) throws Exception {
        try {
            iServiceComment.reportComment(idComment, userId);
            return ResponseEntity.ok("Report added to comment with Id " + idComment + " by user with Id " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding Report: " + e.getMessage());
        }
    }



    //----------
    @PostMapping("/PinComment/{IdPublication}/{userID}/{idComment}")
    public ResponseEntity<String> epinglerCommentaire(@PathVariable Long userID, @PathVariable int IdPublication, @PathVariable int idComment) {
        try {

            iServiceComment.epinglerCommentaire(userID, IdPublication, idComment);
            return ResponseEntity.ok("Comment pinned successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }


    @PostMapping("/Disable comments/{publicationId}/{userID}")
    public ResponseEntity<String> interdireCommentaires(@PathVariable int publicationId, @PathVariable Long userID) {
        try {
            Publication publication = publicationRepository.findById(publicationId).orElseThrow(() -> new Exception("Publication with ID " + publicationId + " does not exist."));
            if (!publication.getCommentsEnabled()) {
                throw new Exception("Comments are already disabled for this post.");
            }

            iServiceComment.interdireCommentaires(publicationId, userID);

            return ResponseEntity.ok("Comments disabled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }


}




/*    @PostMapping("/AddComment")
    Comment addComment (@RequestBody Comment comm){

        return iServiceComment.addComment(comm);
    }
    */
    /*    @PostMapping("/AffectPubToComment/{IdComment}/{IdPublication}")
    public void AffectPubToComment(@PathVariable int IdComment,@PathVariable int IdPublication){
        iServiceComment.AffectPubToComment(IdComment, IdPublication);
    }*/


/*
    @GetMapping("/filtered")
    public List<Comment> getAllFilteredComments() {
        return iServiceComment.getAllFilteredComments();
    }*/

