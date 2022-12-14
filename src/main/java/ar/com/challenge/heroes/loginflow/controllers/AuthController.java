package ar.com.challenge.heroes.loginflow.controllers;


import ar.com.challenge.heroes.entities.Heroe;
import ar.com.challenge.heroes.logging.LogHeroes;
import ar.com.challenge.heroes.loginflow.models.ERole;
import ar.com.challenge.heroes.loginflow.models.Role;
import ar.com.challenge.heroes.loginflow.payload.request.LoginRequest;
import ar.com.challenge.heroes.loginflow.payload.response.UserInfoResponse;
import ar.com.challenge.heroes.loginflow.repository.UserRepository;
import ar.com.challenge.heroes.loginflow.models.User;
import ar.com.challenge.heroes.loginflow.payload.request.SignupRequest;
import ar.com.challenge.heroes.loginflow.payload.response.MessageResponse;
import ar.com.challenge.heroes.loginflow.repository.RoleRepository;
import ar.com.challenge.heroes.loginflow.security.jwt.JwtUtils;
import ar.com.challenge.heroes.loginflow.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @LogHeroes(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @PostMapping("/login")
  @Operation(summary = "Autenticar el usuario de la API")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Se autentic?? correctamente",
                  content = { @Content(mediaType = "application/json",
                          schema = @Schema(implementation = Heroe.class)) }),
          @ApiResponse(responseCode = "400", description = "Parametro incorrecto",
                  content = @Content),
          @ApiResponse(responseCode = "404", description = "Usuario inexistente o con error",
                  content = @Content) })
  public ResponseEntity<?> authenticateUser(@Valid  @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(),
                                   roles));
  }

  @LogHeroes(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @LogHeroes(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("Logout exitoso, para usar la api vuelva a iniciar session desde el login!"));
  }

  @LogHeroes(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
  @GetMapping("/signin")
  public String loginUser() { return "templates/signin.html"; }
}
