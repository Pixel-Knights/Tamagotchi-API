package pixelknights.com.tamagochi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pixelknights.com.tamagochi.model.Tamagochi;
import pixelknights.com.tamagochi.model.Usuario;

import java.util.List;

public interface TamagochiRepository extends JpaRepository<Tamagochi, Long> {
    List<Tamagochi> findByUsuario(Usuario usuario);
}
