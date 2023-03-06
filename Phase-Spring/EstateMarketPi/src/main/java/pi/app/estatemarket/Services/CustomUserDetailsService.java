package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pi.app.estatemarket.Entities.Role;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Entities.UserPage;
import pi.app.estatemarket.Entities.UserSearchCriteria;
import pi.app.estatemarket.Repository.RoleRepository;
import pi.app.estatemarket.Repository.UserCriteriaRepository;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.dto.UserDTO;
import pi.app.estatemarket.dto.UserRequest;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCriteriaRepository userCriteriaRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private  JavaMailSender mailSender;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles = null;

        UserApp user = userRepository.findByUsername(username);
        if (user != null) {
            roles = Arrays.asList(new SimpleGrantedAuthority(user.getRole().getName()));
            return new User(user.getUsername(), user.getPassword(), roles);
        }
        throw new UsernameNotFoundException("User not found with the name " + username);
    }



    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("estateexchangepi@gmail.com", "Estate Exchange Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
    public boolean checkIfUserExist(String email) {
        return userRepository.findByEmailAddress(email) !=null ? true : false;
    }

    public void sendVerificationEmail(UserRequest user, String siteURL)    throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmailAddress();
        String fromAddress = "exchangeestatepi@gmail.com";
        String senderName = "Exchange Estate Support";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "ImmobiTec.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName()+" "+ user.getLastName());
        String verifyURL = siteURL + "verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }
    public boolean verify(String verificationCode) {
        UserApp user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }

    }

    public void updateResetPasswordToken(String token, String emailAddress) throws UsernameNotFoundException {
        UserApp user = userRepository.findByEmailAddress(emailAddress);

        if (user != null) {
            user.setResetPasswordToken(token);
            System.out.println(user.getUserID());
            userRepository.save(user);
            updateResetPasswordToken(user);
        } else {
            throw new UsernameNotFoundException("Could not find any user with the email " + emailAddress);
        }
    }

    public UserApp getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(UserApp user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
    public void updateResetPasswordToken(UserApp user) {
        if (user.getResetPasswordToken() != null) {
            executor.schedule(() -> {
                user.setResetPasswordToken(null);
                userRepository.save(user);
            }, 30, TimeUnit.SECONDS);
        }
    }


    public List<UserDTO> getAllUsers() {
        List<UserApp> userApps = userRepository.findAll();
        return userApps.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }


    public Page<UserApp> getUsers(UserPage userPage,
                                  UserSearchCriteria userSearchCriteria){
        return userCriteriaRepository.findAllWithFilters(userPage, userSearchCriteria);
    }



    public UserApp createUser(UserRequest userRequest) {
        Role role=roleRepository.findById(userRequest.getRole_id()).orElse(null);
        //log.info("{}",userRequest.getRole_id());
        log.info("roleid from role{}",role.getRoleId());
        //User user= new User();
        UserApp user=modelMapper.map(userRequest, UserApp.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(role);
        return userRepository.save(user);
    }


    public UserApp updateUser( UserRequest userRequest) {
        Role role = roleRepository.findById(userRequest.getRole_id()).orElse(null);
        UserApp user = userRepository.findById(userRequest.getUserID()).orElse(null);
        user = modelMapper.map(userRequest, UserApp.class);
        if (role != null)
            user.setRole(role);
        return userRepository.save(user);
    }


    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }


    public UserDTO getUserById(long id) {
        UserApp userApp =userRepository.findById(id).orElse(null);
        return modelMapper.map(userApp, UserDTO.class);
    }



}

