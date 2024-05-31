package luccaschmitz.github.recipebackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenBlackListServiceTest {

  private TokenBlackListService tokenBlackListService;

  @BeforeEach
  public void setUp() {
    tokenBlackListService = new TokenBlackListService();
  }

  @Test
  public void testAddToken() {
    String token = "test-token";
    tokenBlackListService.addToken(token);
    assertTrue(tokenBlackListService.isTokenBlacklisted(token));
  }

  @Test
  public void testIsTokenBlacklisted() {
    String token = "test-token";
    assertFalse(tokenBlackListService.isTokenBlacklisted(token));
    tokenBlackListService.addToken(token);
    assertTrue(tokenBlackListService.isTokenBlacklisted(token));
  }

  @Test
  public void testTokenNotBlacklistedAfterCreation() {
    String token = "another-test-token";
    assertFalse(tokenBlackListService.isTokenBlacklisted(token));
  }
}
