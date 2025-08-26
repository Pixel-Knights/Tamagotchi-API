package br.com.pixelknights.tamagochi.service;

import br.com.pixelknights.tamagochi.infra.email.EmailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.pixelknights.tamagochi.dto.UsuarioDTO;
import br.com.pixelknights.tamagochi.infra.exception.InternalServerException;
import br.com.pixelknights.tamagochi.model.Usuario;
import br.com.pixelknights.tamagochi.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    //CRUD - Update - Edita os dados de um registro ou cria um novo
    public UsuarioDTO update(Usuario usuario){

        //procura um registro com mesmo ID do passado por requisição
        Optional<Usuario> findUsuario = usuarioRepository.findById(usuario.getId());

        //Se o registro já existe
        if (findUsuario.isPresent()){

            //Cria novo objeto com os dados da requisição
            Usuario updUsuario = findUsuario.get();

            //Atualiza o registro existente com os dados novos
            updUsuario.setUserName(usuario.getUser());
            updUsuario.setEmail(usuario.getEmail());
            updUsuario.setSenha(usuario.getPassword());
            updUsuario.setAtivado(usuario.isAtivo());

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
        return usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }

    @Transactional
    public void verificarEmail(String codigo) {
        var usuario = usuarioRepository.findByToken(codigo).orElseThrow();
        usuario.verificar();
    }
}
