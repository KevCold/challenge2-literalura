package mx.kevcold.challenge_literalura.model;

import jakarta.persistence.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    //Atributos
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer numeroDescargas;
    @ManyToOne
    private Autor autor;

    //Constructores
    public Libro() {

    }

    public Libro(DatosLibros libro) {
        this.id = libro.id();
        this.titulo = libro.titulo();
        this.idioma = Idioma.fromString(libro.idiomas().stream().limit(1).collect(Collectors.joining()));
        this.numeroDescargas = libro.numeroDescargas();
    }
    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Libro: " +
                "id= " + id +
                ", titulo=' " + titulo + '\'' +
                ", idioma= " + idioma +
                ", número descargas= " + numeroDescargas +
                ", autor= " + autor;
    }
}
