package br.com.alunra.Screenmtach;

import br.com.alunra.Screenmtach.model.DadosSeries;
import br.com.alunra.Screenmtach.service.ConsumoApi;
import br.com.alunra.Screenmtach.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmtachApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmtachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSeries dados = conversor.obterDados(json, DadosSeries.class);
		System.out.println(dados);

	}
}
