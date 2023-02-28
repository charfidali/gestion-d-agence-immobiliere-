package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.Comment;
import pi.app.estatemarket.Services.IServiceComment;
import pi.app.estatemarket.dto.CommentDTO;
import pi.app.estatemarket.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
public class ControllerComment {

    IServiceComment iServiceComment;
    @GetMapping("/RetrieveAllComments")
    List<CommentDTO> retrieveAllComments(){

        return iServiceComment.getAllComments();
    }

    @PutMapping("/UpdateComment")
    Comment updateComment (@RequestBody Comment comm){

        return iServiceComment.updateComment(comm);
    }

    @PostMapping("/AddComment")
    Comment addComment (@RequestBody Comment comm){

        return iServiceComment.addComment(comm);
    }

    @GetMapping("/retrieveComment/{IdComment}")
    Comment retrieveComment (Integer IdComment){

        return iServiceComment.retrieveComment(IdComment);
    }

    @DeleteMapping("/DeleteComment/{IdComment}")
    void removeComment (Integer IdComment){

        iServiceComment.removeComment(IdComment);
    }

    @PostMapping("/AffectPubToComment/{IdComment}/{IdPublication}")
    public void AffectPubToComment(@PathVariable int IdComment,@PathVariable int IdPublication){
        iServiceComment.AffectPubToComment(IdComment, IdPublication);
    }

    @PostMapping("/ajouterEtAffecterCommentaireAUserEtCommentaire/{userID}/{IdPublication}")
    public void ajouterEtAffecterCommentaireAUserEtCommentaire(@RequestBody Comment comment, @PathVariable Long userID, @PathVariable int IdPublication){
        iServiceComment.ajouterEtAffecterCommentaireAUserEtCommentaire(comment, userID, IdPublication);
    }

    @GetMapping("/filtered")
    public List<Comment> getAllFilteredComments() {
        return iServiceComment.getAllFilteredComments();
    }
}
