package pi.app.estatemarket.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pi.app.estatemarket.Entities.UserApp;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserRepository extends JpaRepository<UserApp,Long> {
    UserApp findByUsername(String username);
}
