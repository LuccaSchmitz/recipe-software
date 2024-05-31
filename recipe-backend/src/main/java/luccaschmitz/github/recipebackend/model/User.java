package luccaschmitz.github.recipebackend.model;

import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
  @Id
  private String id;
  private String username;
  private String email;
  private String password;
  private String photoUrl;
  private List<String> createdRecipes;
  private List<String> likedRecipes;
  private List<String> favoritedRecipes;

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
