package luccaschmitz.github.recipebackend.service;

import luccaschmitz.github.recipebackend.model.User;
import luccaschmitz.github.recipebackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CustomUserDetailsService customUserDetailsService;

  private User user;

  @BeforeEach
  public void setUp() {
    user = new User();
    user.setUsername("testuser");
    user.setPassword("password");
  }

  @Test
  public void testLoadUserByUsername_UserExists() {
    Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

    assertNotNull(userDetails);
    assertEquals("testuser", userDetails.getUsername());
    assertEquals("password", userDetails.getPassword());
  }

  @Test
  public void testLoadUserByUsername_UserDoesNotExist() {
    Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
      customUserDetailsService.loadUserByUsername("nonexistentuser");
    });

    String expectedMessage = "User not found with username: nonexistentuser";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
