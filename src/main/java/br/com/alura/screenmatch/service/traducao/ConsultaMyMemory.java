package br.com.alura.screenmatch.service.traducao;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.screenmatch.service.ConsumoApi;

public class ConsultaMyMemory {

//    private static final String API_URL = "https://api.mymemory.translated.net/get?q=%s&langpair=%s";
	private static final String API_URL = System.getenv("URL_IA");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Optional<String> obterTraducao(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }

        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String encodedLangPair = URLEncoder.encode("en|pt-br", StandardCharsets.UTF_8);
            String url = String.format(API_URL, encodedText, encodedLangPair);

            String json = new ConsumoApi().obterDados(url);
            DadosTraducao traducao = MAPPER.readValue(json, DadosTraducao.class);

            if (traducao != null && traducao.dadosResposta() != null) {
                return Optional.ofNullable(traducao.dadosResposta().textoTraduzido());
            }

        } catch (Exception e) {
            // Registre o erro para depuração, mas não jogue uma RuntimeException genérica
            System.err.println("Erro ao traduzir: " + e.getMessage());
        }

        return Optional.empty();
    }
}

