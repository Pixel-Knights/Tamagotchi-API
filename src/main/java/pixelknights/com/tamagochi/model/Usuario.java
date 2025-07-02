package pixelknights.com.tamagochi.model;

import jakarta.persistence.*;
import pixelknights.com.tamagochi.dto.UsuarioCriacaoDTO;
import pixelknights.com.tamagochi.dto.UsuarioDTO;

import java.util.List;

public class Usuario {
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

    public UsuarioDTO toUsuarioDTO(){
        return new UsuarioDTO(this.id, this.userName, this.email);
    }

    public Long getId() {
        return id;
    }

    public List<Tamagochi> getTamagochis() {
        return tamagochis;
    }

    public String getUserName() {
        return userName;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
