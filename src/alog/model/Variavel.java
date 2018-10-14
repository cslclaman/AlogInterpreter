/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

import java.util.Objects;

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
    
    public void setValorCaracter(String valor) {
        setValor("\"" + valor + "\"");
    }
    
    public String getValor() {
        return valor;
    }
    
    public long getValorInteiro() {
        if (inicializada && tipo == TipoDado.INTEIRO){
            try {
                return Long.parseLong(valor);
            } catch (NumberFormatException e) {
                return 0L;
            }
        } else {
            return 0L;
        }
    }
    
    public double getValorReal() {
        if (inicializada && tipo == TipoDado.REAL){
            try {
                return Double.parseDouble(valor);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        } else {
            return 0.0;
        }
    }
    
    public String getValorCaracter() {
        if (inicializada && tipo == TipoDado.CARACTER){
            return valor.replace("\"", "");
        } else {
            return "";
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.tipo);
        hash = 83 * hash + Objects.hashCode(this.nome);
        return hash;
    }

}
