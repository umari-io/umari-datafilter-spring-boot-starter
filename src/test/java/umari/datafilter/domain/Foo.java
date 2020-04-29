package umari.datafilter.domain;

import javax.persistence.*;

@Entity
public class Foo {

    public enum Genero {
        MASCULINO, FEMININO
    }

    @Id
    private Long id;

    @Column
    private String nome;

    @Column
    private Integer idade;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }
}
