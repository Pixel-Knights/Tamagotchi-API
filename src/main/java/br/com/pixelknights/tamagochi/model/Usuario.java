package br.com.pixelknights.tamagochi.model;

import br.com.pixelknights.tamagochi.infra.exception.RegraDeNegocioException;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import br.com.pixelknights.tamagochi.dto.UsuarioCriacaoDTO;
import br.com.pixelknights.tamagochi.dto.UsuarioDTO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "USUARIO")
public class Usuario implements UserDetails {
    @Id //Define como ID/ Chave primária no banco
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Define autoincremento
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<Tamagochi> tamagochis;

    @Column()
    private boolean ativo;

    private Boolean verificado;
    private String token;
    private LocalDateTime expiracaoToken;

    public Usuario() {}

    public Usuario(UsuarioCriacaoDTO usuarioCriacaoDTO) {
        Usuario usuario = new Usuario();
        usuario.userName = usuarioCriacaoDTO.userName();
        usuario.email = usuarioCriacaoDTO.email();
        usuario.senha = usuarioCriacaoDTO.senha();
        usuario.ativo = true;
        this.verificado = false;
        this.token = UUID.randomUUID().toString();
        this.expiracaoToken = LocalDateTime.now().plusMinutes(30);
    }

    public Usuario(String userName, String email, String senhaCriptografada){
        this.userName = userName;
        this.email = email;
        this.senha = senhaCriptografada;
        this.ativo = true;
        this.verificado = false;
        this.token = UUID.randomUUID().toString();
        this.expiracaoToken = LocalDateTime.now().plusMinutes(30);
    }

    public void verificar() {
        if(expiracaoToken.isBefore(LocalDateTime.now())){
            throw new RegraDeNegocioException("Link de verificação expirou!");
        }
        this.verificado = true;
        this.ativo = true;
        this.token = null;
        this.expiracaoToken = null;
    }

    public UsuarioDTO toUsuarioDTO(){
        return new UsuarioDTO(this.id, this.userName, this.email);
    }

    public Long getId() {
        return id;
    }

    public List<Tamagochi> getTamagochis() {
        return tamagochis;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    public String getUser(){
        return userName;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivado(boolean ativo) {
        this.ativo = ativo;
    }

    public String getToken() {
        return token;
    }
}
