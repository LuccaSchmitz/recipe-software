package luccaschmitz.github.recipebackend.controller;

import luccaschmitz.github.recipebackend.dto.RecipeResponse;
import luccaschmitz.github.recipebackend.dto.UserResponse;
import luccaschmitz.github.recipebackend.dto.UserUpdateRequest;
import luccaschmitz.github.recipebackend.model.Recipe;
import luccaschmitz.github.recipebackend.model.User;
import luccaschmitz.github.recipebackend.security.JwtAuthenticationFilter;
import luccaschmitz.github.recipebackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testGetUserById() throws Exception {
    User user = new User("john", "john@john", "123");
    user.setId("1");

    UserResponse userResponse = new UserResponse(user);
    Mockito.when(userService.getUserById(anyString())).thenReturn(userResponse);

    mockMvc.perform(get("/api/users/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void testGetUserRecipes() throws Exception {
    Recipe recipeGenerated1 = new Recipe();
    Recipe recipeGenerated2 = new Recipe();

    recipeGenerated1.setTitle("Cake");
    recipeGenerated2.setTitle("Banana");

    RecipeResponse recipe1 = new RecipeResponse(recipeGenerated1);
    RecipeResponse recipe2 = new RecipeResponse(recipeGenerated2);
    List<RecipeResponse> recipes = Arrays.asList(recipe1, recipe2);
    Mockito.when(userService.getUserRecipes(anyString())).thenReturn(recipes);

    mockMvc.perform(get("/api/users/1/recipes"))
        .andExpect(status().isOk());
  }
}

