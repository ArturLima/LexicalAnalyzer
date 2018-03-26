package com.fbuni.compiladores.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fbuni.compiladores.model.LinguagemAlvo;
import com.fbuni.compiladores.servico.AnalisadorLexicoServico;

@Controller
@RequestMapping(value = "/fbuni/analisador-lexico")
public class AnalisadorLexicoController {

	@Autowired
	AnalisadorLexicoServico servico;

	@RequestMapping(value = "/")
	public String principal() {
		return "principal";
	}

	@RequestMapping(value = "analisar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> analisar(@RequestBody LinguagemAlvo linguagemAlvo) throws Exception {

		Map<String, Object> retorno = new HashMap<String, Object>();

		try {
			retorno.put("tabela", servico.analisar(linguagemAlvo));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return retorno;
	}

}
