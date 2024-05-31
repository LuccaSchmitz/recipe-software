package luccaschmitz.github.recipebackend.controller;

import luccaschmitz.github.recipebackend.dto.RecipeRequest;
import luccaschmitz.github.recipebackend.dto.RecipeResponse;
import luccaschmitz.github.recipebackend.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

  @Autowired
  private RecipeService recipeService;

  @PostMapping
  public ResponseEntity<RecipeResponse> createRecipe(@RequestBody RecipeRequest recipeRequest) {
    RecipeResponse createdRecipe = recipeService.createRecipe(recipeRequest);
    return ResponseEntity.ok(createdRecipe);
  }

  @GetMapping("/{recipeId}")
  public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable String recipeId) {
    RecipeResponse recipeResponse = recipeService.getRecipeById(recipeId);
    return ResponseEntity.ok(recipeResponse);
  }

  @PutMapping("/{recipeId}")
  public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable String recipeId, @RequestBody RecipeRequest recipeRequest) {
    RecipeResponse updatedRecipe = recipeService.updateRecipe(recipeId, recipeRequest);
    return ResponseEntity.ok(updatedRecipe);
  }

  @DeleteMapping("/{recipeId}")
  public ResponseEntity<?> deleteRecipe(@PathVariable String recipeId) {
    recipeService.deleteRecipe(recipeId);
    return ResponseEntity.ok("Recipe deleted successfully");
  }

  @GetMapping
  public ResponseEntity<List<RecipeResponse>> getAllRecipes(
      @RequestParam(required = false) String searchTerm,
      @RequestParam(required = false) String sortBy
  ) {
    List<RecipeResponse> allRecipes = recipeService.getAllRecipes(searchTerm, sortBy);
    return ResponseEntity.ok(allRecipes);
  }
}
