package pixelknights.com.tamagochi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pixelknights.com.tamagochi.Enum.Estado;
import pixelknights.com.tamagochi.dto.TamagochiDTO;
import pixelknights.com.tamagochi.infra.exception.BadRequestException;
import pixelknights.com.tamagochi.infra.exception.InternalServerException;
import pixelknights.com.tamagochi.infra.exception.NotFoundException;
import pixelknights.com.tamagochi.model.Tamagochi;
import pixelknights.com.tamagochi.model.Usuario;
import pixelknights.com.tamagochi.repository.TamagochiRepository;
import pixelknights.com.tamagochi.repository.UsuarioRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TamagochiService {

    @Autowired
    private final TamagochiRepository tamagochiRepository;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    public TamagochiService(TamagochiRepository tamagochiRepository, UsuarioRepository usuarioRepository) {
        this.tamagochiRepository = tamagochiRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //CRUD - Create - Cria um novo bichinho
    public Tamagochi save(TamagochiDTO tamagochiDTO){

        validarTamagochi(tamagochiDTO);

        //Tenta salvar registro no banco e aponta erros se houver
        try {
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Tamagochi tamagochi = new Tamagochi(tamagochiDTO, usuario);
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
    public List<Tamagochi> findTamagochisByUsuario(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tamagochi> tamagochis = tamagochiRepository.findByUsuario(usuario);
        for (Tamagochi tamagochi : tamagochis){
            verificarEstadoTamagochi(tamagochi);
        }
        return tamagochis;
    }

    //CRUD - Update - Edita os dados de um registro ou cria um novo
    public Tamagochi update(Tamagochi tamagochi){

        validarTamagochi(tamagochi);

        //procura um registro com mesmo ID do passado por requisição
        Optional<Tamagochi> findTamagochi = tamagochiRepository.findById(tamagochi.getId());


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

        return update(tamagochi);
    }

    public void validarTamagochi(TamagochiDTO tamagochi){
        //Apenas o nome é obrigatório, caso esteja vazio, deve ser apontado erro
        if (tamagochi.nome() == null){
            throw new BadRequestException("O Nome do bichinho não deve estar vazio");
        }
        if (tamagochi.tipoTamagochi() == null){
            throw new BadRequestException("O tipo do bichinho não deve estar vazio");
        }
    }

    public void validarTamagochi(Tamagochi tamagochi){
        //Apenas o nome é obrigatório, caso esteja vazio, deve ser apontado erro
        if (tamagochi.getNome() == null){
            throw new BadRequestException("O Nome do bichinho não deve estar vazio");
        }
        if (tamagochi.getTipoTamagochi() == null){
            throw new BadRequestException("O tipo do bichinho não deve estar vazio");
        }
    }

    public Tamagochi defineEstadosTamagochi(Tamagochi tamagochi, String status, int otimo, int bom, int normal, int ruim, int pessimo){

        long horas;

        switch (status) {
            case "sono":
                horas = Duration.between(tamagochi.getLast_sleep(), LocalDateTime.now()).toHours();
                tamagochi.setSono(calcularEstado(horas, otimo, bom, normal, ruim));
                break;
            case "fome":
                horas = Duration.between(tamagochi.getLast_feed(), LocalDateTime.now()).toHours();
                tamagochi.setFome(calcularEstado(horas, otimo, bom, normal, ruim));
                break;
            case "humor":
                horas = Duration.between(tamagochi.getLast_play(), LocalDateTime.now()).toHours();
                tamagochi.setHumor(calcularEstado(horas, otimo, bom, normal, ruim));
                break;
            case "higiene":
                horas = Duration.between(tamagochi.getLast_clean(), LocalDateTime.now()).toHours();
                tamagochi.setHigiene(calcularEstado(horas, otimo, bom, normal, ruim));
                break;
        }

        return tamagochi;
    }

    public Estado calcularEstado(long horas, int otimo, int bom, int normal, int ruim) {
        if (horas < otimo) return Estado.otimo;
        else if (horas < bom) return Estado.bom;
        else if (horas < normal) return Estado.normal;
        else if (horas < ruim) return Estado.ruim;
        else return Estado.pessimo;
    }




}
