package com.fbuni.compiladores.model;

public class Classificacao {

	private Lexema lexema;
	private Token token;

	public Token getToken() {
		return token;
	}

	public Lexema getLexema() {
		return lexema;
	}

	public void setLexema(Lexema lexema) {
		this.lexema = lexema;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Classificacao) {

			Classificacao outro = (Classificacao) obj;

			if (this.lexema.getPalavra().equals(outro.getLexema().getPalavra())
					&& this.token.getNomeToken().equals(outro.getToken().getNomeToken())) {
				return true;
			}
		}
		return false;
	}

}
