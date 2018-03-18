/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Expressao;
import alog.model.Token;
import alog.model.Variavel;
import java.util.ArrayList;

/**
 * Analisador sintático que verifica uma sequência de tokens e retorna expressões executáveis.
 * @author Caique
 */
public class Parser {
    private ArrayList<Token> tokens;
    private ArrayList<Variavel> variaveis;
    private ArrayList<Expressao> expressoes;
    private int linha;
    private int pos;
    
    public Parser (String texto){
        Scanner scanner = new Scanner(texto);
        tokens = scanner.getAll();
        variaveis = new ArrayList<>();
        expressoes = new ArrayList<>();
        pos = 0;
        linha = tokens.get(pos).getLinha();
    }
    
    public boolean parseExpression(){
        Expressao expr = new Expressao();
        boolean go = true;
        
        Token token = tokens.get(pos);
        while (go){
            
            if (linha == token.getLinha()){
                pos++;
            } else {
                go = false;
            }
            
            
        }
        
        //Temporário só pra não dar erro
        return true;
    }

}
