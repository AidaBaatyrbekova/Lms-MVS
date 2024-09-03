package example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static example.model.enums.Role.INSTRUCTOR;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize -> {
            try {
                authorize.requestMatchers("/","user/add","user/save").permitAll()
                        .requestMatchers( "user/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/company/**").hasAuthority("ADMIN")
                        .requestMatchers( "/instructor/**").hasAuthority("ADMIN")
                        .requestMatchers( "/course/**").hasAuthority("ADMIN")
                        .requestMatchers("group/find-all","user/students").hasAnyAuthority("ADMIN","INSTRUCTOR")
                        .anyRequest().authenticated()
                        .and()
                        .formLogin(Customizer.withDefaults())
                        .httpBasic(Customizer.withDefaults());
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        return httpSecurity.build();
    }

}
