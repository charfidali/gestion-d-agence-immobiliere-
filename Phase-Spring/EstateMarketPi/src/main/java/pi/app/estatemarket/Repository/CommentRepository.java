package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pi.app.estatemarket.Entities.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM comments c WHERE LOWER(c.DescriptionCommentaire) NOT LIKE LOWER('%badword1%') " +
            "AND LOWER(c.DescriptionCommentaire) NOT LIKE LOWER('%badword2%') " +
            "AND LOWER(c.DescriptionCommentaire) NOT LIKE LOWER('%badword3%')")
    List<Comment> findAllFiltered();
}
