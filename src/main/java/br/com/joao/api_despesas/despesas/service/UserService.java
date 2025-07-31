package br.com.joao.api_despesas.despesas.service;

import br.com.joao.api_despesas.despesas.domain.user.User;
import br.com.joao.api_despesas.despesas.dto.RoleUpdateDTO;
import br.com.joao.api_despesas.despesas.dto.UserRequestDTO;
import br.com.joao.api_despesas.despesas.dto.UserResponseDTO;
import br.com.joao.api_despesas.despesas.dto.UserUpdateDTO;
import br.com.joao.api_despesas.despesas.exceptions.ApplicationException;
import br.com.joao.api_despesas.despesas.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(@Valid UserRequestDTO dto) {
        if(userRepository.findByEmailIgnoreCaseAndEnabledTrue(dto.email()).isPresent()){
            throw new ApplicationException("Email already exists");
        }

        if(userRepository.findByUsernameIgnoreCaseAndEnabledTrue(dto.username()).isPresent()){
            throw new ApplicationException("Username already exists");
        }

        if(!dto.password().equals(dto.passwordConfirmation())){
            throw new ApplicationException("Passwords don't match");
        }

        var encodedPassword = passwordEncoder.encode(dto.password());
        User user = new User(dto, encodedPassword);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username)
                .orElseThrow(() -> new ApplicationException("User not found"));
        return user;
    }

    @Transactional
    public User updateUser(@Valid UserUpdateDTO dto, User logged) {
        if(dto.name() != null){
            logged.setName(dto.name());
        }
        if(dto.username() != null && !userRepository.findByUsernameIgnoreCaseAndEnabledTrue(dto.username()).isPresent()){
            logged.setUsername(dto.username());
        }
        return userRepository.save(logged);
    }

    @Transactional
    public void disableUser(User logged) {
        logged.setEnabled(false);
        userRepository.save(logged);
    }

    @Transactional
    public void disableUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public void enableUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserRole(Long id, @Valid RoleUpdateDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApplicationException("User not found"));
        user.setRole(dto.role());
        userRepository.save(user);
    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(u -> new UserResponseDTO(u.getId(), u.getName(), u.getEmail(), u.getUsername()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCaseAndEnabledTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
