package pi.app.estatemarket.Services;

import pi.app.estatemarket.Entities.Comment;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.dto.CommentDTO;
import pi.app.estatemarket.dto.PublicationDTO;

import java.util.List;

public interface IServiceComment {
    List<CommentDTO> getAllComments();
    Comment retrieveComment (Integer IdComment) throws Exception;
    Comment updateComment (int IdComment, Comment comm) throws Exception;
    void removeComment (Integer IdComment) throws Exception;
    public void ajouterEtAffecterCommentaireAUserEtCommentaire(Comment comment, Long userID, int IdPublication ) throws Exception;



    Comment reportComment(int idComment, long userId) throws Exception;


    //-----------------













    //public void AffectPubToComment(int IdComment, int IdPublication);
    //List<Comment> retrieveAllComments();
    //Comment addComment (Comment comm);



  //  List<Comment> getAllFilteredComments();
}
