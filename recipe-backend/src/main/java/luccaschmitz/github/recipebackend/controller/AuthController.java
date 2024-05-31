package luccaschmitz.github.recipebackend.controller;

import java.util.Optional;
import luccaschmitz.github.recipebackend.dto.JwtAuthenticationResponse;
import luccaschmitz.github.recipebackend.dto.LoginRequest;
import luccaschmitz.github.recipebackend.dto.SignupRequest;
import luccaschmitz.github.recipebackend.model.User;
import luccaschmitz.github.recipebackend.repository.UserRepository;
import luccaschmitz.github.recipebackend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import luccaschmitz.github.recipebackend.service.TokenBlackListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtTokenProvider tokenProvider;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TokenBlackListService tokenBlackListService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      String jwt = tokenProvider.generateToken(authentication);
      Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

      if (user.isEmpty()) {
        return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user.get().getId()));
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Username or password wrong");
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
    if(userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body("Error: Username is already taken!");
    }

    if(userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body("Error: Email is already in use!");
    }

    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
        passwordEncoder.encode(signUpRequest.getPassword()));

    userRepository.save(user);

    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String token) {
    String jwt = token.substring(7); // Remove "Bearer " prefix
    tokenBlackListService.addToken(jwt);
    SecurityContextHolder.clearContext();
    return ResponseEntity.ok("User logged out successfully");
  }
}

