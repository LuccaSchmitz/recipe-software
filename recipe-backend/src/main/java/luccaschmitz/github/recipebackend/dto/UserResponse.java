package luccaschmitz.github.recipebackend.dto;

import lombok.Getter;
import lombok.Setter;
import luccaschmitz.github.recipebackend.model.User;

@Getter
@Setter
public class UserResponse {
  private String id;
  private String username;
  private String email;
  private String photoUrl;

  public UserResponse(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.photoUrl = user.getPhotoUrl();
  }
}
