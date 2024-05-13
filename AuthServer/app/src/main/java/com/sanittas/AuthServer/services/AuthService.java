package com.sanittas.AuthServer.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class AuthService {

    public static String generateToken(UserDetails user, Date expiryDate) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("6fb1PU1usC68dYDb2XBKepmoEllGVUW3vWWaOQ/BYSU=");
            String authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withSubject(user.getUsername())
                    .withClaim("role", authorities)
                    .withExpiresAt(expiryDate)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }

}
