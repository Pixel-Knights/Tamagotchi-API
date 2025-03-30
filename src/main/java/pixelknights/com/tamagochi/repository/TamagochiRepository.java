package pixelknights.com.tamagochi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pixelknights.com.tamagochi.model.Tamagochi;

public interface TamagochiRepository extends JpaRepository<Tamagochi, Long> { }
