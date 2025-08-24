package com.elearning.elearning_support.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.elearning.elearning_support.entities.users.User;
import com.elearning.elearning_support.security.constants.SecurityConstants;
import com.elearning.elearning_support.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Getter
public class JwtUtils {

    @Value("${jwt.accessTokenExpiredMs}")
    public String ACCESS_TOKEN_EXPIRED_IN_MS;

    @Value("${jwt.refreshTokenExpiredMs}")
    private String REFRESH_TOKEN_EXPIRED_IN_MS;

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.signatureAlgorithm}")
    private String signatureAlgorithm;

    /**
     * Generate Json Token Web
     *
     * @param user : login user
     * @return : JWT
     */
    public String generateJwt(User user) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("username", user.getUsername());
        SignatureAlgorithm saEnum = SignatureAlgorithm.valueOf(signatureAlgorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), saEnum.getJcaName());
        return Jwts.builder().setSubject(user.getUsername())
            .addClaims(claims)
            .setIssuedAt(DateUtils.getCurrentDateTime())
            .setExpiration(new Date(DateUtils.getCurrentDateTime().getTime() + Long.parseLong(ACCESS_TOKEN_EXPIRED_IN_MS)))
            .signWith(secretKeySpec)
            .compact();
    }

    /**
     * Get claims in jwt
     *
     * @param jwt : token
     * @return : claims
     */
    public Claims getClaims(String jwt) {
        try {
            // parse jwt with secret key
            return Jwts.parserBuilder().setSigningKey(getSecretKeySpec(signatureAlgorithm)).build().parseClaimsJws(jwt).getBody();
        } catch (Exception exception) {
            log.error("=====Get claims from jwt error=====");
            return null;
        }
    }

    /**
     * Get username from jwt
     *
     * @param jwt : token
     * @return : username
     */
    public String getUsernameFromJwt(String jwt) {
        try {
            Claims claims = getClaims(jwt);
            return claims.get("username", String.class);
        } catch (Exception ex) {
            log.error("Get username from jwt error");
            return null;
        }
    }

    /**
     * Valid whether
     *
     * @param jwt : token
     * @return : valid : true - not valid : false
     */
    public Boolean validateToken(String jwt) {
        try {
            Claims claims = getClaims(jwt);
            return Objects.nonNull(claims) && claims.getExpiration().after(DateUtils.getCurrentDateTime());
        } catch (Exception exception) {
            log.error("=====Invalid JWT=====");
            return false;
        }
    }

    /**
     * Generate Refresh Token
     *
     * @return : Refresh Token
     */
    public String generateRefreshToken(User user, Long expiredAtInMs) {
        return Base64.getEncoder().encodeToString(String.format("%s%s$%d", user.getUsername(), user.getEmail(), expiredAtInMs).getBytes());
    }

    /**
     * Get Token from Request Header Authorization  = Bearer  " " + Token
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(SecurityConstants.AUTH_HEADER);
        return Objects.nonNull(authHeader) ? authHeader.split(" ")[1] : null;
    }

    /**
     * get secret key spec object from secret-key and algorithm
     */
    private SecretKeySpec getSecretKeySpec(String signatureAlgorithm) {
        SignatureAlgorithm saEnum = SignatureAlgorithm.valueOf(signatureAlgorithm);
        return new SecretKeySpec(SECRET_KEY.getBytes(), saEnum.getJcaName());
    }
}
