package luccaschmitz.github.recipebackend.controller;

import luccaschmitz.github.recipebackend.dto.RecipeResponse;
import luccaschmitz.github.recipebackend.dto.UserResponse;
import luccaschmitz.github.recipebackend.dto.UserUpdateRequest;
import luccaschmitz.github.recipebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
    UserResponse userResponse = userService.getUserById(userId);
    return ResponseEntity.ok(userResponse);
  }

  @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest userUpdateRequest) {
    UserResponse updatedUser = userService.updateUser(userId, userUpdateRequest);
    return ResponseEntity.ok(updatedUser);
  }

  @GetMapping(value = "/{userId}/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<RecipeResponse>> getUserRecipes(@PathVariable String userId) {
    List<RecipeResponse> userRecipes = userService.getUserRecipes(userId);
    return ResponseEntity.ok(userRecipes);
  }
}