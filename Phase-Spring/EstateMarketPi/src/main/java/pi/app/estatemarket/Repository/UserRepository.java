package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pi.app.estatemarket.Entities.UserApp;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserApp,Long> {
    public UserApp findByUsername(String username);
    public UserApp findByResetPasswordToken(String token);
    @Query("SELECT u FROM UserApp u WHERE u.emailAddress = ?1")
    UserApp findByEmailAddress (String emailAddress);
    @Query("SELECT u FROM UserApp u WHERE u.verificationCode = ?1")
    public UserApp findByVerificationCode(String code);

}
