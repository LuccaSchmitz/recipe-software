package luccaschmitz.github.recipebackend.service;

import luccaschmitz.github.recipebackend.dto.RecipeResponse;
import luccaschmitz.github.recipebackend.dto.UserResponse;
import luccaschmitz.github.recipebackend.dto.UserUpdateRequest;
import luccaschmitz.github.recipebackend.exception.ResourceNotFoundException;
import luccaschmitz.github.recipebackend.model.Recipe;
import luccaschmitz.github.recipebackend.model.User;
import luccaschmitz.github.recipebackend.repository.RecipeRepository;
import luccaschmitz.github.recipebackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RecipeRepository recipeRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  private User user;
  private Recipe recipe;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user = new User();
    user.setId("user1");
    user.setUsername("username");
    user.setEmail("user@example.com");
    user.setPassword("password");
    user.setPhotoUrl("photoUrl");

    recipe = new Recipe();
    recipe.setId("recipe1");
    recipe.setTitle("Recipe Title");
    recipe.setDescription("Recipe Description");
    recipe.setOwnerId("user1");
  }

  @Test
  void testGetUserById_Success() {
    when(userRepository.findById("user1")).thenReturn(Optional.of(user));

    UserResponse response = userService.getUserById("user1");

    assertNotNull(response);
    assertEquals("username", response.getUsername());
  }

  @Test
  void testGetUserById_UserNotFound() {
    when(userRepository.findById("user1")).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      userService.getUserById("user1");
    });
  }

  @Test
  void testUpdateUser_Success() {
    UserUpdateRequest request = new UserUpdateRequest();
    request.setId("user1");
    request.setUsername("newUsername");
    request.setEmail("new@example.com");
    request.setPhotoUrl("newPhotoUrl");
    request.setPassword("newPassword");

    when(userRepository.findById("user1")).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserResponse response = userService.updateUser("user1", request);

    assertNotNull(response);
    assertEquals("newUsername", response.getUsername());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testUpdateUser_Unauthorized() {
    UserUpdateRequest request = new UserUpdateRequest();
    request.setId("user2");
    request.setUsername("newUsername");
    request.setEmail("new@example.com");
    request.setPhotoUrl("newPhotoUrl");
    request.setPassword("newPassword");

    assertThrows(AuthorizationServiceException.class, () -> {
      userService.updateUser("user1", request);
    });
  }

  @Test
  void testUpdateUser_UserNotFound() {
    UserUpdateRequest request = new UserUpdateRequest();
    request.setId("user1");
    request.setUsername("newUsername");
    request.setEmail("new@example.com");
    request.setPhotoUrl("newPhotoUrl");
    request.setPassword("newPassword");

    when(userRepository.findById("user1")).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      userService.updateUser("user1", request);
    });
  }

  @Test
  void testGetUserRecipes_Success() {
    when(userRepository.findById("user1")).thenReturn(Optional.of(user));
    when(recipeRepository.findByOwnerId("user1")).thenReturn(Collections.singletonList(recipe));

    List<RecipeResponse> recipes = userService.getUserRecipes("user1");

    assertNotNull(recipes);
    assertEquals(1, recipes.size());
    assertEquals("Recipe Title", recipes.get(0).getTitle());
  }

  @Test
  void testGetUserRecipes_UserNotFound() {
    when(userRepository.findById("user1")).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> {
      userService.getUserRecipes("user1");
    });
  }
}
