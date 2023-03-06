package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import pi.app.estatemarket.Entities.Payment;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Repository.PaymentRepository;
import pi.app.estatemarket.dto.UserDTO;

@Service
@AllArgsConstructor
public class PaymentService implements  IServicePayment{


    private final String stripeApiKey;
    private Environment environment;
    @Autowired
    public PaymentService(String stripeApiKey) {
        this.stripeApiKey = stripeApiKey;


    }

    private PaymentRepository paymentRepository;

    public Payment processPayment(UserDTO user, double amount, String paymentMethod) throws StripeException {

        Stripe.apiKey = stripeApiKey;

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("description", "Payment for Estate Market services");
        chargeParams.put("source", paymentMethod);

        Charge charge = Charge.create(chargeParams);

        Payment payment = new Payment();
        payment.setPaymentDate(new Date());
        payment.setPaymentId(charge.getId());
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setUserApp(new UserApp());

        paymentRepository.save(payment);

        return payment;

    }

}
