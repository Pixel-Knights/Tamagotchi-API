package pixelknights.com.tamagochi.service;

import org.springframework.beans.factory.annotation.Autowired;
import pixelknights.com.tamagochi.Enum.Estado;
import pixelknights.com.tamagochi.dto.TamagochiDTO;
import pixelknights.com.tamagochi.dto.UsuarioCriacaoDTO;
import pixelknights.com.tamagochi.dto.UsuarioDTO;
import pixelknights.com.tamagochi.exception.BadRequestException;
import pixelknights.com.tamagochi.exception.InternalServerException;
import pixelknights.com.tamagochi.exception.NotFoundException;
import pixelknights.com.tamagochi.model.Tamagochi;
import pixelknights.com.tamagochi.model.Usuario;
import pixelknights.com.tamagochi.repository.UsuarioRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    //CRUD - Create - Cria um novo bichinho
    public UsuarioDTO save(UsuarioCriacaoDTO usuarioCriacaoDTO){

        //Os campos do DTO são obrigatórios, caso estejam vazios, deve ser apontado erro
        if (usuarioCriacaoDTO.userName() == null){
            throw new BadRequestException("O Nome do usuário não pode estar vazio");
        }
        if (usuarioCriacaoDTO.email() == null){
            throw new BadRequestException("O email do usuário não pode estar vazio");
        }
        if (usuarioCriacaoDTO.email().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new BadRequestException("O email do usuário não é valido");
        }
        //Tenta salvar registro no banco e aponta erros se houver
        try {
            Usuario usuario = new Usuario(usuarioCriacaoDTO);
            usuarioRepository.save(usuario);
            return usuario.toUsuarioDTO();
        }
        catch (Exception e){
            throw new InternalServerException("Erro desconhecido ao criar usuario! " + e.getMessage());
        }
    }

    //CRUD - Read - Retorna pessoa pelo ID
    public UsuarioDTO findById(Long id){
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("usuário com ID " + id + " não encontrado"));

            return usuario.toUsuarioDTO();
        } catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InternalServerException("Erro interno ao buscar usuário: " + e.getMessage());
        }
    }

    //CRUD - Read - Retorna todos os registros de pessoa
    public List<UsuarioDTO> findAll(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();
        for (Usuario usuario : usuarios){
            UsuarioDTO usuarioDTO = usuario.toUsuarioDTO();
            usuarioDTOS.add(usuarioDTO);
        }
        return usuarioDTOS;
    }

    //CRUD - Update - Edita os dados de um registro ou cria um novo
    public UsuarioDTO update(Usuario usuario){

        //procura um registro com mesmo ID do passado por requisição
        Optional<Usuario> findUsuario = usuarioRepository.findById(usuario.getId());

        //Os campos do DTO são obrigatórios, caso estejam vazios, deve ser apontado erro
        if (usuario.getUserName() == null){
            throw new BadRequestException("O Nome do usuário não pode estar vazio");
        }
        if (usuario.getEmail() == null){
            throw new BadRequestException("O email do usuário não pode estar vazio");
        }
        if (usuario.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new BadRequestException("O email do usuário não é valido");
        }

        //Se o registro já existe
        if (findUsuario.isPresent()){

            //Cria novo objeto com os dados da requisição
            Usuario updUsuario = findUsuario.get();

            //Atualiza o registro existente com os dados novos
            updUsuario.setUserName(usuario.getUserName());
            updUsuario.setEmail(usuario.getEmail());
            updUsuario.setSenha(usuario.getSenha());

            //Tenta salvar registro no banco e aponta erros se houver
            try {
                usuarioRepository.save(updUsuario);
                return updUsuario.toUsuarioDTO();
            }
            catch (Exception e){
                throw new InternalServerException("Erro desconhecido ao editar usuário!");
            }
        }

        else {
            try {
                usuarioRepository.save(usuario);
                return usuario.toUsuarioDTO();
            } catch (Exception e) {
                throw new InternalServerException("Erro ao salvar novo usuário!");
            }
        }
    }

    //CRUD - Delete - Deeleta registro por ID
    public void delete(Long id){
        usuarioRepository.deleteById(id);
    }
}
