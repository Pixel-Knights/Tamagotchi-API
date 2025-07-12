package pixelknights.com.tamagochi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pixelknights.com.tamagochi.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
}
