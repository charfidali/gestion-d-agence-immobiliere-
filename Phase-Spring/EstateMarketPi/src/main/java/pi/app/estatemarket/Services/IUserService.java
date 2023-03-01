package pi.app.estatemarket.Services;

import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.dto.UserDTO;

import java.util.List;


public interface IUserService {
    List<UserDTO> getAllUsers();

    UserApp createUser(UserApp user);

    UserApp updateUser(UserApp user);

    void deleteUser(long id);
    UserDTO getUserById(long id);
}


