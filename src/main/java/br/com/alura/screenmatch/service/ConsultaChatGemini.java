package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGemini {

	public static String obterTraducao(String texto) {
	    // Lê a chave da API da variável de ambiente
	    String apiKey = System.getenv("OPENAI_API_KEY"); // Corrigido aqui!

	    if (apiKey == null || apiKey.isEmpty()) {
	        throw new RuntimeException(
	                "A chave da API da OpenAI não foi definida na variável de ambiente OPENAI_API_KEY.");
	    }

	    OpenAiService service = new OpenAiService(apiKey);

	    CompletionRequest requisicao = CompletionRequest.builder().model("gpt-3.5-turbo-instruct")
	            .prompt("traduza para o português o texto: " + texto).maxTokens(1000).temperature(0.7).build();

	    var resposta = service.createCompletion(requisicao);

	    String traducao = resposta.getChoices().get(0).getText();

	    return traducao.trim();
	
	}
}