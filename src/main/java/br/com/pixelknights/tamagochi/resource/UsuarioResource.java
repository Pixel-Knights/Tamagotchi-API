package br.com.pixelknights.tamagochi.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import br.com.pixelknights.tamagochi.dto.TrocaSenhaDTO;
import br.com.pixelknights.tamagochi.dto.UsuarioDTO;
import br.com.pixelknights.tamagochi.infra.exception.IncorrectPasswordException;
import br.com.pixelknights.tamagochi.model.Usuario;
import br.com.pixelknights.tamagochi.service.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioResource {

    private final UsuarioService usuarioService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioResource(UsuarioService usuarioService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usuarioService = usuarioService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PutMapping("/troca-senha")// PUT http://localhost:8080/api/usuario
    public ResponseEntity<UsuarioDTO> update(@RequestBody TrocaSenhaDTO dados){

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (bCryptPasswordEncoder.matches((CharSequence) dados.senhaAntiga(), usuarioLogado.getPassword())){
            usuarioLogado.setSenha(bCryptPasswordEncoder.encode(dados.senhaNova()));
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
