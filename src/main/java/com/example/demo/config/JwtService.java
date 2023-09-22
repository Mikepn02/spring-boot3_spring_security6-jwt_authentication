package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "d4dHyN7T80+YMC4aEspuSj3HLGyBGt9/npMIXKMkIz4MUascPekyerwiIsIv0yOJXdnEqWyUA0LqELgSCoXESv5GRk+DGDhOiT1eiMDM4lx0IwJ8fryMUoh6nnD3Yrv7r1y+WpvL8845dS7UjfgwDQ==";

    private  long jwtExpiration;

    public String extractUserName(String token){
        return extractClaim(token , Claims::getSubject);
    }
    //subject willl be the user email

    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver) {
        final  Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return  buildToken(new HashMap<>() , userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails);
    }
    public String buildToken(
            Map<String , Object> extraClaims,
            UserDetails userDetails

    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return  extractClaim(token , Claims::getExpiration);

    }


    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
    // setsignInKey is secret that is used to digitally sign jwt it creates the signature of jwt which is used to verify the sender of jwt is who and what claims to be

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}
