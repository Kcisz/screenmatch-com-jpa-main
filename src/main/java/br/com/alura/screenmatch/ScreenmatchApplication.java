package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot para o projeto Screenmatch.
 * Esta classe é responsável por inicializar e executar a aplicação.
 */
@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	// Injeção de dependência do repositório de séries para acesso ao banco de dados.
	@Autowired
	private SerieRepository serieRepository;

	/**
	 * Método principal que inicia a aplicação Spring Boot.
	 * @param args Argumentos da linha de comando.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	/**
	 * Método `run` que é executado após a inicialização da aplicação Spring.
	 * Ele cria uma instância da classe Principal e exibe o menu para o usuário.
	 * @param args Argumentos da linha de comando.
	 * @throws Exception Lança exceção em caso de erro.
	 */
	@Override
	public void run(String... args) throws Exception {
		// Cria uma instância de Principal, injetando o repositório de séries.
		Principal principal = new Principal(serieRepository);
		// Inicia a interface de menu para o usuário.
		principal.exibeMenu();
	}
}
