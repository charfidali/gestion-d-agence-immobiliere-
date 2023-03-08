package pi.app.estatemarket.Services;

import com.stripe.exception.StripeException;
import pi.app.estatemarket.Entities.Payment;
import pi.app.estatemarket.dto.UserDTO;

public interface IServicePayment {
    public Payment processPayment(UserDTO user, double amount, String paymentMethod) throws StripeException;


    }
