package org.springybot.utils;

import java.util.Date;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Component;
import org.springybot.entity.Admin;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

  private static final String SECRET = "5$98f12!@a$15";
  private static final int jwtExpirationMs = 1 * 60 * 60 * 60 * 1000;
  private HashMap<String, Date> invalidTokens = new HashMap<>();

  public String generateToken(Admin member) {
    return generateTokenFromUsername(member.getUsername());
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean invalidateToken(String token) {
    try {
      Claims claims = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token)
          .getBody();

      invalidTokens.put(token, claims.getExpiration());

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isTokenInvalid(String token) {
    if (invalidTokens.containsKey(token)) {
      Date expirationDate = invalidTokens.get(token);
      return expirationDate.after(new Date());
    }
    return false;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      log.info("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.info("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.info("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.info("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  public String verifyToken(String token) {
    return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET)).parseClaimsJws(token).getBody()
        .getSubject();
  }

}
