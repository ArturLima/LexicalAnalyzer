package com.fbuni.compiladores.servico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fbuni.compiladores.enumeration.ExpressaoRegular;
import com.fbuni.compiladores.enumeration.Linguagem;
import com.fbuni.compiladores.model.Classificacao;
import com.fbuni.compiladores.model.Lexema;
import com.fbuni.compiladores.model.LinguagemAlvo;
import com.fbuni.compiladores.model.Padrao;
import com.fbuni.compiladores.model.Token;

@Service
public class AnalisadorLexicoServicoImpl implements AnalisadorLexicoServico {

	private Integer CODIGO_SIMBOLO;

	@Override
	public List<Classificacao> analisar(LinguagemAlvo linguagemAlvo) throws Exception {

		if (!linguagemAlvo.getNomeLinguagem().equalsIgnoreCase(Linguagem.JS.getNome()))
			throw new Exception("Analisador feito somente para javascript");

		// zera sempre quando for analisar
		CODIGO_SIMBOLO = 0;

		return montaTabelaSimbolosJavaScript(linguagemAlvo);
	}

	private List<Classificacao> montaTabelaSimbolosJavaScript(LinguagemAlvo linguagem) throws Exception {

		List<Classificacao> tabela = new ArrayList<Classificacao>();

		List<Lexema> comentarios = verificaComentarios(linguagem);

		if (!comentarios.isEmpty()) {
			for (Lexema comentario : comentarios) {
				Classificacao classificacaoComentario = new Classificacao();
				classificacaoComentario.setLexema(comentario);
				Token token = new Token();
				token.setCodToken(CODIGO_SIMBOLO++);
				token.setNomeToken("COMENTÁRIO");
				classificacaoComentario.setToken(token);
				tabela.add(classificacaoComentario);
			}
		}

		Integer numerolinha = 1;

		for (String linha : linguagem.getCodigoFonte().split("\n")) {

			List<Lexema> lexemas = obterPalavrasDaLinha(linha, numerolinha++);

			for (Lexema lexema : lexemas) {
				if (!lexema.getPalavra().isEmpty()) {

					int indiceLexemaAtual = lexemas.indexOf(lexema);

					List<Lexema> posterioresLexemasDaLinha = new ArrayList<>();

					for (int i = indiceLexemaAtual + 1; i < lexemas.size(); i++) {
						posterioresLexemasDaLinha.add(lexemas.get(i));
					}

					classificarLexema(lexema, tabela, numerolinha, posterioresLexemasDaLinha);
				}
			}
		}

		return tabela;
	}

	private List<Lexema> verificaComentarios(LinguagemAlvo linguagem) throws Exception {

		String comentarioAbertura = "/*";
		String comentarioFechamento = "*/";
		String comentarioLinha = "//";
		String comentario = "";

		List<Lexema> comentarios = new ArrayList<>();

		boolean existirComentarioDeLinha = linguagem.getCodigoFonte().contains(comentarioLinha);

		if (existirComentarioDeLinha) {

			while (existirComentarioDeLinha) {

				int indexComentarioNormal = linguagem.getCodigoFonte().indexOf(comentarioLinha);

				// final do arquivo
				if (linguagem.getCodigoFonte().indexOf('\n', indexComentarioNormal) == -1) {
					comentario = linguagem.getCodigoFonte().substring(indexComentarioNormal,
							linguagem.getCodigoFonte().length());

					limparComentario(indexComentarioNormal, linguagem.getCodigoFonte().length(), linguagem);

				} else {
					comentario = linguagem.getCodigoFonte().substring(indexComentarioNormal,
							linguagem.getCodigoFonte().indexOf('\n', indexComentarioNormal));

					limparComentario(indexComentarioNormal,
							linguagem.getCodigoFonte().indexOf('\n', indexComentarioNormal), linguagem);
				}

				existirComentarioDeLinha = linguagem.getCodigoFonte().contains(comentarioLinha);

				Lexema lexemaComentarioDeLinha = new Lexema(comentario);
				Padrao padrao = new Padrao();
				padrao.setDescricao("Comentário de LINHA");
				lexemaComentarioDeLinha.setPadrao(padrao);
				comentarios.add(lexemaComentarioDeLinha);

				comentario = "";

			}
		}

		if (linguagem.getCodigoFonte().contains(comentarioAbertura)
				&& !linguagem.getCodigoFonte().contains(comentarioFechamento)) {
			throw new Exception("Comentário aberto não foi fechado");
		} else if (linguagem.getCodigoFonte().contains(comentarioFechamento)
				&& !linguagem.getCodigoFonte().contains(comentarioAbertura)) {
			throw new Exception("Comentário não foi aberto");
		}

		boolean existirComentarioDeEscopo = linguagem.getCodigoFonte().contains(comentarioAbertura)
				&& linguagem.getCodigoFonte().contains(comentarioFechamento);

		if (existirComentarioDeEscopo) {

			while (existirComentarioDeEscopo) {

				int indexComentarioAbertura = linguagem.getCodigoFonte().indexOf(comentarioAbertura);
				int indexComentarioFechamento = linguagem.getCodigoFonte().indexOf(comentarioFechamento);

				if (indexComentarioAbertura > indexComentarioFechamento) {
					throw new Exception("Comentário de fechamento antes do de abertura");
				}

				for (int i = indexComentarioAbertura; i <= indexComentarioFechamento + 1; i++) {
					if (linguagem.getCodigoFonte().charAt(i) != '\n') {
						comentario += linguagem.getCodigoFonte().charAt(i);
						linguagem.setCodigoFonte(replace(linguagem.getCodigoFonte(), i, Character.MIN_VALUE));
					}
				}

				existirComentarioDeEscopo = linguagem.getCodigoFonte().contains(comentarioAbertura)
						&& linguagem.getCodigoFonte().contains(comentarioFechamento);

				if (!comentario.isEmpty()) {
					Lexema lexemaComentarioDeLinha = new Lexema(comentario);
					Padrao padrao = new Padrao();
					padrao.setDescricao("Comentário de BLOCO");
					lexemaComentarioDeLinha.setPadrao(padrao);
					comentarios.add(lexemaComentarioDeLinha);
				}

				comentario = "";
			}
		}

		return comentarios;
	}

	private void classificarLexema(Lexema lexema, List<Classificacao> classificacoes, Integer linha,
			List<Lexema> lexemasPosterioresDaLinha) {

		Classificacao classificacao = new Classificacao();

		Token token = new Token();

		for (ExpressaoRegular expressao : ExpressaoRegular.values()) {

			if (lexema.getPalavra().matches(expressao.getExpressao())) {

				lexema.setPadrao(new Padrao(expressao));
				token.setNomeToken(expressao.toString());
				token.setCodToken(CODIGO_SIMBOLO++);
				classificacao.setLexema(lexema);
				classificacao.setToken(token);

				if (expressao.toString().equals(ExpressaoRegular.NUMERICO.toString())) {

					if (lexema.getPalavra().contains(".")) {
						classificacao.getToken().setNomeToken("PONTO_FLUTUANTE");
					} else {
						classificacao.getToken().setNomeToken("INTEIRO");
					}
				}

				if (expressao.toString().equals(ExpressaoRegular.ID.toString())) {

					if (estaDentroDeFuncao(classificacoes)) {

						if (ehFuncao(lexemasPosterioresDaLinha)) {
							classificacao.getToken().setNomeToken("ID_CHAMADA_FUNCAO");
						} else {
							classificacao.getToken().setNomeToken("ID_VAR_LOCAL");
						}

					} else {

						if (ehFuncao(lexemasPosterioresDaLinha)) {
							classificacao.getToken().setNomeToken("ID_DECLARAÇÃO_FUNCAO");
						}
					}

					if (verificaJaExisteIdentificador(classificacoes, classificacao)) {
						// faz nada
						return;
					}

				}

				if (expressao.toString().equals(ExpressaoRegular.SEPARADORES_LEXEMAS.toString())
						|| expressao.toString().equals(ExpressaoRegular.FIM_DE_LINHA.toString())) {
					return;
				}

				classificacoes.add(classificacao);
				break;
			}
		}

		if (lexema.getPadrao() == null) {
			Padrao padrao = new Padrao();
			padrao.setDescricao("SEM PADRÃO DEFINIDO");
			lexema.setPadrao(padrao);
			token.setNomeToken("ERROR");
			token.setCodToken(-1);
			lexema.setColunaFinal(-1);
			lexema.setColunaInicial(-1);

			classificacao.setToken(token);
			classificacao.setLexema(lexema);
			classificacoes.add(classificacao);
		}
	}

	private boolean ehFuncao(List<Lexema> lexemas) {

		Lexema primeirolexemaPosterior = lexemas.stream().findFirst().get();
		// Lexema segundolexemaPosterior = lexemas.stream().skip(1).findFirst().get();

		return primeirolexemaPosterior.getPalavra().equals("(");
	}

	private boolean verificaJaExisteIdentificador(List<Classificacao> classificacoes, Classificacao classificacao) {

		for (Classificacao classific : classificacoes) {
			if (classificacao.equals(classific)) {
				return true;
			}
		}

		return false;
	}

	// true - é variável local, false - é variável global
	private boolean estaDentroDeFuncao(List<Classificacao> classificacoes) {

		for (int i = classificacoes.size() - 1; i >= 0; i--) {

			if (classificacoes.get(i).getLexema().getPalavra().equals("}")) {
				return false;
			}

			if (classificacoes.get(i).getLexema().getPalavra().equals("(")) {
				return true;
			}
		}

		return false;
	}

	// usa os separadores de lexemas para fazer o split.

	public List<Lexema> obterPalavrasDaLinha(String linha, Integer posicaoLinha) {

		List<Lexema> lexemas = new ArrayList<>();

		String palavraCorrente = "";
		Integer coluna = 1;

		linha += " ";

		for (Character caractere : linha.toCharArray()) {

			if (caractere.toString().matches(ExpressaoRegular.SEPARADORES_LEXEMAS.getExpressao())) {

				if (palavraCorrente.trim().matches(ExpressaoRegular.NUMERICO.getExpressao()) && caractere.equals('.')) {
					// faz nada
				} else {

					if (!palavraCorrente.isEmpty()) {
						lexemas.add(new Lexema(palavraCorrente.trim(), posicaoLinha, coluna - palavraCorrente.length(),
								coluna - 1));
					}

					lexemas.add(new Lexema(caractere.toString(), posicaoLinha, coluna));

					palavraCorrente = "";

					coluna++;

					continue;
				}

			}

			if (!palavraCorrente.contains("\"")) {

				if (caractere == ' ') {
					if (!palavraCorrente.isEmpty()) {
						lexemas.add(new Lexema(palavraCorrente.trim(), posicaoLinha, coluna - palavraCorrente.length(),
								coluna));
					}

					palavraCorrente = "";
				}
			}

			palavraCorrente += caractere.toString();
			coluna++;
		}

		return lexemas;

	}

	private String replace(String str, int index, char replace) {
		if (str == null) {
			return str;
		} else if (index < 0 || index >= str.length()) {
			return str;
		}
		char[] chars = str.toCharArray();
		chars[index] = replace;
		return String.valueOf(chars);
	}

	private void limparComentario(int indexInicial, int indexFinal, LinguagemAlvo linguagem) {
		for (int i = indexInicial; i < indexFinal; i++) {
			linguagem.setCodigoFonte(replace(linguagem.getCodigoFonte(), i, Character.MIN_VALUE));
		}

	}

}
