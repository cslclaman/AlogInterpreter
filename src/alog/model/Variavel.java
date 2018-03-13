/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

/**
 *
 * @author Caique Souza
 */
public class Variavel {
    private Tipo tipo;
    private String nome;
    //Substituir por implementação de subclasses
    private String valor;

    public Variavel(Tipo tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
        this.valor = "";
    }
    
    public String getNome(){
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
    
    
}
