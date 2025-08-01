package pixelknights.com.tamagochi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pixelknights.com.tamagochi.dto.UsuarioCriacaoDTO;
import pixelknights.com.tamagochi.dto.UsuarioDTO;
import pixelknights.com.tamagochi.infra.exception.BadRequestException;
import pixelknights.com.tamagochi.infra.exception.InternalServerException;
import pixelknights.com.tamagochi.infra.exception.NotFoundException;
import pixelknights.com.tamagochi.model.Usuario;
import pixelknights.com.tamagochi.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    //CRUD - Update - Edita os dados de um registro ou cria um novo
    public UsuarioDTO update(Usuario usuario){

        //procura um registro com mesmo ID do passado por requisição
        Optional<Usuario> findUsuario = usuarioRepository.findById(usuario.getId());

        //Se o registro já existe
        if (findUsuario.isPresent()){

            //Cria novo objeto com os dados da requisição
            Usuario updUsuario = findUsuario.get();

            //Atualiza o registro existente com os dados novos
            updUsuario.setUserName(usuario.getUsername());
            updUsuario.setEmail(usuario.getEmail());
            updUsuario.setSenha(usuario.getPassword());

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }
}
