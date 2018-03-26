package com.fbuni.compiladores.model;

import com.fbuni.compiladores.enumeration.ExpressaoRegular;

public class Padrao {

	ExpressaoRegular expressao;
	String descricao;

	public Padrao() {
	}

	public Padrao(ExpressaoRegular expressao) {
		this.setExpressao(expressao);
	}

	public ExpressaoRegular getExpressao() {
		return expressao;
	}

	public void setExpressao(ExpressaoRegular expressao) {

		this.expressao = expressao;

		if (expressao.equals(ExpressaoRegular.ABERTURA_FUNCAO_ESCOPO_INDEXACAO)) {
			this.descricao = "Indica a abertura de função, escopo ou indexação";
		} else if (expressao.equals(ExpressaoRegular.FECHAMENTO_FUNCAO_ESCOPO_INDEXACAO)) {
			this.descricao = "Indica o fechamento de função, escopo ou indexação";
		} else if (expressao.equals(ExpressaoRegular.ID)) {
			this.descricao = "Identificador de função, variáveis";
		} else if (expressao.equals(ExpressaoRegular.LITERAL)) {
			this.descricao = "Identificador de String entre aspas";
		} else if (expressao.equals(ExpressaoRegular.OPERADORES)) {
			this.descricao = "Identificação de operadores";
		} else if (expressao.equals(ExpressaoRegular.PALAVRA_RESERVADA)) {
			this.descricao = "Palavra reservada da linguagem";
		} else if (expressao.equals(ExpressaoRegular.NUMERICO)) {
			this.descricao = "Números inteiros e reais";
		}
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
