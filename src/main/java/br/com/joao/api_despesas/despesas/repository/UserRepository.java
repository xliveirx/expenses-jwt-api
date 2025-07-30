package br.com.joao.api_despesas.despesas.repository;

import br.com.joao.api_despesas.despesas.domain.user.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCaseAndEnabledTrue(String email);
    Optional<User> findByUsernameIgnoreCaseAndEnabledTrue(String username);
}
