package luccaschmitz.github.recipebackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import luccaschmitz.github.recipebackend.dto.RecipeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import luccaschmitz.github.recipebackend.dto.RecipeResponse;
import luccaschmitz.github.recipebackend.model.Recipe;
import luccaschmitz.github.recipebackend.repository.RecipeRepository;

@SpringBootTest
public class RecipeServiceTest {

  @Mock
  private RecipeRepository recipeRepository;

  @InjectMocks
  private RecipeService recipeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateRecipe() {
    RecipeRequest recipeRequest = new RecipeRequest();
    recipeRequest.setTitle("Test Recipe");
    recipeRequest.setDescription("Test Description");

    Recipe savedRecipe = new Recipe();
    savedRecipe.setId("1");
    savedRecipe.setTitle("Test Recipe");
    savedRecipe.setDescription("Test Description");

    when(recipeRepository.save(any())).thenReturn(savedRecipe);

    RecipeResponse result = recipeService.createRecipe(recipeRequest);

    assertNotNull(result);
    assertEquals(savedRecipe.getId(), result.getId());
    assertEquals(savedRecipe.getTitle(), result.getTitle());
    assertEquals(savedRecipe.getDescription(), result.getDescription());
  }

  @Test
  void testGetRecipeById() {
    String recipeId = "1";
    Recipe recipe = new Recipe();
    recipe.setId(recipeId);
    recipe.setTitle("Test Recipe");
    recipe.setDescription("Test Description");

    when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

    RecipeResponse result = recipeService.getRecipeById(recipeId);

    assertNotNull(result);
    assertEquals(recipe.getId(), result.getId());
    assertEquals(recipe.getTitle(), result.getTitle());
    assertEquals(recipe.getDescription(), result.getDescription());
  }

  @Test
  void testUpdateRecipe() {
    String recipeId = "1";
    RecipeRequest recipeRequest = new RecipeRequest();
    recipeRequest.setTitle("Updated Recipe");
    recipeRequest.setDescription("Updated Description");

    Recipe existingRecipe = new Recipe();
    existingRecipe.setId(recipeId);
    existingRecipe.setTitle("Test Recipe");
    existingRecipe.setDescription("Test Description");

    when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(existingRecipe));
    when(recipeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    RecipeResponse result = recipeService.updateRecipe(recipeId, recipeRequest);

    assertNotNull(result);
    assertEquals(recipeId, result.getId());
    assertEquals(recipeRequest.getTitle(), result.getTitle());
    assertEquals(recipeRequest.getDescription(), result.getDescription());
  }

  @Test
  void testDeleteRecipe() {
    String recipeId = "1";
    Recipe recipe = new Recipe();
    recipe.setId(recipeId);

    when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

    assertDoesNotThrow(() -> recipeService.deleteRecipe(recipeId));
  }

  @Test
  void testGetAllRecipes() {
    List<Recipe> recipes = new ArrayList<>();
    recipes.add(new Recipe());
    recipes.add(new Recipe());

    when(recipeRepository.findAll()).thenReturn(recipes);

    List<RecipeResponse> result = recipeService.getAllRecipes(null, null);

    assertNotNull(result);
    assertEquals(recipes.size(), result.size());
  }

  @Test
  void testGetAllRecipesWithSearchTerm() {
    String searchTerm = "Recipe";
    List<Recipe> recipes = new ArrayList<>();
    recipes.add(new Recipe());
    recipes.add(new Recipe());

    when(recipeRepository.findByTitleContainingIgnoreCase(searchTerm)).thenReturn(recipes);

    List<RecipeResponse> result = recipeService.getAllRecipes(searchTerm, null);

    assertNotNull(result);
    assertEquals(recipes.size(), result.size());
  }

  @Test
  void testGetAllRecipesWithSorting() {
    List<Recipe> recipes = new ArrayList<>();
    recipes.add(new Recipe());
    recipes.add(new Recipe());

    when(recipeRepository.findAll()).thenReturn(recipes);

    List<RecipeResponse> result = recipeService.getAllRecipes(null, "title");

    assertNotNull(result);
    assertEquals(recipes.size(), result.size());
  }

  @Test
  void testGetAllRecipesWithInvalidSorting() {
    List<Recipe> recipes = new ArrayList<>();
    recipes.add(new Recipe());
    recipes.add(new Recipe());

    when(recipeRepository.findAll()).thenReturn(recipes);

    List<RecipeResponse> result = recipeService.getAllRecipes(null, "invalidSort");

    assertNotNull(result);
    assertEquals(recipes.size(), result.size());
  }
}
