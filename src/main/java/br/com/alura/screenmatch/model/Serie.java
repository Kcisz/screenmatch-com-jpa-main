package br.com.alura.screenmatch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import br.com.alura.screenmatch.service.traducao.ConsultaMyMemory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 * Classe de modelo que representa uma série.
 * Mapeada para a tabela "series" no banco de dados.
 */
@Entity
@Table(name = "series")
public class Serie {

	// O `id` é a chave primária da tabela.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// O título deve ser único na tabela.
	@Column(unique = true)
	private String titulo;
	@Column
	private Integer totalTemporadas;
	@Column
	private Double avaliacao;

	private Categoria genero;

	private String atores;

	private String poster;

	private String sinopse;

	// O `episodios` não será persistido no banco de dados.
	//@Transient
	
	@OneToMany(mappedBy = "serie")
	private List<Episodio> episodios = new ArrayList<Episodio>();
	

	/**
	 * Construtor que recebe dados da API para criar um objeto Serie.
	 * * @param dadosSerie Objeto DadosSerie com as informações da API.
	 */
	public Serie(DadosSerie dadosSerie) {

		this.titulo = dadosSerie.titulo();
		this.totalTemporadas = dadosSerie.totalTemporadas();
		// Converte a string de avaliação para um Double, usando 0 se não for um número válido.
		this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0.0);
		// Converte a string de gênero para a enum Categoria.
		this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim()).orElse(Categoria.DESCONHECIDO);
		this.atores = dadosSerie.atores();
		this.poster = dadosSerie.poster();
		// Traduz a sinopse usando o serviço de tradução.
		this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse().split(",")[0].trim())
				.orElse("Sinopse não disponível.");
	}

	// Construtor padrão exigido pelo JPA.
	public Serie() {}

	// Getters e Setters para cada atributo da classe.

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

	public Integer getTotalTemporadas() {
		return totalTemporadas;
	}

	public void setTotalTemporadas(Integer totalTemporadas) {
		this.totalTemporadas = totalTemporadas;
	}

	public Double getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Double avaliacao) {
		this.avaliacao = avaliacao;
	}

	public Categoria getGenero() {
		return genero;
	}

	public void setGenero(Categoria genero) {
		this.genero = genero;
	}

	public String getAtores() {
		return atores;
	}

	public void setAtores(String atores) {
		this.atores = atores;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}
	
	public List<Episodio> getEpisodios() {
		return episodios;
	}

	public void setEpisodios(List<Episodio> episodios) {
		this.episodios = episodios;
	}

	/**
	 * Sobrescreve o método toString para fornecer uma representação textual da série.
	 * * @return Uma string formatada com os dados da série.
	 */
	@Override
	public String toString() {
		return "Série: " + titulo + "\nTotal de Temporadas: " + totalTemporadas + "\nAvaliação: " + avaliacao
				+ "\nGênero: " + genero + "\nAtores: " + atores + "\nPoster: " + poster + "\nSinopse: " + sinopse
				+ "\n";
	}
}
