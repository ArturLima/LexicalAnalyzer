package com.fbuni.compiladores.servico;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fbuni.compiladores.model.Classificacao;
import com.fbuni.compiladores.model.LinguagemAlvo;

@Service
public interface AnalisadorLexicoServico {

    public List<Classificacao> analisar(LinguagemAlvo linguagemAlvo) throws Exception;

}
