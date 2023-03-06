package pi.app.estatemarket.Security;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


    @Component
    public class JwtAuthenticationEntryPoint implements AccessDeniedHandler {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String message;

            if (accessDeniedException.getCause() != null) {
                message = accessDeniedException.getCause().toString() + " " + accessDeniedException.getMessage();
            } else {
                message = accessDeniedException.getMessage();
            }

            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));


            response.getOutputStream().write(body);
        }
    }
