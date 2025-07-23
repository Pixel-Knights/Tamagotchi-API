package pixelknights.com.tamagochi.resource;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pixelknights.com.tamagochi.dto.DadosLoginDTO;
import pixelknights.com.tamagochi.dto.DadosRefreshTokenDTO;
import pixelknights.com.tamagochi.dto.DadosTokenDTO;
import pixelknights.com.tamagochi.model.Usuario;
import pixelknights.com.tamagochi.repository.UsuarioRepository;
import pixelknights.com.tamagochi.service.TokenService;

@RestController
@RequestMapping("/api/login")
public class AutenticacaoResource {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoResource(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<DadosTokenDTO> efetuarLogin(@Valid @RequestBody DadosLoginDTO dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String tokenAcesso = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        String refreshToken = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenDTO(tokenAcesso, refreshToken));
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosTokenDTO> atualizarToken(@Valid @RequestBody DadosRefreshTokenDTO dados){
        var refreshToken = dados.refreshToken();
        Long idUsuario = Long.valueOf(tokenService.verificarToken(refreshToken));
        var usuario = usuarioRepository.findById(idUsuario).orElseThrow();

        String tokenAcesso = tokenService.gerarToken(usuario);
        String newRefreshToken = tokenService.gerarRefreshToken(usuario);

        return ResponseEntity.ok(new DadosTokenDTO(tokenAcesso, newRefreshToken));
    }

    //@PostMapping
    //public ResponseEntity<String> efetuarLogout(Authentication authentication) {
    //    SecurityContextHolder.clearContext();
    //    return ResponseEntity.ok("Logout realizado com sucesso!");
    //}
}
