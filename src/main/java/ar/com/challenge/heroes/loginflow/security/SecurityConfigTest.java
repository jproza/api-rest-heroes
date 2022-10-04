package ar.com.challenge.heroes.loginflow.security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("test")
@EnableWebSecurity()
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class SecurityConfigTest extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web)  {
        web.ignoring().antMatchers("/v2/api-docs", "/v3/api-docs","/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**","/api/test/**","/api/auth/**","/swagger-ui-heroes.html","/api/heroes/**","/api/heroes**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

                http.authorizeRequests()
                //.antMatchers("/api-docs/**", "/swagger-resources/**", "/v2/api-docs", "/**/favicon.ico", "/webjars/**", "/api/admin/health","/api/heroes/**","/api/heroes**").permitAll()
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .headers().disable()
                .httpBasic();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}