package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Services.IUserService;
import pi.app.estatemarket.dto.UserDTO;
import pi.app.estatemarket.dto.UserRequest;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Transactional
public class UserController {
    @Autowired
    private final IUserService userService;
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("add")
    @ResponseStatus(HttpStatus.CREATED)

    public UserApp createUser(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);}

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("update")
    public UserApp updateUser( @RequestBody UserRequest userRequest){
        return userService.updateUser(userRequest);
    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("delete/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
    @GetMapping("getbyid/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}