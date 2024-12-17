package br.com.alunra.Screenmtach.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto){
        OpenAiService service = new OpenAiService("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\n");

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o portuges o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var reposta = service.createCompletion(requisicao);
        return reposta.getChoices().get(0).getText();
    }
}
