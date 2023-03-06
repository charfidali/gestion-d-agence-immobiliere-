package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pi.app.estatemarket.Entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment,String> {
}
