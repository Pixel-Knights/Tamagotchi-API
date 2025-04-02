package pixelknights.com.tamagochi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixelknights.com.tamagochi.exception.BadRequestException;
import pixelknights.com.tamagochi.exception.InternalServerException;
import pixelknights.com.tamagochi.exception.NotFoundException;
import pixelknights.com.tamagochi.model.Tamagochi;
import pixelknights.com.tamagochi.repository.TamagochiRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TamagochiService {

    @Autowired
    private TamagochiRepository tamagochiRepository;

    //CRUD - Create - Cria um novo bichinho
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

    //CRUD - Read - Retorna pessoa pelo ID
    public Tamagochi findById(Long id){
        try {
            return tamagochiRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("tamagochi com ID " + id + " não encontrado"));
        } catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InternalServerException("Erro interno ao buscar tamagochi: " + e.getMessage());
        }
    }

    //CRUD - Read - Retorna todos os registros de pessoa
    public List<Tamagochi> findAll(){
        return tamagochiRepository.findAll();
    }




}
