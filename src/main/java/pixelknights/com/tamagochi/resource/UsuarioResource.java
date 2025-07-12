package pixelknights.com.tamagochi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pixelknights.com.tamagochi.dto.UsuarioCriacaoDTO;
import pixelknights.com.tamagochi.dto.UsuarioDTO;
import pixelknights.com.tamagochi.model.Usuario;
import pixelknights.com.tamagochi.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioResource {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping// POST http://localhost:8080/api/usuario
    public ResponseEntity<UsuarioDTO> save(@RequestBody UsuarioCriacaoDTO usuarioCriacaoDTO){

        UsuarioDTO usuarioDTO = usuarioService.save(usuarioCriacaoDTO);

        return ResponseEntity.ok(usuarioDTO);
    }

    @GetMapping("/{id}")// GET http://localhost:8080/api/usuario/{id}
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id){

        UsuarioDTO usuarioDTO = usuarioService.findById(id);

        return ResponseEntity.ok(usuarioDTO);
    }

    @GetMapping// GET http://localhost:8080/api/usuario
    public  ResponseEntity<List<UsuarioDTO>> list(){

        List<UsuarioDTO> usuarios = usuarioService.findAll();

        if (usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.ok(usuarios);
        }
    }

    @PutMapping// PUT http://localhost:8080/api/usuario
    public ResponseEntity<UsuarioDTO> update(@RequestBody Usuario updUsuario){

        usuarioService.update(updUsuario);

        UsuarioDTO usuarioDTO = updUsuario.toUsuarioDTO();

        return ResponseEntity.ok(usuarioDTO);
    }

    @DeleteMapping("/{id}")// DELETE http://localhost:8080/api/usuario{id}
    public ResponseEntity<?> delete(@PathVariable Long id){

        usuarioService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
