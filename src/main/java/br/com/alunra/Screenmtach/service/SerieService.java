package br.com.alunra.Screenmtach.service;

import br.com.alunra.Screenmtach.dto.EpisodioDTO;
import br.com.alunra.Screenmtach.dto.SerieDTO;
import br.com.alunra.Screenmtach.model.Categoria;
import br.com.alunra.Screenmtach.model.Serie;
import br.com.alunra.Screenmtach.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;

    private List<SerieDTO> converterDados(List<Serie> series){
        return series.stream().
                map(s -> new SerieDTO(s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporada(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterTodasAsSeries(){
        return converterDados(serieRepository.findAll());
    }

    public List<SerieDTO> obterTop5Series(){
        return converterDados(serieRepository.findTop5OByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos(){
        return converterDados(serieRepository.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporada(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse());
        }
        return  null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return  null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return  serieRepository.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriesPorCategoria(String nomeGenero) {
        Categoria categoria = Categoria.FromPortugues(nomeGenero);
        return converterDados(serieRepository.findBygenero(categoria));

    }
}
