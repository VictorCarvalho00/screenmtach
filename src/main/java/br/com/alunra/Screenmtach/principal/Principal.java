package br.com.alunra.Screenmtach.principal;

import br.com.alunra.Screenmtach.model.DadosEpisodio;
import br.com.alunra.Screenmtach.model.DadosSeries;
import br.com.alunra.Screenmtach.model.DadosTemporada;
import br.com.alunra.Screenmtach.model.Episodio;
import br.com.alunra.Screenmtach.service.ConsumoApi;
import br.com.alunra.Screenmtach.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();

    //"https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=6585022c"
    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KAY = "&apikey=6585022c";

    public void  exibeMenu(){

        System.out.println("Digite o nome da seire para buscar: ");

        var nomeSerie =  leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KAY);

        DadosSeries dados = conversor.obterDados(json, DadosSeries.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for(int i = 1; i <= dados.totalTemporada(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "$season=" + i + API_KAY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(System.out::println);

        for(int i = 0; i  < dados.totalTemporada(); i++){
            List<DadosEpisodio> episodioTemporada = temporadas.get(i).episodios();
            for(int j = 0; j < episodioTemporada.size(); j++){
                System.out.println(episodioTemporada.get(j).titulo());
            }
        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episodios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println("Ordenação " + e))
                .limit(5)
                .peek(e -> System.out.println("Limite " + e))
                .map(e-> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Mapeamento " + e))
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        var  trechoTitulo = leitura.nextLine();
        System.out.println("Digite um trecho do titulo do episodio que deseja buscar: ");
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if(episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado. ");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episodio nao encontrado. ");
        }

        System.out.println("Apartir de que ano voce deseja ver os episodios? ");
        var ano = leitura.nextLine();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(Integer.parseInt(ano),1,1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Data de lançamento: " + e.getDataLancamento().format(formatador)));

        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Media: " + est.getAverage());
        System.out.println("Melhor episodio: " + est.getMax());
        System.out.println("Pior episodio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }
}
