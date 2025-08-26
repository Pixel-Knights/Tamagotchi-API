package br.com.pixelknights.tamagochi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.pixelknights.tamagochi.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCaseAndVerificadoTrue(String email);
    Optional<Usuario> findByToken(String token);
}
