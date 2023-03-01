package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pi.app.estatemarket.Entities.Role;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Repository.RoleRepository;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.dto.UserRequest;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    public UserApp save(UserRequest userRequest) {
        UserApp newUser = new UserApp();
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role role=new Role();
        role.setName(userRequest.getRole_name());
        role.setRoleId(userRequest.getRole_id());
        newUser.setRole(role);
        return userRepository.save(newUser);
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

}

