package com.sistema.app.config.jwt;

import java.util.Date;

import com.sistema.app.entity.UsuarioPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);


    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;
  
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    // @Value("{jwt.secret}")
    // private static String secret;

    // @Value("jwt.expiration")
    // private static Integer expiration;

    public String generateToken(Authentication authentication){
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        return Jwts.builder().setSubject(usuarioPrincipal.getUsername())
        .setIssuedAt(new Date())
        // .setExpiration(new Date(new Date().getTime()+36000*1000))
        .setExpiration(new Date(new Date().getTime()+validityInMilliseconds))        
        .signWith(SignatureAlgorithm.HS512,secretKey)
        .compact();
    }

    public String getNombreUsuarioFromToken(String token){
        return Jwts.parser().setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    public Boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch(MalformedJwtException e){
            logger.error("token mal formado");
        }catch(UnsupportedJwtException e){
            logger.error("token no soportado");
        }catch(ExpiredJwtException e){
            logger.error("token expirado");
        }catch(IllegalArgumentException e){
            logger.error("token vacio");
        }catch(SignatureException e){
            logger.error("fail con la firma");
        }
        return false;
    }
}
