package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pi.app.estatemarket.Entities.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {
}
