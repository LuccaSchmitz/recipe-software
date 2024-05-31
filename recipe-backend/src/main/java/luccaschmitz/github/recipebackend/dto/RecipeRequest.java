package luccaschmitz.github.recipebackend.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeRequest {
  private String title;
  private String description;
  private List<String> ingredients;
  private List<String> steps;
  private String ownerId;
  private String photoUrl;
}
