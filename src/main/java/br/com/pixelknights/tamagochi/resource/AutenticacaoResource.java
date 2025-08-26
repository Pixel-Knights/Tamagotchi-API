package br.com.pixelknights.tamagochi.resource;

import br.com.pixelknights.tamagochi.dto.*;
import br.com.pixelknights.tamagochi.infra.email.EmailService;
import br.com.pixelknights.tamagochi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import br.com.pixelknights.tamagochi.dto.*;
import br.com.pixelknights.tamagochi.model.Usuario;
import br.com.pixelknights.tamagochi.repository.UsuarioRepository;
import br.com.pixelknights.tamagochi.service.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoResource {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    private final EmailService emailService;

    private final UsuarioService usuarioService;

    public AutenticacaoResource(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioRepository usuarioRepository, EmailService emailService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<DadosTokenDTO> efetuarLogin(@Valid @RequestBody DadosLoginDTO dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String tokenAcesso = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        String refreshToken = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenDTO(tokenAcesso, refreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(@Valid @RequestBody UsuarioCriacaoDTO usuarioCriacaoDTO){
        if(this.usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(usuarioCriacaoDTO.email()).isPresent()) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(usuarioCriacaoDTO.senha());

        Usuario usuario = new Usuario(usuarioCriacaoDTO.userName(), usuarioCriacaoDTO.email(), encryptedPassword);

        emailService.enviarEmailVerificacao(usuario);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok().body(usuario.toUsuarioDTO());
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

    @GetMapping("/verificar-conta")
    public ResponseEntity<String> verificarEmail(@RequestParam String codigo){
        usuarioService.verificarEmail(codigo);
        return ResponseEntity.ok("Conta verificada com sucesso!");
    }
}
