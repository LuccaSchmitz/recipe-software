package luccaschmitz.github.recipebackend.service;

import java.util.Comparator;
import luccaschmitz.github.recipebackend.dto.RecipeRequest;
import luccaschmitz.github.recipebackend.dto.RecipeResponse;
import luccaschmitz.github.recipebackend.exception.ResourceNotFoundException;
import luccaschmitz.github.recipebackend.model.Recipe;
import luccaschmitz.github.recipebackend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

  @Autowired
  private RecipeRepository recipeRepository;

  public RecipeResponse createRecipe(RecipeRequest recipeRequest) {
    Recipe recipe = new Recipe();
    recipe.setTitle(recipeRequest.getTitle());
    recipe.setDescription(recipeRequest.getDescription());
    recipe.setIngredients(recipeRequest.getIngredients());
    recipe.setSteps(recipeRequest.getSteps());
    recipe.setOwnerId(recipeRequest.getOwnerId());
    recipe.setPhotoUrl(recipeRequest.getPhotoUrl());

    Recipe savedRecipe = recipeRepository.save(recipe);
    return new RecipeResponse(savedRecipe);
  }

  public RecipeResponse getRecipeById(String recipeId) {
    Recipe recipe = recipeRepository.findById(recipeId)
        .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));
    return new RecipeResponse(recipe);
  }

  public RecipeResponse updateRecipe(String recipeId, RecipeRequest recipeRequest) {
    Recipe recipe = recipeRepository.findById(recipeId)
        .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

    recipe.setTitle(recipeRequest.getTitle());
    recipe.setDescription(recipeRequest.getDescription());
    recipe.setIngredients(recipeRequest.getIngredients());
    recipe.setSteps(recipeRequest.getSteps());
    recipe.setPhotoUrl(recipeRequest.getPhotoUrl());

    Recipe updatedRecipe = recipeRepository.save(recipe);
    return new RecipeResponse(updatedRecipe);
  }

  public void deleteRecipe(String recipeId) {
    Recipe recipe = recipeRepository.findById(recipeId)
        .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", recipeId));

    recipeRepository.delete(recipe);
  }

  public List<RecipeResponse> getAllRecipes(String searchTerm, String sortBy) {
    List<Recipe> recipes;

    if (searchTerm != null && !searchTerm.isEmpty()) {
      recipes = recipeRepository.findByTitleContainingIgnoreCase(searchTerm);
    } else {
      recipes = recipeRepository.findAll();
    }

    if (sortBy != null && !sortBy.isEmpty()) {
      switch (sortBy) {
        case "title":
          recipes.sort(Comparator.comparing(Recipe::getTitle, Comparator.nullsLast(Comparator.naturalOrder())));
          break;
        case "createdAt":
          recipes.sort(Comparator.comparing(Recipe::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())));
          break;
        default:
          // Não faz nada para o caso padrão
      }
    }

    return recipes.stream()
        .map(RecipeResponse::new)
        .collect(Collectors.toList());
  }
}

