package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Classe principal que gerencia o menu de interação com o usuário para buscar,
 * listar e salvar séries e episódios.
 */
public class Principal {

	/** Scanner para capturar a entrada do usuário. */
	private Scanner leitura = new Scanner(System.in);
	/** Serviço para consumir APIs REST. */
	private ConsumoApi consumo = new ConsumoApi();
	/** Serviço para converter dados JSON em objetos Java. */
	private ConverteDados conversor = new ConverteDados();
	/** URL base da API OMDB. */
	private final String ENDERECO = "https://www.omdbapi.com/?t=";
	/** Chave da API para autenticação. */
	private final String API_KEY = "&apikey=6585022c";

	/** Repositório para salvar e buscar séries no banco de dados. */
	private SerieRepository serieRepository;

	/** Lista de séries buscadas no banco de dados. */
	private List<Serie> series = new ArrayList<Serie>();

	/**
	 * Construtor da classe Principal que recebe e injeta o repositório de séries.
	 *
	 * @param serieRepository O repositório de séries a ser utilizado.
	 */
	public Principal(SerieRepository serieRepository) {
		this.serieRepository = serieRepository;
	}

	/**
	 * Exibe o menu de opções para o usuário e gerencia as escolhas.
	 */
	public void exibeMenu() {
		var opcao = -1;
		while (opcao != 0) {
			var menu = """
					Por favor, digite uma das opções abaixo:

					1 - Buscar séries
					2 - Buscar episódios
					3 - Listar séries buscadas

					0 - Sair
					""";

			System.out.println(menu);
			opcao = leitura.nextInt();
			leitura.nextLine();

			switch (opcao) {
				case 1:
					buscarSerieWeb();
					break;
				case 2:
					buscarEpisodioPorSerie();
					break;
				case 3:
					listarSeriesBuscadas();
					break;
				case 0:
					System.out.println("Saindo...");
					break;
				default:
					System.out.println("Opção inválida");
			}
		}
	}

	/**
	 * Busca uma série na web, cria um objeto Serie e o salva no banco de dados.
	 */
	private void buscarSerieWeb() {
		DadosSerie dados = getDadosSerie();
		Serie serie = new Serie(dados);
		// Salva a série no banco de dados através do repositório
		serieRepository.save(serie);

		System.out.println(dados);
	}

	/**
	 * Solicita o nome de uma série ao usuário, consome a API e retorna os dados da
	 * série.
	 *
	 * @return Objeto DadosSerie com as informações da série.
	 */
	private DadosSerie getDadosSerie() {

		System.out.println("Digite o nome da série para busca");
		var nomeSerie = leitura.nextLine();
		// Constrói a URL da API, substituindo espaços por '+'
		var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
		// Converte a resposta JSON para um objeto DadosSerie
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		return dados;
	}

	/**
	 * Busca todos os episódios de uma série e os imprime no console.
	 */
	private void buscarEpisodioPorSerie() {

		listarSeriesBuscadas();

		System.out.println("Escolha uma série pelo nome");
		var nomeSerie = leitura.nextLine();

		// Busca a série no banco de dados.
		Optional<Serie> serie = series.stream().filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase())).findFirst();

		if (serie.isPresent()) {

			var serieEncontrada = serie.get();

			List<DadosTemporada> temporadas = new ArrayList<>();

			// Itera sobre o número de temporadas para buscar os dados de cada uma
			for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
				var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
				DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
				temporadas.add(dadosTemporada);
			}

			// Imprime as temporadas encontradas
			temporadas.forEach(System.out::println);

			// Combina todos os episódios de todas as temporadas em uma única lista e mapeia-os para a classe Episodio.
			List<Episodio> episodios = temporadas.stream()
					.flatMap(d -> d.episodios().stream().map( e -> new Episodio(d.numero(), e)))
					.collect(Collectors.toList());

			serieEncontrada.setEpisodios(episodios);

			serieRepository.save(serieEncontrada);

		} else {
			System.out.println("Série não encontrada!");
		}
	}

	/**
	 * Busca todas as séries salvas no banco de dados e as lista no console,
	 * ordenadas por gênero.
	 */
	private void listarSeriesBuscadas() {
		series = serieRepository.findAll();
		// Ordena a lista de séries por gênero e imprime cada uma no console.
		series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);

	}
}
