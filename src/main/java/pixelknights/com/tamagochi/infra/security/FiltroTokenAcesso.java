package pixelknights.com.tamagochi.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pixelknights.com.tamagochi.infra.exception.NotFoundException;
import pixelknights.com.tamagochi.model.Usuario;
import pixelknights.com.tamagochi.repository.UsuarioRepository;
import pixelknights.com.tamagochi.resource.AutenticacaoResource;
import pixelknights.com.tamagochi.resource.UsuarioResource;
import pixelknights.com.tamagochi.service.TokenService;

import java.io.IOException;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    public FiltroTokenAcesso(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //recuperar o token da request
        String token = recuperarTokenRequisicao(request);

        if (token != null){
            //validação do token
            String email = tokenService.verificarToken(token);
            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow();

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null){
            authorizationHeader = authorizationHeader.replace("Bearer ", "");
            return authorizationHeader;
        }
        return null;
    }
}
