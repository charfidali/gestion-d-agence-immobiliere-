    package pi.app.estatemarket.Controller;

    import com.nimbusds.openid.connect.sdk.LogoutRequest;
    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import lombok.AllArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import net.bytebuddy.utility.RandomString;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Sort;
    import org.springframework.data.repository.query.Param;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.DisabledException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
    import org.springframework.ui.Model;
    import org.springframework.util.StringUtils;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.view.RedirectView;
    import pi.app.estatemarket.Entities.Role;
    import pi.app.estatemarket.Entities.UserApp;
    import pi.app.estatemarket.Entities.UserPage;
    import pi.app.estatemarket.Entities.UserSearchCriteria;
    import pi.app.estatemarket.Repository.RoleRepository;
    import pi.app.estatemarket.Repository.UserRepository;
    import pi.app.estatemarket.Security.CustomJwtAuthenticationFilter;
    import pi.app.estatemarket.Security.auth.AuthenticationRequest;
    import pi.app.estatemarket.Security.auth.AuthenticationResponse;
    import pi.app.estatemarket.Services.*;
    import pi.app.estatemarket.Security.JwtUtil;
    import pi.app.estatemarket.dto.UserDTO;
    import pi.app.estatemarket.dto.UserRequest;
    import javax.mail.MessagingException;
    import javax.mail.Session;
    import javax.servlet.http.Cookie;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import javax.transaction.Transactional;
    import java.io.IOException;
    import java.io.UnsupportedEncodingException;
    import java.time.Instant;
    import java.util.Arrays;
    import java.util.Date;
    import java.util.List;


    @RestController
    @AllArgsConstructor
    @Slf4j
    //@RequestMapping("/api/user")
    @Transactional
    public class AuthenticationController {

        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private CustomUserDetailsService userDetailsService;
        @Autowired
        private UserServiceImpl userService;
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RoleRepository roleRepository;
        @Autowired
        private JwtUtil jwtTokenUtil;
        @Autowired
        private JwtBlacklist jwtBlacklist;

        @Autowired
        private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;


        static String tokenn;

        //static String token;


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
            tokenn = jwtTokenUtil.generateToken(userDetails);

            UserApp user = userRepository.findByUsername(userDetails.getUsername());
            SessionService.setEmailAddress(user.getEmailAddress());
            SessionService.setUserID(user.getUserID());
            SessionService.setFirstName(user.getFirstName());
            SessionService.setLastName(user.getLastName());
            SessionService.setPassword(user.getPassword());

            return ResponseEntity.ok(new AuthenticationResponse(tokenn));
        }


        @RequestMapping(value = "/register", method = RequestMethod.POST)
        public ResponseEntity<?> saveUser(@RequestBody UserRequest user) throws Exception {


            if (userDetailsService.checkIfUserExist(user.getEmailAddress())) {
                return null;//  throw new Exception("User already exists for this email") ;
            } else {
                String randomCode = RandomString.make(64);
                user.setVerificationCode(randomCode);
                user.setEnabled(false);
                userService.createUser(user);
                userDetailsService.sendVerificationEmail(user, "http://localhost:8085");
                return ResponseEntity.ok(userService.createUser(user));
            }
        }

        @PostMapping("/forgot_password")
        @ResponseBody
        public String processForgotPassword(@RequestBody String emailAddress, Model model) {
            //String email = emailAddress.//emailAddress = "samibenfadhl@gmail.com";
            String token = RandomString.make(30);
            try {
                System.out.println(emailAddress);
                userDetailsService.updateResetPasswordToken(token, emailAddress);
                String resetPasswordLink = "http://localhost:8085" + "/reset_password?token=" + token;
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
        public String processResetPassword(@RequestBody String password, Model model) {
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
            this.tokenn = token;

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

        @GetMapping("/googleAuth")
        public ResponseEntity<?> currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            System.out.println("SHALOM");
            String email = oAuth2AuthenticationToken.getPrincipal().getAttribute("email");
            UserApp user = userRepository.findByEmailAddress(email);
            if (user == null) {
                user = new UserApp();
                Role role = roleRepository.findRoleByName("ROLE_USER");
                user.setEmailAddress(oAuth2AuthenticationToken.getPrincipal().getAttribute("email"));
                user.setRole(role);
                user.setUsername(oAuth2AuthenticationToken.getPrincipal().getAttribute("name"));
                user.setFirstName(oAuth2AuthenticationToken.getPrincipal().getAttribute("name"));
                user.setLastName(oAuth2AuthenticationToken.getPrincipal().getAttribute("family_name"));
                user.setProfilePicture(oAuth2AuthenticationToken.getPrincipal().getAttribute("picture"));
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode("test");
                user.setPassword(encodedPassword);
                user.setEnabled(true);
                userRepository.save(user);
            }

            user.setEnabled(true);
            SessionService.setEmailAddress(user.getEmailAddress());
            SessionService.setUserID(user.getUserID());
            SessionService.setFirstName(user.getFirstName());
            SessionService.setLastName(user.getLastName());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), "test"));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            tokenn = jwtTokenUtil.generateToken(userDetails);
            log.info("fel AUTH {}", tokenn);
            return ResponseEntity.ok(new AuthenticationResponse(tokenn));
        }

        @GetMapping("/jwt/logout")
        public RedirectView logout() {
            log.info("fel logout {}", tokenn);
            jwtBlacklist.add(tokenn);
            return new RedirectView("http://localhost:8085/login");
        }
        public static void clearCookie(HttpServletResponse response) {
            Cookie cookie = new Cookie("myCookie", "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        public static void invalidateToken() {
           /*Claims claim=Jwts.parser()
                    .setSigningKey("exchangemarket")
                    .parseClaimsJws(CustomJwtAuthenticationFilter.jwtToken)
                    .getBody();
            claim.setExpiration(Date.from(Instant.now().minusSeconds(1)));
            CustomJwtAuthenticationFilter.jwtToken = Jwts.builder()
                    .setClaims(claim)
                    .signWith(SignatureAlgorithm.HS256, "exchangemarket")
                    .compact();

            CustomJwtAuthenticationFilter.userDetails = new User(jwtTokenUtil.getUsernameFromToken(CustomJwtAuthenticationFilter.jwtToken), "",
                    jwtTokenUtil.getRolesFromToken(CustomJwtAuthenticationFilter.jwtToken));
            CustomJwtAuthenticationFilter.usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(CustomJwtAuthenticationFilter.userDetails,null,CustomJwtAuthenticationFilter.userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(CustomJwtAuthenticationFilter.usernamePasswordAuthenticationToken);
            */

        }
        @GetMapping("/refreshToken")
        public static  void refreshToken(HttpServletResponse response) throws IOException {
            clearCookie(response);
            invalidateToken();
     /*
            // Parse the existing token and extract its claims
            Claims claims = Jwts.parser()
                    .setSigningKey("exchangemarket")
                    .parseClaimsJws(token)
                    .getBody();

            // Modify the expiration date to 1 hour from now


            claims.setExpiration(Date.from(Instant.now().minusSeconds(1)));

            // Build a new token with the updated claims
            CustomJwtAuthenticationFilter.jwtToken = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS256, "exchangemarket")
                    .compact();

            //  userDetails = userDetailsService.loadUserByUsername(null);
            ;

            if (StringUtils.hasText(CustomJwtAuthenticationFilter.jwtToken) ) {
                userDetails = new User("dead", "",
                        jwtTokenUtil.getRolesFromToken(CustomJwtAuthenticationFilter.jwtToken));
                // Return the new token

            }



            return CustomJwtAuthenticationFilter.jwtToken;

     */
        }


            public boolean isBlacklisted (String token){
                return jwtBlacklist.contains(token);
            }

        @GetMapping("/getAllUsersFilter")
        public ResponseEntity<Page<UserApp>> getAllUsers(
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size,
                @RequestParam(name = "sort", defaultValue = "userID") String sortField,
                @RequestParam(name = "direction", defaultValue = "asc") String direction,
                @RequestParam(name = "firstName") String name,
                @RequestParam(name = "lastName") String lastname,
                @RequestParam(name = "emailAddress") String email
        ){
            UserPage userPage= new UserPage();
            UserSearchCriteria userSearchCriteria = new UserSearchCriteria();
            userPage.setPageNumber(page);userPage.setPageSize(size);
            userPage.setSortBy(sortField);userPage.setSortDirection(Sort.Direction.fromString(direction));
            userSearchCriteria.setFirstName(name);
            userSearchCriteria.setLastName(lastname);
            userSearchCriteria.setEmailAddress(email);
            return new ResponseEntity<>(userDetailsService.getUsers(userPage, userSearchCriteria),
                    HttpStatus.OK);
        }


            @GetMapping("/getAllUsers")
            public List<UserDTO> getAllUsers () throws UserAppNotFoundException {
                log.info("fel GETZZ {}", tokenn);

                if (isBlacklisted(tokenn)) {
                   throw new UserAppNotFoundException("Invalid Credential");
                }
                return userDetailsService.getAllUsers();
            }


            @PostMapping("add")
            @ResponseStatus(HttpStatus.CREATED)

            public UserApp createUser (@RequestBody UserRequest userRequest) throws UserAppNotFoundException {
                if (isBlacklisted(tokenn)) {
                    throw new UserAppNotFoundException("Invalid Credential");
                }
                return userDetailsService.createUser(userRequest);
            }

            @ResponseStatus(HttpStatus.OK)
            @PutMapping("update")
            public UserApp updateUser (@RequestBody UserRequest userRequest) throws UserAppNotFoundException {
                if (isBlacklisted(tokenn)) {
                    throw new UserAppNotFoundException("Invalid Credential");
                }
                return userDetailsService.updateUser(userRequest);
            }
            @ResponseStatus(HttpStatus.OK)
            @DeleteMapping("delete/{id}")
            public void deleteUser ( @PathVariable long id) throws UserAppNotFoundException {
                if (!isBlacklisted(tokenn))
                    userDetailsService.deleteUser(id);
                else
                    throw new UserAppNotFoundException("Invalid Credential");
            }
            @GetMapping("getbyid/{id}")
            @ResponseStatus(HttpStatus.FOUND)
            public UserDTO getUserById (@PathVariable Long id) throws UserAppNotFoundException {
                if (isBlacklisted(tokenn))
                    throw new UserAppNotFoundException("Invalid Credential");
                return userDetailsService.getUserById(id);
            }
        }







