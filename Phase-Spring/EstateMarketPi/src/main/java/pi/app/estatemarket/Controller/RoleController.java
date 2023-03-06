package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.Role;
import pi.app.estatemarket.Services.IRoleService;
import pi.app.estatemarket.Services.JwtBlacklist;
import pi.app.estatemarket.Services.UserAppNotFoundException;
import pi.app.estatemarket.dto.RoleDTO;

import javax.transaction.Transactional;
import java.util.List;

import static pi.app.estatemarket.Controller.AuthenticationController.tokenn;

@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
@Transactional
public class RoleController {
    @Autowired
    private final IRoleService roleService;

    @Autowired
    private JwtBlacklist jwtBlacklist;


    public boolean isBlacklisted (String token){
        return jwtBlacklist.contains(token);
    }
    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }
    @PostMapping("add")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRole(@RequestBody Role role) throws UserAppNotFoundException {
        if (isBlacklisted(tokenn)) {
            throw new UserAppNotFoundException("Invalid Credential");
        }
        else
            roleService.createRole(role);
    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("update")
    public void updateRole( @RequestBody Role role) throws UserAppNotFoundException {
        if (isBlacklisted(tokenn)) {
            throw new UserAppNotFoundException("Invalid Credential");
        }
        else
             roleService.updateRole(role);}

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("delete/{id}")
    public void deleteRole(@PathVariable long id) throws UserAppNotFoundException {
        if (isBlacklisted(tokenn)) {
            throw new UserAppNotFoundException("Invalid Credential");
        }
        else
            roleService.deleteRole(id);
    }
    @GetMapping("getbyid/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public RoleDTO getRoleById(@PathVariable Long id) throws UserAppNotFoundException {

        if (isBlacklisted(tokenn)) {
            throw new UserAppNotFoundException("Invalid Credential");
        }
        else
            return roleService.getRoleById(id);
    }
}
