package br.com.joao.api_despesas.despesas.controller;

import br.com.joao.api_despesas.despesas.domain.user.User;
import br.com.joao.api_despesas.despesas.dto.RoleUpdateDTO;
import br.com.joao.api_despesas.despesas.dto.UserRequestDTO;
import br.com.joao.api_despesas.despesas.dto.UserResponseDTO;
import br.com.joao.api_despesas.despesas.dto.UserUpdateDTO;
import br.com.joao.api_despesas.despesas.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto){
        User user = userService.createUser(dto);
        return ResponseEntity.created(URI.create("/users/" + user.getId()))
                .body(new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getUsername()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username){
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getUsername()));
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO dto,
                                                      @AuthenticationPrincipal User logged){
        User user = userService.updateUser(dto, logged);
        return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getUsername()));
    }

    @DeleteMapping("/disable-profile")
    public ResponseEntity<Void> disableUser(@AuthenticationPrincipal User logged){
        userService.disableUser(logged);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/disable-profile/{id}")
    public ResponseEntity<Void> disableUser(@PathVariable Long id){
        userService.disableUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/enable-profile/{id}")
    public ResponseEntity<Void> enableUser(@PathVariable Long id){
        userService.enableUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/edit-role/{id}")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateDTO dto){
        userService.updateUserRole(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
