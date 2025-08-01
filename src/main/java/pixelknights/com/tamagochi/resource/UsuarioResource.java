package pixelknights.com.tamagochi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pixelknights.com.tamagochi.dto.TrocaSenhaDTO;
import pixelknights.com.tamagochi.dto.UsuarioCriacaoDTO;
import pixelknights.com.tamagochi.dto.UsuarioDTO;
import pixelknights.com.tamagochi.infra.exception.IncorrectPasswordException;
import pixelknights.com.tamagochi.model.Usuario;
import pixelknights.com.tamagochi.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioResource {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping// PUT http://localhost:8080/api/usuario
    public ResponseEntity<UsuarioDTO> update(TrocaSenhaDTO dados){

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (new BCryptPasswordEncoder().matches(dados.senhaAntiga(), usuarioLogado.getPassword())){
            usuarioLogado.setSenha(dados.senhaNova());
        }
        else {
            throw new IncorrectPasswordException("A senha está incorreta");
        }

        UsuarioDTO usuarioDTO = usuarioLogado.toUsuarioDTO();

        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping("/desativar")// PUT http://localhost:8080/api/usuario
    public ResponseEntity<UsuarioDTO> update(){

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        usuarioLogado.setAtivado(false);

        usuarioService.update(usuarioLogado);

        UsuarioDTO usuarioAtualizado = usuarioLogado.toUsuarioDTO();

        return ResponseEntity.ok(usuarioAtualizado);
    }
}
