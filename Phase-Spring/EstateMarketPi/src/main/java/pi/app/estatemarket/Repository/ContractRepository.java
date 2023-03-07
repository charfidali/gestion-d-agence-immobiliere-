package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pi.app.estatemarket.Entities.Contract;
import java.util.Date;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Integer> {





}


