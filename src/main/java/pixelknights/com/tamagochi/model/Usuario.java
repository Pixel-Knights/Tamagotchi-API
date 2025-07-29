package pixelknights.com.tamagochi.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pixelknights.com.tamagochi.dto.UsuarioCriacaoDTO;
import pixelknights.com.tamagochi.dto.UsuarioDTO;

import java.util.Collection;
import java.util.List;

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

    public Usuario() {}

    public Usuario(UsuarioCriacaoDTO usuarioCriacaoDTO) {
        Usuario usuario = new Usuario();
        usuario.userName = usuarioCriacaoDTO.userName();
        usuario.email = usuarioCriacaoDTO.email();
        usuario.senha = usuarioCriacaoDTO.senha();
    }

    public Usuario(String userName, String email, String senhaCriptografada){
        this.userName = userName;
        this.email = email;
        this.senha = senhaCriptografada;
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
}
