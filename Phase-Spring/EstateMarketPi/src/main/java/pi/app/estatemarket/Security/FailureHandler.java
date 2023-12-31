package pi.app.estatemarket.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
@Component

public class FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;

        final Exception exc = (Exception) request.getAttribute("exception");

        if (exc != null) {

            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("cause", exception.toString()));
            response.getOutputStream().write(body);
        } else {
            if (exception.getCause() != null) {
                message = exception.getCause().toString() + " " + exception.getMessage();
            } else {
                message = exception.getMessage();
            }

            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));

            response.getOutputStream().write(body);
        }
    }
}
