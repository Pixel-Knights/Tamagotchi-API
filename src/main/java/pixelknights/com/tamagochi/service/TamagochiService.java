package pixelknights.com.tamagochi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixelknights.com.tamagochi.Enum.Estado;
import pixelknights.com.tamagochi.dto.TamagochiDTO;
import pixelknights.com.tamagochi.infra.exception.BadRequestException;
import pixelknights.com.tamagochi.infra.exception.InternalServerException;
import pixelknights.com.tamagochi.infra.exception.NotFoundException;
import pixelknights.com.tamagochi.model.Tamagochi;
import pixelknights.com.tamagochi.repository.TamagochiRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TamagochiService {

    @Autowired
    private TamagochiRepository tamagochiRepository;

    //CRUD - Create - Cria um novo bichinho
    public Tamagochi save(TamagochiDTO tamagochiDTO){

        //Os campos do DTO são obrigatórios, caso estejam vazios, deve ser apontado erro
        if (tamagochiDTO.nome() == null){
            throw new BadRequestException("O Nome do bichinho não deve estar vazio");
        }
        if (tamagochiDTO.tipoTamagochi() == null){
            throw new BadRequestException("O tipo do bichinho não deve estar vazio");
        }
        //Tenta salvar registro no banco e aponta erros se houver
        try {
            Tamagochi tamagochi = new Tamagochi(tamagochiDTO);
            return tamagochiRepository.save(tamagochi);
        }
        catch (Exception e){
            throw new InternalServerException("Erro interno de servidor! " + e.getMessage());
        }
    }

    //CRUD - Read - Retorna pessoa pelo ID
    public Tamagochi findById(Long id){
        try {
            Tamagochi tamagochi = tamagochiRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("tamagochi com ID " + id + " não encontrado"));

            return verificarEstadoTamagochi(tamagochi);
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
        if (tamagochi.getNome() == null){
            throw new BadRequestException("O Nome do bichinho não deve estar vazio");
        }
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

    //Verificação e edição de banco
    //Vai consultar o banco de dados a respeito do tamagochi
    // e alterar seus estados de acordo com o tempo passado
    public Tamagochi verificarEstadoTamagochi(Tamagochi tamagochi){

        tamagochi = defineEstadosTamagochi(tamagochi, "sono", 8,16,20,24,30);
        tamagochi = defineEstadosTamagochi(tamagochi, "fome", 3,5,7,12,20);
        tamagochi = defineEstadosTamagochi(tamagochi, "humor", 5,10,15,20,30);
        tamagochi = defineEstadosTamagochi(tamagochi, "higiene", 12,24,36,48,60);

        return tamagochi;
    }

    public Tamagochi defineEstadosTamagochi(Tamagochi tamagochi, String status, int horasOtimo, int horasBom, int horasOK, int horasRuim, int horasPessimo){

        Duration timeNoClean = Duration.between(tamagochi.getLast_clean(),LocalDateTime.now());
        Duration timeNoSleep = Duration.between(tamagochi.getLast_sleep(),LocalDateTime.now());
        Duration timeNoPlay = Duration.between(tamagochi.getLast_play(),LocalDateTime.now());
        Duration timeNoFeed = Duration.between(tamagochi.getLast_feed(),LocalDateTime.now());

        switch (status){

            case "fome":
                if (timeNoFeed.toHours() < horasOtimo){
                    tamagochi.setFome(Estado.otimo);
                    update(tamagochi);
                }
                else if (timeNoFeed.toHours() < horasBom) {
                    tamagochi.setFome(Estado.bom);
                    update(tamagochi);
                }

                else if (timeNoFeed.toHours() < horasOK) {
                    tamagochi.setFome(Estado.normal);
                    update(tamagochi);
                }

                else if (timeNoFeed.toHours() < horasRuim) {
                    tamagochi.setFome(Estado.ruim);
                    update(tamagochi);
                }

                else {
                    tamagochi.setFome(Estado.pessimo);
                    update(tamagochi);
                }
                break;

            case "sono":
                if (timeNoSleep.toHours() < horasOtimo){
                    tamagochi.setSono(Estado.otimo);
                    update(tamagochi);
                }
                else if (timeNoSleep.toHours() < horasBom) {
                    tamagochi.setSono(Estado.bom);
                    update(tamagochi);
                }

                else if (timeNoSleep.toHours() < horasOK) {
                    tamagochi.setSono(Estado.normal);
                    update(tamagochi);
                }

                else if (timeNoSleep.toHours() < horasRuim) {
                    tamagochi.setSono(Estado.ruim);
                    update(tamagochi);
                }

                else {
                    tamagochi.setSono(Estado.pessimo);
                    update(tamagochi);
                }
                break;

            case "humor":
                if (timeNoPlay.toHours() < horasOtimo){
                    tamagochi.setHumor(Estado.otimo);
                    update(tamagochi);
                }
                else if (timeNoPlay.toHours() < horasBom) {
                    tamagochi.setHumor(Estado.bom);
                    update(tamagochi);
                }

                else if (timeNoPlay.toHours() < horasOK) {
                    tamagochi.setHumor(Estado.normal);
                    update(tamagochi);
                }

                else if (timeNoPlay.toHours() < horasRuim) {
                    tamagochi.setHumor(Estado.ruim);
                    update(tamagochi);
                }

                else {
                    tamagochi.setHumor(Estado.pessimo);
                    update(tamagochi);
                }
                break;

            case "higiene":
                if (timeNoClean.toHours() < horasOtimo){
                    tamagochi.setHigiene(Estado.otimo);
                    update(tamagochi);
                }
                else if (timeNoClean.toHours() < horasBom) {
                    tamagochi.setHigiene(Estado.bom);
                    update(tamagochi);
                }

                else if (timeNoClean.toHours() < horasOK) {
                    tamagochi.setHigiene(Estado.normal);
                    update(tamagochi);
                }

                else if (timeNoClean.toHours() < horasRuim) {
                    tamagochi.setHigiene(Estado.ruim);
                    update(tamagochi);
                }

                else {
                    tamagochi.setHigiene(Estado.pessimo);
                    update(tamagochi);
                }
                break;

            default:
                break;
        }
        return tamagochi;
    }




}
