package pixelknights.com.tamagochi.model;

import jakarta.persistence.*;
import pixelknights.com.tamagochi.Enum.Estado;
import pixelknights.com.tamagochi.Enum.TipoTamagochi;

import java.time.LocalDateTime;

@Entity
@Table(name = "tamagochi")
public class Tamagochi {

    @Id //Define como ID/ Chave primária no banco
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Define autoincremento
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTamagochi tipoTamagochi;

    @Column(nullable = false)
    private boolean acordado;

    @Column(nullable = false)
    private int horasDormidas;

    @Column(nullable = false)
    private LocalDateTime last_sleep;

    @Column(nullable = false)
    private LocalDateTime last_feed;

    @Column(nullable = false)
    private LocalDateTime last_clean;

    @Column(nullable = false)
    private LocalDateTime last_play;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado humor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado higiene;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado sono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado fome;

    public Tamagochi() {}

    public Tamagochi(String nome, TipoTamagochi tipoTamagochi, Estado humor, Estado higiene, Estado sono, Estado fome) {
        this.nome = nome;
        this.tipoTamagochi = tipoTamagochi;
        this.acordado = true;
        this.horasDormidas = 8;
        this.humor = humor;
        this.higiene = higiene;
        this.sono = sono;
        this.fome = fome;
        this.last_clean = LocalDateTime.now();
        this.last_feed = LocalDateTime.now();
        this.last_play = LocalDateTime.now();
        this.last_sleep = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoTamagochi getTipoTamagochi() {
        return tipoTamagochi;
    }

    public boolean isAcordado() {
        return acordado;
    }

    public void setAcordado(boolean acordado) {
        this.acordado = acordado;
    }

    public int getHorasDormidas() {
        return horasDormidas;
    }

    public void setHorasDormidas(int horasDormidas) {
        this.horasDormidas = horasDormidas;
    }

    public LocalDateTime getLast_sleep() {
        return last_sleep;
    }

    public void setLast_sleep(LocalDateTime last_sleep) {
        this.last_sleep = last_sleep;
    }

    public LocalDateTime getLast_feed() {
        return last_feed;
    }

    public void setLast_feed(LocalDateTime last_feed) {
        this.last_feed = last_feed;
    }

    public LocalDateTime getLast_clean() {
        return last_clean;
    }

    public void setLast_clean(LocalDateTime last_clean) {
        this.last_clean = last_clean;
    }

    public LocalDateTime getLast_play() {
        return last_play;
    }

    public void setLast_play(LocalDateTime last_play) {
        this.last_play = last_play;
    }

    public Estado getHumor() {
        return humor;
    }

    public void setHumor(Estado humor) {
        this.humor = humor;
    }

    public Estado getHigiene() {
        return higiene;
    }

    public void setHigiene(Estado higiene) {
        this.higiene = higiene;
    }

    public Estado getSono() {
        return sono;
    }

    public void setSono(Estado sono) {
        this.sono = sono;
    }

    public Estado getFome() {
        return fome;
    }

    public void setFome(Estado fome) {
        this.fome = fome;
    }
}

