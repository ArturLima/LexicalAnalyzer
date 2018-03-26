package com.fbuni.compiladores.model;

public class Lexema {

	private String palavra;
	private Integer linha;
	private Integer colunaFinal;
	private Integer colunaInicial;
	private Padrao padrao;

	public Lexema(String nomeLexema) {
		this.palavra = nomeLexema;
	}

	public Lexema(String nomeLexema, Integer linha, Integer colunaInicial, Integer colunaFinal) {
		this.palavra = nomeLexema;
		this.linha = linha;
		this.colunaInicial = colunaInicial;
		this.colunaFinal = colunaFinal;
	}

	public Lexema(String nomeLexema, Integer linha, Integer colunaInicial) {
		this.palavra = nomeLexema;
		this.linha = linha;
		this.colunaInicial = colunaInicial;
	}

	public Padrao getPadrao() {
		return padrao;
	}

	public void setPadrao(Padrao padrao) {
		this.padrao = padrao;
	}

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public Integer getColunaFinal() {
		return colunaFinal;
	}

	public void setColunaFinal(Integer colunaFinal) {
		this.colunaFinal = colunaFinal;
	}

	public Integer getColunaInicial() {
		return colunaInicial;
	}

	public void setColunaInicial(Integer colunaInicial) {
		this.colunaInicial = colunaInicial;
	}

}
