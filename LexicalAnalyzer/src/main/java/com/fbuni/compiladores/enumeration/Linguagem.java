package com.fbuni.compiladores.enumeration;

public enum Linguagem {
    JS("javascript"), JAVA("java"), CSharp("C#");

    private String key;

    private Linguagem(String key) {
	this.key = key;
    }

    public String getNome() {
	return key;
    }

}
