package com.scand.bookshop.security.jwt;

import com.scand.bookshop.entity.Token;
import com.scand.bookshop.repository.TokenRepository;
import com.scand.bookshop.security.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

  private final TokenRepository tokenRepository;

  @Value("${scand.bookshop.jwtSecret}")
  private String jwtSecret;

  @Value("${scand.bookshop.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${scand.bookshop.refreshSecret}")
  private String refreshSecret;

  public String generateJwtToken(Authentication authentication) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(jwtSecret), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken(UserDetailsImpl userDetails) {
    Date issueDate = new Date();
    Date expirationDate = new Date(
        System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000); // 1 month
    String token = Jwts.builder()
        .setIssuedAt(issueDate)
        .setExpiration(expirationDate)
        .signWith(key(refreshSecret), SignatureAlgorithm.HS256)
        .compact();
    Token refreshToken = new Token(null, token, expirationDate, userDetails.getId());
    tokenRepository.save(refreshToken);
    return token;
  }

  private Key key(String secret) {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key(jwtSecret)).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean verifyRefreshToken(String token) {
    Optional<Token> refreshTokenOpt = tokenRepository.findByValue(token);
    if (refreshTokenOpt.isEmpty()) {
      return false;
    }
    Token refreshToken = refreshTokenOpt.get();
    if (refreshToken.getExpirationDate().before(new Date())) {
      tokenRepository.delete(refreshToken);
      return false;
    }
    return true;
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key(jwtSecret)).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}