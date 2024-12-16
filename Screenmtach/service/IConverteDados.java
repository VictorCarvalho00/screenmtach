package br.com.alunra.Screenmtach.service;

public interface IConverteDados {

    <T>  T obterDados(String json, Class <T> classe);
}
