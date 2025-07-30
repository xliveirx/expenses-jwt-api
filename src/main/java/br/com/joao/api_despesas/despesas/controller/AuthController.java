package br.com.joao.api_despesas.despesas.controller;

import br.com.joao.api_despesas.despesas.domain.auth.TokenService;
import br.com.joao.api_despesas.despesas.domain.user.User;
import br.com.joao.api_despesas.despesas.dto.LoginRequestDTO;
import br.com.joao.api_despesas.despesas.dto.TokenDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDTO> login (@Valid @RequestBody LoginRequestDTO dto){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenDTO(token));
    }
}
