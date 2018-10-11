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
    private TipoDado tipo;
    private String nome;
    private String valor;
    private boolean inicializada;

    public Variavel(TipoDado tipo, String nome) {
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

    public TipoDado getTipo() {
        return tipo;
    }

    public void setValor(String valor) {
        this.valor = valor;
        this.inicializada = true;
    }
    
    public void setValorInteiro(long valor) {
        setValor(String.valueOf(valor));
    }
    
    public void setValorReal(double valor) {
        setValor(String.valueOf(valor));
    }

    public String getValor() {
        return valor;
    }
    
    public long getValorInteiro() {
        if (inicializada && tipo == TipoDado.INTEIRO){
            return Long.parseLong(valor);
        } else {
            return 0L;
        }
    }
    
    public double getValorReal() {
        if (inicializada && tipo == TipoDado.REAL){
            return Double.parseDouble(valor);
        } else {
            return 0.0;
        }
    }
    
    @Override
    public String toString(){
        return tipo + ": " + nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Variavel other = (Variavel) obj;
        return this.tipo == other.tipo && this.nome.equalsIgnoreCase(other.nome);
    }
    
    
}
