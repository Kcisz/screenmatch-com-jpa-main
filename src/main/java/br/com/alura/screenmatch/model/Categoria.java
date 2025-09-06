package br.com.alura.screenmatch.model;

import java.util.Optional;

public enum Categoria {

	ACAO("Action"), ROMANCE("Romance"), CRIME("Crime"), COMEDIA("Comedy"), DRAMA("Drama"), DESCONHECIDO("");

	private String categoriaOmdb;

	Categoria(String categoriaOmdb) {
		this.categoriaOmdb = categoriaOmdb;

	}

	public static Optional<Categoria>  fromString(String text) {
		for (Categoria categoria : Categoria.values()) {
			if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
				return Optional.of(categoria);
			}
		}
		//throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
		  return Optional.empty();
	}

}
