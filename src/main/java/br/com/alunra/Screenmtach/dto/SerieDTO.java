package br.com.alunra.Screenmtach.dto;

import br.com.alunra.Screenmtach.model.Categoria;

public record SerieDTO(
        Long id,

        String titulo,

        Integer totalTemporada,

        Double avaliacao,

        Categoria genero,

        String atores,

        String poster,

        String sinopse) {
}
