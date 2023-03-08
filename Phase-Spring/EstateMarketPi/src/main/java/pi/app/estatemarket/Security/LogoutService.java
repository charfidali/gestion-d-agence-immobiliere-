package pi.app.estatemarket.Security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pi.app.estatemarket.Controller.AuthenticationController;
import pi.app.estatemarket.Services.SessionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

import static pi.app.estatemarket.Security.CustomJwtAuthenticationFilter.jwtToken;
import static pi.app.estatemarket.Security.CustomJwtAuthenticationFilter.userDetails;


@Service
@Slf4j
public class LogoutService implements LogoutHandler {


    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return;
        }
        log.info("CCCC {}", CustomJwtAuthenticationFilter.jwtToken);
        Claims claim = Jwts.parser()
                .setSigningKey("exchangemarket")
                .parseClaimsJws(CustomJwtAuthenticationFilter.jwtToken)
                .getBody();
        claim.setExpiration(Date.from(Instant.now().plusSeconds(100000)));
        try {
            AuthenticationController.refreshToken(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Claims claims = Jwts.parser()
                .setSigningKey("exchangemarket")
                .parseClaimsJws(jwtToken)
                .getBody();

        claims.setExpiration(Date.from(Instant.now().minusSeconds(1)));
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (StringUtils.hasText(CustomJwtAuthenticationFilter.jwtToken)) {
             userDetails = new User("dead", "",
                    roles);

            SecurityContextHolder.clearContext();
            SessionService.setUserID(0);
            SessionService.setEmailAddress(null);
            SessionService.setFirstName(null);
            SessionService.setLastName(null);
            SessionService.setPassword(null);
        }

    }
}