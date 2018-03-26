package com.fbuni.compiladores.model;

public class Token {

	private String nomeToken;
	private Integer codToken;

	public Token() {
		// TODO Auto-generated constructor stub
	}

	public Integer getCodToken() {
		return codToken;
	}

	public void setCodToken(Integer codToken) {
		this.codToken = codToken;
	}

	public String getNomeToken() {
		return nomeToken;
	}

	public void setNomeToken(String nomeToken) {
		this.nomeToken = nomeToken;
	}

	public String getTokenFormatado() {
		return "<" + this.nomeToken + "," + this.codToken + ">";
	}

}
