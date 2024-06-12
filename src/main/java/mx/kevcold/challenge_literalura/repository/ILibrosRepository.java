package mx.kevcold.challenge_literalura.repository;

import mx.kevcold.challenge_literalura.model.Autor;
import mx.kevcold.challenge_literalura.model.Idioma;
import mx.kevcold.challenge_literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ILibrosRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE l.titulo LIKE %:nombre%")
    Optional<Libro> buscarLibroPorNombre(@Param("nombre") String nombre);

    @Query("SELECT a FROM Libro l JOIN l.autor a WHERE a.nombre LIKE %:nombre%")
    Optional<Autor> buscarAutorPorNombre(@Param("nombre") String nombre);

    @Query("SELECT l FROM Autor a JOIN a.libros l")
    List<Libro> buscarTodosLosLibros();

    @Query("SELECT a FROM Autor a WHERE a.fechaFallecimiento > :fecha")
    List<Autor> buscarAutoresVivos(@Param("fecha") Integer fecha);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.idioma = :idioma")
    List<Libro> buscarLibrosPorIdioma(@Param("idioma") Idioma idioma);

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento = :fecha")
    List<Autor> listarAutoresPorNacimiento(@Param("fecha") Integer fecha);

    @Query("SELECT a FROM Autor a WHERE a.fechaFallecimiento = :fecha")
    List<Autor> listarAutoresPorFallecimiento(@Param("fecha") Integer fecha);

    @Query("SELECT l FROM Autor a JOIN a.libros l ORDER BY l.numeroDescargas DESC LIMIT 10")
    List<Libro> top10Libros();
}

