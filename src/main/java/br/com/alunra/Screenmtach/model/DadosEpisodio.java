package br.com.alunra.Screenmtach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(

       @JsonAlias("Titles") String titulo,

        @JsonAlias("Episode") Integer numero,

       @JsonAlias("imdbRating") String avaliacao,

       @JsonAlias("Released") String dataLancamento) {
}
