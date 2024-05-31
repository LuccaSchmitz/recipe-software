package luccaschmitz.github.recipebackend.service;

import java.util.stream.Collectors;
import luccaschmitz.github.recipebackend.dto.RecipeResponse;
import luccaschmitz.github.recipebackend.dto.UserResponse;
import luccaschmitz.github.recipebackend.dto.UserUpdateRequest;
import luccaschmitz.github.recipebackend.exception.ResourceNotFoundException;
import luccaschmitz.github.recipebackend.model.Recipe;
import luccaschmitz.github.recipebackend.model.User;
import luccaschmitz.github.recipebackend.repository.RecipeRepository;
import luccaschmitz.github.recipebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RecipeRepository recipeRepository;

  @Autowired
  PasswordEncoder passwordEncoder;


  public UserResponse getUserById(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    return new UserResponse(user);
  }

  public UserResponse updateUser(String userId, UserUpdateRequest userUpdateRequest) {
    if (!userId.equals(userUpdateRequest.getId())) {
      throw new AuthorizationServiceException("User ID different from user requested ID");
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    user.setUsername(userUpdateRequest.getUsername());
    user.setEmail(userUpdateRequest.getEmail());

    if (userUpdateRequest.getPhotoUrl() != null){
      user.setPhotoUrl(userUpdateRequest.getPhotoUrl());
    }

    if (userUpdateRequest.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
    }

    userRepository.save(user);
    return new UserResponse(user);
  }

  public List<RecipeResponse> getUserRecipes(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    List<Recipe> recipes = recipeRepository.findByOwnerId(user.getId());
    return recipes.stream().map(RecipeResponse::new).collect(Collectors.toList());
  }
}

