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
    private TipoVariavel tipo;
    private String nome;
    //Substituir por implementação de subclasses
    private String valor;
    private boolean inicializada;

    public Variavel(TipoVariavel tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
        this.valor = "";
        this.inicializada = false;
    }
    
    public boolean isInicializada(){
        return inicializada;
    }
    
    public String getNome(){
        return nome;
    }

    public TipoVariavel getTipo() {
        return tipo;
    }

    public void setValor(String valor) {
        this.valor = valor;
        this.inicializada = true;
    }

    public String getValor() {
        return valor;
    }
    
    public int getValorInteiro() {
        if (!inicializada || tipo != TipoVariavel.INTEIRO){
            return 0;
        } else {
            return Integer.parseInt(valor);
        }
    }
    
    public double getValorReal() {
        if (!inicializada || tipo == TipoVariavel.CARACTER){
            return 0.0;
        } else {
            return Double.parseDouble(valor);
        }
    }
    
    @Override
    public String toString(){
        return tipo + ": " + nome;
    }
}
