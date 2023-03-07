package pi.app.estatemarket.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pi.app.estatemarket.Services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private LogoutService logoutHandler;


    @Autowired
    private AuthenticationFailureHandler authFail;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private static final String[] AUTH_WHITELIST = {
            "/forgot_password","/reset_password","/update_password","/","/verify","/login",
            "/googleAuth",
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/authenticate",
            "/register",


            "/RetrieveAllPublications",
            "/UpdatePublication/**",
            "/RetrievePublication/**",
            "/DeletePublication/**",
            "/addAndAffectPublicationTouser/**",
            "/Afficher le nombre de commentaire par publication/**",
            "/Afficher tous les commentaire d'une publication/**",
            "/addlike/**",


            "/RetrieveAllComments",
            "/UpdateComment/**",
            "/retrieveComment/**",
            "/DeleteComment/**",
            "/ajouterEtAffecterCommentaireAUserEtCommentaire/**",
            "/reportComment/**",
            "/api/contract/**",
            "/pdf",
            "/api/payment/**",
            "/api/chatwork",
            "/PinComment/**",
            "/Disable comments/**",

            "/api/chatwork",

            "/chat"


            // other public endpoints of your API may be appended to this array
    };

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests().antMatchers("/api/role/**","/api/user/**","/api/contract/**","/pdf").hasRole("ADMIN")
                .antMatchers("/api/user/**","/logout","/api/payment/**").hasRole("USER")

               // .antMatchers("/api/chatwork").hasAnyRole("USER","MANAGER")
                .antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated()


                .and().formLogin().loginPage("/login")
                .and().logout()
                .logoutUrl("/logout").logoutSuccessUrl("/login").addLogoutHandler(logoutHandler)
                .deleteCookies("auth_code", "JSESSIONID").invalidateHttpSession(true)
                //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                .and().oauth2Login()
                .failureHandler(authFail)
                .defaultSuccessUrl("/googleAuth");
               //.authorizationEndpoint();


               // .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).

                http.addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                        .exceptionHandling()
                        .accessDeniedHandler(jwtAuthenticationEntryPoint);
    }

}