package luccaschmitz.github.recipebackend.repository;

import java.util.List;
import luccaschmitz.github.recipebackend.model.Recipe;
import luccaschmitz.github.recipebackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
  List<Recipe> findByOwnerId(String ownerId);
  List<Recipe> findByTitleContainingIgnoreCase(String searchTerm);
}
