package br.com.pixelknights.tamagochi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.pixelknights.tamagochi.model.Tamagochi;
import br.com.pixelknights.tamagochi.model.Usuario;

import java.util.List;

public interface TamagochiRepository extends JpaRepository<Tamagochi, Long> {
    List<Tamagochi> findByUsuario(Usuario usuario);
}
