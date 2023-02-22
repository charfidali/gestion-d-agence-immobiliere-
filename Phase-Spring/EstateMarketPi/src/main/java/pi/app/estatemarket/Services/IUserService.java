package pi.app.estatemarket.Services;

import pi.app.estatemarket.Entities.User;
import pi.app.estatemarket.dto.UserDTO;
import pi.app.estatemarket.dto.UserRequest;

import java.util.List;


public interface IUserService {
    List<UserDTO> getAllUsers();

    User createUser(UserRequest userRequest);

    User updateUser( UserRequest userRequest);

    void deleteUser(long id);
    UserDTO getUserById(long id);
}


