package pi.app.estatemarket.Controller;

import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.Payment;
import pi.app.estatemarket.Services.PaymentService;
import pi.app.estatemarket.Services.UserServiceImpl;
import pi.app.estatemarket.dto.UserDTO;

@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
public class PaymentController {


    private PaymentService paymentService;

    private UserServiceImpl userService;

    @PostMapping("/{userID}")
    public ResponseEntity<?> processPayment(@RequestBody Payment payment, @PathVariable Long userID) {
        UserDTO user = userService.getUserById(userID);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        try {
            Payment savedPayment = paymentService.processPayment(user, payment.getAmount(), payment.getPaymentMethod());
            return ResponseEntity.ok(savedPayment);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
