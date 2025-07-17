package pixelknights.com.tamagochi.resource;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pixelknights.com.tamagochi.dto.DadosLoginDTO;
import pixelknights.com.tamagochi.model.Usuario;
import pixelknights.com.tamagochi.service.TokenService;

@RestController
@RequestMapping("/api/login")
public class AutenticacaoResource {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public AutenticacaoResource(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> efetuarLogin(@Valid @RequestBody DadosLoginDTO dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String tokenAcesso = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(tokenAcesso);
    }
}
