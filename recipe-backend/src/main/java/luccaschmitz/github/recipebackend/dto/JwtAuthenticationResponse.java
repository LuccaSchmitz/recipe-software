package luccaschmitz.github.recipebackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private String userId;

  public JwtAuthenticationResponse(String accessToken, String id) {
    this.accessToken = accessToken;
    this.userId = id;
  }
}

