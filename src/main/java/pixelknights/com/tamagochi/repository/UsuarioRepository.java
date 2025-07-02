package pixelknights.com.tamagochi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pixelknights.com.tamagochi.model.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
}
