package pi.app.estatemarket.Controller;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Security.auth.AuthenticationRequest;
import pi.app.estatemarket.Security.auth.AuthenticationResponse;
import pi.app.estatemarket.Services.CustomUserDetailsService;
import pi.app.estatemarket.Security.JwtUtil;
import pi.app.estatemarket.dto.UserDTO;
import pi.app.estatemarket.dto.UserRequest;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    static String tokenn;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserRequest user) throws Exception {
        return ResponseEntity.ok(userDetailsService.createUser(user));
    }

    @PostMapping("/forgot_password")
    @ResponseBody
    public String processForgotPassword(@RequestBody String emailAddress, Model model) {
        //String email = emailAddress.//emailAddress = "samibenfadhl@gmail.com";
        String token = RandomString.make(30);
        try {
            System.out.println(emailAddress);
            userDetailsService.updateResetPasswordToken(token, emailAddress);
            String resetPasswordLink = "http://localhost:8085"+ "/reset_password?token=" + token;
            userDetailsService.sendEmail(emailAddress, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgot_password_form";
    }

    @PostMapping("/update_password")
    @ResponseBody
    public String processResetPassword( @RequestBody String password, Model model) {
        UserApp user = userDetailsService.getByResetPasswordToken(tokenn);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userDetailsService.updatePassword(user, password);
            model.addAttribute("suceess update", "You have successfully changed your password.");
        }
        return "message";
    }

    @GetMapping("/reset_password")
    public String showResetPassword(@Param(value = "token") String token, Model model) {

        UserApp user = userDetailsService.getByResetPasswordToken(token);

        model.addAttribute("token", token);
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "Invalid token";
        }
        this.tokenn=token;

        return "Success";
    }
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {

        if (userDetailsService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }
}


