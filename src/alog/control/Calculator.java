/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.FuncaoToken;
import alog.model.TipoToken;
import alog.model.TipoVariavel;
import alog.model.Token;
import alog.model.Variavel;

/**
 *
 * @author Caique Souza
 */
public class Calculator {
    private Token operador;
    private FuncaoToken funcao;
    
    public Calculator(Token operador){
        this.operador = operador;
        funcao = FuncaoToken._INDEFINIDO;
    }
    
    public Token executaOperacaoBinaria(Variavel op1, Variavel op2){
        if (!defineOperacao(op1, op2)){
            return null;
        } else {
            return operador;
        }
    } 
    
    private boolean defineOperacao(Variavel op1, Variavel op2){
        switch (operador.getFuncaoToken()){
            case OP_SOMA:
            case OP_SUBTRACAO:
            case OP_MULTIPLICACAO:
                if (op1.getTipo() == TipoVariavel.CARACTER || op2.getTipo() == TipoVariavel.CARACTER){
                    System.err.println("Operação " + operador.getFuncaoToken()+ " inválida para tipo de dado Caracter");
                    return false;
                } else {
                    if (op1.getTipo() == TipoVariavel.INTEIRO && op2.getTipo() == op1.getTipo()){
                        funcao = FuncaoToken.CONST_INTEIRA;
                    } else {
                        funcao = FuncaoToken.CONST_REAL;
                    }
                }
                break;
        }
        return true;
    }
}
