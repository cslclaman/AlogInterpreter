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
    }
    
    public void addToken(Token token){
        tokens.add(token);
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
    
    
}