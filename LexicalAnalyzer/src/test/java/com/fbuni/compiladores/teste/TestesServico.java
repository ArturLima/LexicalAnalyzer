package com.fbuni.compiladores.teste;

import org.junit.Test;

import com.fbuni.compiladores.servico.AnalisadorLexicoServicoImpl;

public class TestesServico {

	AnalisadorLexicoServicoImpl servico;

	@Test
	public void testeObterPalavrasLinhas() {

		// servico = new AnalisadorLexicoServicoImpl();
		// String linha = "console.log(\"hello word\", \"teste\")";
		//
		// List<Lexema> palavras = servico.obterPalavrasDaLinha(linha);
		// //
		// // // remove espaço em branco dentro colchete
		// // linha.replaceAll("\\s+(?=[^()]*\\))", "");
		// //
		// // linha.replaceAll(" ", "|");
		// //
		// // // remove espaço em branco dentro de espaços
		// // linha.replaceAll("$1", " ");
		//
		// // String[] palavras = linha.split("\"?( |$)(?=(([^\"]*\"){2})*[^\"]*$)\"?");
		// // String[] palavras = linha.split(" ");
		//
		// for (Lexema lexema : palavras) {
		// System.out.println(lexema.getPalavra() + "\n");
		//
		// }

	}

	@Test
	public void testeRegexLiteral() {

		String regex_string_aberta_fechada = "\\\"(\\\\.|[^\\\"])*\\\"";

		System.out.println("\"hello word\"".matches(regex_string_aberta_fechada));

	}

}
