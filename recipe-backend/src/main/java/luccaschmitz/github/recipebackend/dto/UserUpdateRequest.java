package luccaschmitz.github.recipebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
  private String id;
  private String username;
  private String email;
  private String password;
  private String photoUrl;
}
