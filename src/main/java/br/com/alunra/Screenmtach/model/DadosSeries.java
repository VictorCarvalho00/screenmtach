package br.com.alunra.Screenmtach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSeries(

        @JsonAlias("Title") String titulo,

        @JsonAlias("totalSeasons") Integer totalTemporada,

        @JsonAlias("imdbRating") String avaliacao,

        @JsonAlias ("Genre") String genero,

        @JsonAlias("Actors") String atores,

        @JsonAlias("Poster") String poster,

        @JsonAlias("plot") String sinopse) {
}
