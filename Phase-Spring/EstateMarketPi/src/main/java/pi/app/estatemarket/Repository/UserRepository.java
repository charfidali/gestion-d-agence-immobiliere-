package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.app.estatemarket.Entities.UserApp;

public interface UserRepository extends JpaRepository<UserApp,Long> {
}
