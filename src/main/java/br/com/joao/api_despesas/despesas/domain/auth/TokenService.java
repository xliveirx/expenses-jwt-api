package br.com.joao.api_despesas.despesas.domain.auth;

import br.com.joao.api_despesas.despesas.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        try {
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withIssuer("API Despesas")
                    .withExpiresAt(expiresAt(30))
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException(e);
        }
    }

    public String verifyToken(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("API Despesas")
                    .build();
            decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e){
            throw new RuntimeException(e);
        }
    }

    private Instant expiresAt(Integer minutes){
        return LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.of("-03:00"));
    }
}
