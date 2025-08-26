package br.com.pixelknights.tamagochi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.pixelknights.tamagochi.dto.TamagochiDTO;
import br.com.pixelknights.tamagochi.model.Tamagochi;
import br.com.pixelknights.tamagochi.service.TamagochiService;

import java.util.List;

@RestController
@RequestMapping("/api/tamagochi")
public class TamagochiResource {

    @Autowired
    private TamagochiService tamagochiService;

    @PostMapping// POST http://localhost:8080/api/tamagochi
    public ResponseEntity<Tamagochi> save(@RequestBody TamagochiDTO tamagochiDTO){

        Tamagochi newTamagochi = tamagochiService.save(tamagochiDTO);

        return ResponseEntity.ok(newTamagochi);
    }

    @GetMapping("/{id}")// GET http://localhost:8080/api/tamagochi/{id}
    public ResponseEntity<Tamagochi> findById(@PathVariable Long id){

        Tamagochi tamagochi = tamagochiService.findById(id);

        return ResponseEntity.ok(tamagochi);
    }

    @GetMapping// GET http://localhost:8080/api/tamagochi
    public  ResponseEntity<List<Tamagochi>> listTamagochisByUsuario(){

        List<Tamagochi> tamagochis = tamagochiService.findTamagochisByUsuario();

        if (tamagochis.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.ok(tamagochis);
        }
    }

    @DeleteMapping("/{id}")// DELETE http://localhost:8080/api/tamagochi/{id}
    public ResponseEntity<?> delete(@PathVariable Long id){

        tamagochiService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/feed/{id}")// PUT http://localhost:8080/api/tamagochi/feed/{id}
    public ResponseEntity<Tamagochi> feedTamagochi(@PathVariable Long id){

        Tamagochi tamagochi = tamagochiService.cuidar(id, "fome");

        return ResponseEntity.ok().body(tamagochi);
    }

    @PutMapping("/play/{id}")// PUT http://localhost:8080/api/tamagochi/play/{id}
    public ResponseEntity<Tamagochi> playTamagochi(@PathVariable Long id){

        Tamagochi tamagochi = tamagochiService.cuidar(id, "humor");

        return ResponseEntity.ok().body(tamagochi);
    }

    @PutMapping("/sleep/{id}")// PUT http://localhost:8080/api/tamagochi/sleep/{id}
    public ResponseEntity<Tamagochi> sleepTamagochi(@PathVariable Long id){

        Tamagochi tamagochi = tamagochiService.cuidar(id, "sono");

        return ResponseEntity.ok().body(tamagochi);
    }

    @PutMapping("/clean/{id}")// PUT http://localhost:8080/api/tamagochi/clean/{id}
    public ResponseEntity<Tamagochi> cleanTamagochi(@PathVariable Long id){

        Tamagochi tamagochi = tamagochiService.cuidar(id, "higiene");

        return ResponseEntity.ok().body(tamagochi);
    }

}
