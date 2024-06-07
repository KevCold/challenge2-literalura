package mx.kevcold.challenge_literalura.principal;

import mx.kevcold.challenge_literalura.model.*;
import mx.kevcold.challenge_literalura.repository.ILibrosRepository;
import mx.kevcold.challenge_literalura.service.ConsumoApi;
import mx.kevcold.challenge_literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ILibrosRepository repository;
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();

    public Principal(ILibrosRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    public void buscarLibroPorTitulo() {
        System.out.println("Introduzca el nombre del libro que desea buscar:");
        var nombre = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombre.replace(" ", "+").toLowerCase());

        if (json.isEmpty() || !json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {
            var datos = conversor.obtenerDatos(json, Datos.class);

            Optional<DatosLibros> libroBuscado = datos.libros().stream().findFirst();
            if (libroBuscado.isPresent()) {
                System.out.println(
                        "\n------------- LIBRO \uD83D\uDCD9  --------------" +
                                "\nTítulo: " + libroBuscado.get().titulo() +
                                "\nAutor: " + libroBuscado.get().autor().stream()
                                .map(DatosAutor::nombre).limit(1).collect(Collectors.joining()) +
                                "\nIdioma: " + libroBuscado.get().idiomas().stream().collect(Collectors.joining()) +
                                "\nNúmero de descargas: " + libroBuscado.get().numeroDescargas() +
                                "\n--------------------------------------\n"
                );

                try {
                    List<Libro> libroEncontrado = libroBuscado.stream().map(Libro::new).collect(Collectors.toList());
                    Autor autorAPI = libroBuscado.stream().
                            flatMap(l -> l.autor().stream()
                                    .map(a -> new Autor(a)))
                            .collect(Collectors.toList()).stream().findFirst().get();
                    Optional<Autor> autorBD = repository.buscarAutorPorNombre(libroBuscado.get().autor().stream()
                            .map(DatosAutor::nombre)
                            .collect(Collectors.joining()));
                    Optional<Libro> libroOptional = repository.buscarLibroPorNombre(nombre);
                    if (libroOptional.isPresent()) {
                        System.out.println("El libro ya está guardado en la BD.");
                    } else {
                        Autor autor;
                        if (autorBD.isPresent()) {
                            autor = autorBD.get();
                            System.out.println("EL autor ya esta guardado en la BD");
                        } else {
                            autor = autorAPI;
                            repository.save(autor);
                        }
                        autor.setLibros(libroEncontrado);
                        repository.save(autor);
                    }
                } catch (Exception e) {
                    System.out.println("Warning! " + e.getMessage());
                }
            } else {
                System.out.println("Libro no encontrado!");
            }
        }
    }

}
