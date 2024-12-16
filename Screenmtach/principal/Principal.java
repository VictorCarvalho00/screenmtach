package br.com.alunra.Screenmtach.principal;

import br.com.alunra.Screenmtach.model.DadosSeries;
import br.com.alunra.Screenmtach.model.DadosTemporada;
import br.com.alunra.Screenmtach.service.ConsumoApi;
import br.com.alunra.Screenmtach.service.ConverteDados;
import java.util.*;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KAY = "&apikey=6585022c";
    private  List<DadosSeries> dadosSeries = new ArrayList<>();

    public void  exibeMenu() {

        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar Series
                    2 - Bsucar Episodios
                    3- Listar Series buscas
                    
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
        System.out.println(dados);
        dadosSeries.add(dados);
    }

    private DadosSeries getDadosSeries() {
        System.out.println("Digite o nome da serie para buscar: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KAY);
        DadosSeries dados = conversor.obterDados(json, DadosSeries.class);
        return dados;
    }

    private void buscarEpisodioPorSeries(){

        DadosSeries dadosSeries = getDadosSeries();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 0; i <= dadosSeries.totalTemporada(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSeries.titulo().replace(" ", "+")+ "&season=" + i + API_KAY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
        dadosSeries.forEach(System.out::println);
    }
}
