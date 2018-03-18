/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Expressao;
import alog.model.TipoVariavel;
import alog.model.Token;
import alog.model.Variavel;
import java.util.LinkedList;

/**
 * Classe que recebe uma expressão e executa.
 * @author Caique
 */
public class Interpreter {
    private int bloco;
    private LinkedList<Variavel> listaVariaveis;
    
    public Interpreter(){
        bloco = 0;
        listaVariaveis = new LinkedList<>();
    }
    
    public void reseta(){
        bloco = 0;
        listaVariaveis = new LinkedList<>();
    }
    
    public void executa(Expressao expressao){
        switch(expressao.getTipo()){
            case DELIM_BLOCO:
                switch (expressao.getTokenAt(0).getFuncaoToken()){
                    case RES_BLOCO_INICIO:
                        bloco ++;
                        break;
                    case RES_BLOCO_FIM:
                        bloco --;
                        break;
                }
                break;
            case CRIACAO_VARIAVEL:
                TipoVariavel tipoVar;
                switch (expressao.getTokenAt(0).getFuncaoToken()){
                    case IDENT_TIPO_INTEIRO:
                        tipoVar = TipoVariavel.INTEIRO;
                        break;
                    case IDENT_TIPO_REAL:
                        tipoVar = TipoVariavel.REAL;
                        break;
                    case IDENT_TIPO_CARACTER:
                    default:
                        tipoVar = TipoVariavel.CARACTER;
                        break;
                }
                for (int it = 1; it < expressao.getNumTokens(); it++){
                    Token token = expressao.getTokenAt(it);
                    Variavel variavel = new Variavel(tipoVar, token.getPalavra());
                    listaVariaveis.add(variavel);
                }
                break;
        }
    }
}
