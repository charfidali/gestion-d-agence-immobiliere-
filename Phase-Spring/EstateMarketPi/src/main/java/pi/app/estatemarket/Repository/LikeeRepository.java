package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.app.estatemarket.Entities.Likee;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Entities.User;

public interface LikeeRepository extends JpaRepository<Likee, Integer> {

    Likee findByPubLAndUserL(Publication publication, User user);
}
