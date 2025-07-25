package pixelknights.com.tamagochi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pixelknights.com.tamagochi.dto.TamagochiDTO;
import pixelknights.com.tamagochi.model.Tamagochi;
import pixelknights.com.tamagochi.service.TamagochiService;

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

    //@PutMapping// PUT http://localhost:8080/api/tamagochi
    //public ResponseEntity<Tamagochi> update(@RequestBody TamagochiDTO tamagochiDTO){
    //
    //    Tamagochi updTamagochi = new Tamagochi(tamagochiDTO);
    //
    //    tamagochiService.update(updTamagochi);
    //
    //    return ResponseEntity.ok(updTamagochi);
    //}

    @DeleteMapping("/{id}")// DELETE http://localhost:8080/api/tamagochi/{id}
    public ResponseEntity<?> delete(@PathVariable Long id){

        tamagochiService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
