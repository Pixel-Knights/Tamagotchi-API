package pixelknights.com.tamagochi.service;

import org.springframework.stereotype.Service;
import pixelknights.com.tamagochi.exception.BadRequestException;
import pixelknights.com.tamagochi.exception.InternalServerException;
import pixelknights.com.tamagochi.model.Tamagochi;
import pixelknights.com.tamagochi.repository.TamagochiRepository;

@Service
public class TamagochiService {

    private TamagochiRepository tamagochiRepository;

    public Tamagochi save(Tamagochi tamagochi){
        if (tamagochi.getId() != null){
            throw new BadRequestException("O ID não deve ser passado ao salvar o novo bichinho");
        }
        //Apenas o nome é obrigatório, caso esteja vazio, deve ser apontado erro
        if (tamagochi.getNome() == null){
            throw new BadRequestException("O Nome do bichinho não deve estar vazio");
        }
        if (tamagochi.getTipoTamagochi() == null){
            throw new BadRequestException("O tipo do bichinho não deve estar vazio");
        }
        //Tenta salvar pessoa no banco e aponta erros se houver
        try {
            return tamagochiRepository.save(tamagochi);
        }
        catch (Exception e){
            throw new InternalServerException("Erro interno de servidor!");
        }
    }
}
