package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.dto.UserDTO;

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
    private final ModelMapper modelMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserApp> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserApp createUser(UserApp user) {
        return userRepository.save(user);
    }

    @Override
    public UserApp updateUser(UserApp user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserById(long id) {
        UserApp user=userRepository.findById(id).orElse(null);
        return modelMapper.map(user, UserDTO.class);
    }
}
