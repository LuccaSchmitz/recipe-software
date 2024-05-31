package luccaschmitz.github.recipebackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recipes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
  @Id
  private String id;
  private String title;
  private String description;
  private String photoUrl;
  private List<String> steps;
  private List<String> ingredients;
  private List<String> comments;
  private List<String> likes;
  private String ownerId;
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}

