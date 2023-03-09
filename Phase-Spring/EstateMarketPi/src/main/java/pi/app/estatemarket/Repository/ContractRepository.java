package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.app.estatemarket.Entities.Contract;


public interface ContractRepository extends JpaRepository<Contract,Integer> {



}
