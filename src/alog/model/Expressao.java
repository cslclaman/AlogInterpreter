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
    private TipoExpressao tipo;
    private ArrayList<Token> tokens;
    
    public Expressao(){
        tokens = new ArrayList<>();
        tipo = TipoExpressao._INDEFINIDO;
    }
    
    public void addToken(Token token){
        tokens.add(token);
    }
    
    public Token getLastToken(){
        return tokens.get(tokens.size() - 1);
    }
    
    public void removeToken(Token token){
        tokens.remove(token);
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
