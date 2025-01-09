package br.com.alunra.Screenmtach.repository;

import br.com.alunra.Screenmtach.model.Categoria;
import br.com.alunra.Screenmtach.model.Episodio;
import br.com.alunra.Screenmtach.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository  extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5OByOrderByAvaliacaoDesc();

    List<Serie> findBygenero(Categoria categoria);

    List<Serie> findByTotalTemporadaLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporada, double avaliacao);

    @Query
            ("""
             SELECT s FROM Serie s
             WHERE 
             s.totalTemporada <= :totaltemporada
             AND 
             s.avaliacao >= :avaliacao
             """)
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporada, double avaliacao);

    @Query
            ("""
             SELECT e FROM Serie s
             JOIN
             s.episodios e
             WHERE
             e.titulo ILIKE %:trechoEpisodio%
             """)
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("""
            SELECT e FROM Serie s
            JOIN s.episodios e
            WHERE
            s = :serie
            ORDER BY e.avaliacao DESC
            LIMIT 5
            """)
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("""
            SELECT e FROM Serie s
            JOIN s.episodios e
            WHERE
            s = :serie
            AND
            YEAR (e.dataLancamento) >= :anoLancamento
            """)
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

    @Query("""
            SELECT s FROM Serie s  
            JOIN s.episodios e  
            GROUP BY s 
            ORDER BY MAX(e.dataLancamento) DESC LIMIT 5
            """)
    List<Serie> encontrarEpisodiosMaisRecentes();

@Query("""
        SELECT e fROM Serie s 
        JOIN s.episodios e
        WHERE s.id = :id
        AND
        e.temporada = :numero
        """)
    List<Episodio> obterEpisodiosPorTemporada(Long id, Long numero);
}
