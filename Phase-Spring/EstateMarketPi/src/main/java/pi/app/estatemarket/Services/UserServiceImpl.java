package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Entities.Role;


import pi.app.estatemarket.Repository.RoleRepository;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.dto.UserDTO;
import pi.app.estatemarket.dto.UserRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserApp> userApps = userRepository.findAll();
        return userApps.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserApp createUser(UserRequest userRequest) {
        Role role=roleRepository.findById(userRequest.getRole_id()).orElse(null);
        //log.info("{}",userRequest.getRole_id());
        //log.info("roleid from role{}",role.getRoleId());
        //User user= new User();
        UserApp user=modelMapper.map(userRequest, UserApp.class);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public UserApp updateUser( UserRequest userRequest) {
        Role role = roleRepository.findById(userRequest.getRole_id()).orElse(null);
        UserApp user = userRepository.findById(userRequest.getUserID()).orElse(null);
        user = modelMapper.map(userRequest, UserApp.class);
        if (role != null)
            user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserById(long id) {
        UserApp userApp =userRepository.findById(id).orElse(null);
        return modelMapper.map(userApp, UserDTO.class);
    }
}
