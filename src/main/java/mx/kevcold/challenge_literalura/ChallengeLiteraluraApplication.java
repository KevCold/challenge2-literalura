package mx.kevcold.challenge_literalura;

import mx.kevcold.challenge_literalura.principal.Principal;
import mx.kevcold.challenge_literalura.repository.ILibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraluraApplication implements CommandLineRunner {

    @Autowired
    private ILibrosRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(ChallengeLiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repository);
        principal.muestraElMenu();
    }
}
