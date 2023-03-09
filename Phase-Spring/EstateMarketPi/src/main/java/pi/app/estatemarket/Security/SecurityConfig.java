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
            "/affecterUserAppointment/{IdAppointment}/{userID}",
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

            "/addAndAffectPublicationTouser/**",


/*            "/RetrieveAllPublications",
            "/UpdatePublication/**",
            "/RetrievePublication/**",
            "/DeletePublication/**",
            "/addAndAffectPublicationTouser/**",
            "/countCommentsByPublication/{idPublication}/**",
            "/Afficher le nombre de commentaire par publication/**",
            "/Afficher tous les commentaire d'une publication/**",
            "/addlike/**",
            "/RetrieveAllComments",
            "/UpdateComment/**",
            "/retrieveComment/**",
            "/DeleteComment/**",
            "/ajouterEtAffecterCommentaireAUserEtCommentaire/**",
            "/reportComment/**",
            "/PinComment/**",
            "/Disable comments/**",*/


            "/api/contract/**",

            "/pdf",
            "/api/chatwork",

            "/PinComment/**",
            "/Disable comments/**",
            "/afficherAppointments","/supAppointment/{id}","/updateAppointment/{id}","/  available-dates/{userId}/{userId2}",
            "/checkAvailability/{userId1}/{userId2}","/RDV/{userId1}/{userId2}",
            "/nombrederendezvous/{id}","/appointmentStatistics" , "/addAppointment"



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
<<<<<<< HEAD
                .authorizeRequests().antMatchers("/api/role/**","/api/user/**","/api/contract/**","/pdf","/addAppointment").hasRole("ADMIN")
=======
                .authorizeRequests().antMatchers("/api/role/**","/api/user/**","/api/contract/**","/pdf").hasRole("ADMIN")

                .antMatchers("/api/user/**","/logout","/api/payment/**").hasRole("USER")
                .antMatchers(            "/RetrieveAllPublications",
                        "/UpdatePublication/**",
                        "/RetrievePublication/**",
                        "/DeletePublication/**",
                        "/addAndAffectPublicationTouser/**",
                        "/countCommentsByPublication/{idPublication}/**",
                        "/Afficher le nombre de commentaire par publication/**",
                        "/Afficher tous les commentaire d'une publication/**",
                        "/addlike/**",
                        "/RetrieveAllComments",
                        "/UpdateComment/**",
                        "/RetrieveComment/**",
                        "/DeleteComment/**",
                        "/ajouterEtAffecterCommentaireAUserEtCommentaire/**",
                        "/reportComment/**",
                        "/PinComment/**",
                        "/Disable comments/**").hasAnyRole("USER","ADMIN","MANAGER","CHEFAGENCE")


                // .antMatchers("/api/chatwork").hasAnyRole("USER","MANAGER")
>>>>>>> 8f3f596ca9609150e8763c0d5369cb363f522256
                .antMatchers("/api/user/**","/api/payment/**").hasRole("USER")
                .antMatchers( ).hasAnyRole("USER","ADMIN")
                //.antMatchers("/api/chatwork").hasAnyRole("USER","MANAGER")
                .antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .and().logout()
                .logoutUrl("/logout")//.logoutSuccessUrl("/login")//.addLogoutHandler(logoutHandler)
                .deleteCookies("auth_code", "JSESSIONID").invalidateHttpSession(true)
                //.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                .and().oauth2Login()
                .defaultSuccessUrl("/googleAuth");



        // .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).

        http.addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(jwtAuthenticationEntryPoint);
    }

}