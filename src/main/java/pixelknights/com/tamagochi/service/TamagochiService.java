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
import java.util.Optional;

@Service
public class TamagochiService {

    @Autowired
    private TamagochiRepository tamagochiRepository;

    //CRUD - Create - Cria um novo bichinho
    public Tamagochi save(Tamagochi tamagochi){
        //if (tamagochi.getId() != null){
        //    throw new BadRequestException("O ID não deve ser passado ao salvar o novo bichinho");
        //}
        //Apenas o nome é obrigatório, caso esteja vazio, deve ser apontado erro
        if (tamagochi.getNome() == null){
            throw new BadRequestException("O Nome do bichinho não deve estar vazio");
        }
        if (tamagochi.getTipoTamagochi() == null){
            throw new BadRequestException("O tipo do bichinho não deve estar vazio");
        }
        //Tenta salvar registro no banco e aponta erros se houver
        try {
            return tamagochiRepository.save(tamagochi);
        }
        catch (Exception e){
            throw new InternalServerException("Erro interno de servidor! " + e.getMessage());
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

    //CRUD - Update - Edita os dados de um registro ou cria um novo
    public Tamagochi update(Tamagochi tamagochi){

        //procura um registro com mesmo ID do passado por requisição
        Optional<Tamagochi> findTamagochi = tamagochiRepository.findById(tamagochi.getId());

        //Apenas o nome é obrigatório, caso esteja vazio, deve ser apontado erro
        //if (tamagochi.getNome() == null){
        //    throw new BadRequestException("O Nome do bichinho não deve estar vazio");
        //}
        if (tamagochi.getTipoTamagochi() == null){
            throw new BadRequestException("O tipo do bichinho não deve estar vazio");
        }

        //Se o registro já existe
        if (findTamagochi.isPresent()){

            //Cria novo objeto com os dados da requisição
            Tamagochi updTamagochi = findTamagochi.get();

            //Atualiza o registro existente com os dados novos
            updTamagochi.setAcordado(tamagochi.isAcordado());
            updTamagochi.setSono(tamagochi.getSono());
            updTamagochi.setFome(tamagochi.getFome());
            updTamagochi.setHigiene(tamagochi.getHigiene());
            updTamagochi.setHumor(tamagochi.getHumor());

            //Tenta salvar registro no banco e aponta erros se houver
            try {
                return tamagochiRepository.save(tamagochi);
            }
            catch (Exception e){
                throw new InternalServerException("Erro interno de servidor!");
            }
        }

        //Se não existe o registro, um registro novo é criado
        //else if (tamagochi.getId() != null){
        //    throw new BadRequestException("O ID não deve ser passado ao salvar o novo bichinho");
        //}
        else {
            try {
                return tamagochiRepository.save(tamagochi);
            } catch (Exception e) {
                throw new InternalServerException("Erro interno de servidor!");
            }
        }
    }

    //CRUD - Delete - Deeleta registro por ID
    public void delete(Long id){

        tamagochiRepository.deleteById(id);
    }




}
