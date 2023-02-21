package pi.app.estatemarket.Services;

import pi.app.estatemarket.Entities.Comment;

import java.util.List;

public interface IServiceComment {
    List<Comment> retrieveAllComments();

    Comment updateComment (Comment comm);

    Comment addComment (Comment comm);

    Comment retrieveComment (Integer IdComment);

    void removeComment (Integer IdComment);

    public void AffectPubToComment(int IdComment, int IdPublication);
}
