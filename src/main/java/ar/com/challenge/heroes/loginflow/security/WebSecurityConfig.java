package ar.com.challenge.heroes.loginflow.security;


import ar.com.challenge.heroes.loginflow.security.jwt.AuthEntryPointJwt;
import ar.com.challenge.heroes.loginflow.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("prod")
@EnableWebSecurity
/*@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)*/
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
  
  @Value("${spring.h2.console.path}")
  private String h2ConsolePath;
  
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

 /* @Override
  protected void configure(AuthenticationManagerBuilder auth)
          throws Exception
  {
    auth.inMemoryAuthentication()
            .passwordEncoder(passwordEncoder())
            .withUser("jproza")
            .password(passwordEncoder().encode("jproza"))
            .roles("ADMIN");
  }*/
  
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }

//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web)  {
    web.ignoring().antMatchers("/v2/api-docs", "/v3/api-docs","/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**","/api/test/**",h2ConsolePath + "/**","/api/auth**","/swagger-ui-heroes.html");

    }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
  http.authorizeRequests()
                  .antMatchers("/user").hasAnyRole()
            //.hasAnyRole("USER", "ADMIN")
            .antMatchers("/admin").hasAnyRole()
            //.hasRole("ADMIN")
            .antMatchers("/")
          .hasAnyRole().antMatchers("/swagger-ui/**")
          .permitAll()

            .and()
            .formLogin()
            .permitAll()
            .loginPage("/signin")
            .and()
            .logout()
            .logoutRequestMatcher(
                    new AntPathRequestMatcher("/api/auth/logout"))
            .permitAll()
          .and().authorizeRequests().anyRequest().authenticated();


    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

   /* http.cors().and().csrf().disable()
      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
      .antMatchers("/api/test/**").permitAll()
      .antMatchers(h2ConsolePath + "/**").permitAll()
            .antMatchers("/swagger-ui-heroes.html" +"/**").permitAll()
            .antMatchers("/swagger-ui/index.html"+"/**").permitAll()
            .and().authorizeRequests().anyRequest().authenticated();

      http.headers().frameOptions().sameOrigin();

      http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);*/
  }
  
/*  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests().antMatchers("/api/auth/**").permitAll()
        .antMatchers("/api/test/**").permitAll()
        .antMatchers(h2ConsolePath + "/**").permitAll()
        .antMatchers("/swagger-ui-heroes.html").permitAll()
        .anyRequest().authenticated();
    
 // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
    http.headers().frameOptions().sameOrigin();
    
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }*/
}
