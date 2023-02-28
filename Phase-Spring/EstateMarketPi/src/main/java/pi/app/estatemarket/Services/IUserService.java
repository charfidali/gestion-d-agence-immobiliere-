package pi.app.estatemarket.Services;

import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.dto.UserDTO;
import pi.app.estatemarket.dto.UserRequest;

import java.util.List;


public interface IUserService {
    List<UserDTO> getAllUsers();

<<<<<<< HEAD
    User createUser(UserRequest userRequest);

    User updateUser( UserRequest userRequest);
=======
    UserApp createUser(UserRequest userRequest);

    UserApp updateUser(UserRequest userRequest);
>>>>>>> sami

    void deleteUser(long id);
    UserDTO getUserById(long id);
}


