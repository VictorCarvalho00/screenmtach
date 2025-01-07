package br.com.alunra.Screenmtach.principal;

import br.com.alunra.Screenmtach.model.*;
import br.com.alunra.Screenmtach.repository.SerieRepository;
import br.com.alunra.Screenmtach.service.ConsumoApi;
import br.com.alunra.Screenmtach.service.ConverteDados;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final Logger log = LoggerFactory.getLogger(Principal.class);
    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KAY = "&apikey=6585022c";

    private  List<DadosSeries> dadosSeries = new ArrayList<>();

    private SerieRepository serieRepository;

    private List <Serie> series = new ArrayList<>();

    private Optional <Serie> serieBusca;

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void  exibeMenu() {

        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar Series
                    2 - Buscar Episodios
                    3 - Listar Series buscas
                    4 - Buscar serie por titulo
                    5 - Buscar serie por ator
                    6 - Top 5 series
                    7 - Buscar serie categoria
                    8 - Filtrar Series
                    9 - Buscar episodio por trecho
                    10 - Top 5 episodios
                    11 -  Buscar episodios a partir de uma data
                    
                    0 - sair
                    """;

            System.out.println(menu);
           opcao = Integer.parseInt(leitura.nextLine());

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSeries();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePortitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSerieCategoria();
                    break;
                case 8:
                    BuscarSerieFiltrada();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodisoPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSeries dados = getDadosSeries();
        Serie serie = new Serie(dados);
        serieRepository.save(serie);
        System.out.println(dados);
    }

    private DadosSeries getDadosSeries() {
        System.out.println("Digite o nome da serie para buscar: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KAY);
        DadosSeries dados = conversor.obterDados(json, DadosSeries.class);
        return dados;
    }

    private void buscarEpisodioPorSeries(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()){

            var serieEcontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 0; i <= serieEcontrada.getTotalTemporada(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEcontrada.getTitulo().replace(" ", "+")+ "&season=" + i + API_KAY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEcontrada.setEpisodios(episodios);
            serieRepository.save(serieEcontrada);
        } else {
            System.out.println("Serie nao encontrada.");
        }
    }

    private void listarSeriesBuscadas() {
         series = serieRepository.findAll();
         series.stream().
                 sorted(Comparator.comparing(Serie::getGenero))
                 .forEach(System.out::println);
    }

    private void buscarSeriePortitulo() {
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();
          serieBusca= serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()){
            System.out.println("Dados da serie: " + serieBusca.get());
        } else {
            System.out.println("Serie nao encotrada. ");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator que deseja buscar: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliaçoes apartir de que valor? ");
        var avaliacao = leitura.nextDouble();
        //Digite a avaliação no formato X.X
        List<Serie> seriesEncontradas = serieRepository. findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Series em que " + nomeAtor + "trabalhou: ");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + "avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> serietop = serieRepository.findTop5OByOrderByAvaliacaoDesc();
    }

    private void buscarSerieCategoria() {
        System.out.println("deseja buscar series de que categiroa/genero? ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.FromPortugues(nomeGenero);
        List<Serie> seriesporCategoria = serieRepository.findBygenero(categoria);
        System.out.println("Series da categoria "+ nomeGenero);
        seriesporCategoria.forEach(System.out::println);
    }

    private void BuscarSerieFiltrada() {
        System.out.println("Filtrar series ate quantas temporadas? ");
        var totalTemporada = leitura.nextLine();
        System.out.println("Com avaliaçoes apartir de que valor? ");
        var avaliacao = leitura.nextDouble();
        List<Serie> filtroSeries = serieRepository.seriesPorTemporadaEAvaliacao(Integer.parseInt(totalTemporada),avaliacao);
        System.out.println("*** Series Filtradas ***");
        filtroSeries.forEach(s -> System.out.println(s.getTitulo() + " -avaliacao: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual nome do episodio para busca? ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = serieRepository.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie : %s Temporada %s - Episodios %s - %s\n",
                e.getSerie()
                        .getTitulo(),
                e.getTemporada(),
                e.getNumeroEpisodio(),
                e.getTitulo()));
    }

    private void topEpisodisoPorSerie() {
        buscarSeriePortitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio>  topEpisodios = serieRepository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie : %s Temporada %s - Episodios %s - %s\n",
                    e.getSerie()
                            .getTitulo(),
                    e.getTemporada(),
                    e.getNumeroEpisodio(),
                    e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriePortitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento: ");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodioAno = serieRepository.episodiosPorSerieEAno(serie, anoLancamento);
            episodioAno.forEach(System.out::println);
        }
    }
}
