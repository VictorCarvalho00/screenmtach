package br.com.alunra.Screenmtach.model;

import java.util.OptionalDouble;

public class Serie {

    private String titulo;
    private Integer totalTemporada;
    private Double avaliacao;
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

    public Serie(DadosSeries dadosSeries){
        this.titulo = dadosSeries.titulo();
        this.totalTemporada = dadosSeries.totalTemporada();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSeries.avaliacao())).orElse(0);
        this.genero = Categoria.FromString(dadosSeries.genero().split(",")[0].trim());
        this.atores = dadosSeries.atores();
        this.poster = dadosSeries.poster();
        this.sinopse = dadosSeries.sinopse();
    }
}
