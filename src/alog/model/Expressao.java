/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

import java.util.ArrayList;

/**
 * Uma expressão aqui é uma sequência de Tokens que pode ser executada.
 * @author Caique
 */
public class Expressao {
    private String texto;
    private TipoExpressao tipo;
    private ArrayList<Token> tokens;
    private int indice;
    private int linha;
    
    public Expressao(){
        tokens = new ArrayList<>();
        tipo = TipoExpressao._INDEFINIDO;
        indice = 0;
        linha = 0;
        texto = "";
    }
    
    public int getLinha(){
        return linha;
    }
    
    public void setLinha(int linha){
        this.linha = linha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public void atualizaTexto(String sub){
        if (texto.isEmpty()){
            texto = sub;
        } else {
            texto += " " + sub;
        }
    }
    
    public boolean hasNext(){
        return indice < tokens.size();
    }
    
    public Token getNext(){
        if (hasNext()){
            return tokens.get(indice++);
        } else {
            return null;
        }
    }
    
    public int getIndice(){
        return indice;
    }
    
    public void setIndice(int indice){
        this.indice = indice;
    }
    
    public int getNumTokens(){
        return tokens.size();
    }
    
    public void addToken(Token token){
        tokens.add(token);
    }
    
    public Token getTokenAt(int i){
        return tokens.get(i);
    }
    
    public void setTokenAt(int i, Token token){
        tokens.set(i, token);
    }
    
    public void removeToken(Token token){
        tokens.remove(token);
    }

    public ArrayList<Token> listTokens(){
        return tokens;
    }
    
    public TipoExpressao getTipo() {
        return tipo;
    }

    public void setTipo(TipoExpressao tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        String str = tipo + " -";
        for (Token t : tokens){
            str += " " + t.getPalavra();
        }
        return str;
    }
    
    
}
