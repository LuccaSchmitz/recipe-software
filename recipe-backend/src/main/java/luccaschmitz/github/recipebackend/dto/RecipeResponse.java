package luccaschmitz.github.recipebackend.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import luccaschmitz.github.recipebackend.model.Recipe;

@Getter
@Setter
public class RecipeResponse {
  private String id;
  private String title;
  private String description;
  private List<String> ingredients;
  private List<String> steps;
  private String ownerId;
  private List<String> comments;
  private List<String> likes;
  private String photoUrl;

  public RecipeResponse(Recipe recipe) {
    this.id = recipe.getId();
    this.title = recipe.getTitle();
    this.description = recipe.getDescription();
    this.ingredients = recipe.getIngredients();
    this.steps = recipe.getSteps();
    this.ownerId = recipe.getOwnerId();
    this.comments = recipe.getComments();
    this.likes = recipe.getLikes();
    this.photoUrl = recipe.getPhotoUrl();
  }
}
