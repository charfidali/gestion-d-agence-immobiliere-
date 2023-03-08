package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.app.estatemarket.Entities.Contract;

import java.util.Date;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract,Integer> {




}
