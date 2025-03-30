package pixelknights.com.tamagochi.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "tamagochi")
public class Tamagochi {

    //Cria um enum com possiveis tipos de tamagochi
    private enum TipoTamagochi { gato, cachorro, corvo, dragao };
    //Cria um enum com possíveis tipos de estado para as necessidades do bichinho
    private enum Estado { pessimo, ruim, normal, bom, otimo }

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
    private Timestamp last_sleep;

    @Column(nullable = false)
    private Timestamp last_feed;

    @Column(nullable = false)
    private Timestamp last_clean;

    @Column(nullable = false)
    private Timestamp last_play;

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
        this.last_clean = new Timestamp(System.currentTimeMillis());
        this.last_feed = new Timestamp(System.currentTimeMillis());
        this.last_play = new Timestamp(System.currentTimeMillis());
        this.last_sleep = new Timestamp(System.currentTimeMillis());
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

    public Timestamp getLast_sleep() {
        return last_sleep;
    }

    public void setLast_sleep(Timestamp last_sleep) {
        this.last_sleep = last_sleep;
    }

    public Timestamp getLast_feed() {
        return last_feed;
    }

    public void setLast_feed(Timestamp last_feed) {
        this.last_feed = last_feed;
    }

    public Timestamp getLast_clean() {
        return last_clean;
    }

    public void setLast_clean(Timestamp last_clean) {
        this.last_clean = last_clean;
    }

    public Timestamp getLast_play() {
        return last_play;
    }

    public void setLast_play(Timestamp last_play) {
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

