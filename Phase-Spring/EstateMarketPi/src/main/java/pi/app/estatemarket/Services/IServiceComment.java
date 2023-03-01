package pi.app.estatemarket.Services;

import pi.app.estatemarket.Entities.Comment;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.dto.CommentDTO;
import pi.app.estatemarket.dto.PublicationDTO;

import java.util.List;

public interface IServiceComment {
    List<CommentDTO> getAllComments();
    Comment retrieveComment (Integer IdComment);
    Comment updateComment (int IdComment, Comment comm);
    void removeComment (Integer IdComment);
    public void ajouterEtAffecterCommentaireAUserEtCommentaire(Comment comment, Long userID, int IdPublication ) ;


    //-----------------













    //public void AffectPubToComment(int IdComment, int IdPublication);
    //List<Comment> retrieveAllComments();
    //Comment addComment (Comment comm);



  //  List<Comment> getAllFilteredComments();
}
