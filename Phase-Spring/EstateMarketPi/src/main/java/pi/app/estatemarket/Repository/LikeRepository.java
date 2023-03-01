package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.app.estatemarket.Entities.Likee;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Entities.User;

@Repository
public interface LikeRepository extends JpaRepository<Likee, Integer> {
    boolean existsByPostAndUserL(Publication post, User user);
}
