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
    
    public Token executaOperacaoAritmetica(Variavel op1, Variavel op2){
        Token resultToken = null;
        if (defineOperacao(op1, op2)){
            String result = "";
        
            switch (operador.getFuncaoToken()){
                case OP_SOMA:
                    switch (funcao){
                        case CONST_INTEIRA:
                            result = String.valueOf(op1.getValorInteiro() + op2.getValorInteiro());
                            break;
                        case CONST_REAL:
                            result = String.valueOf(op1.getValorReal()+ op2.getValorReal());
                            break;
                    }
                    break;
                case OP_SUBTRACAO:
                    switch (funcao){
                        case CONST_INTEIRA:
                            result = String.valueOf(op1.getValorInteiro() - op2.getValorInteiro());
                            break;
                        case CONST_REAL:
                            result = String.valueOf(op1.getValorReal() - op2.getValorReal());
                            break;
                    }
                    break;
                case OP_MULTIPLICACAO:
                    switch (funcao){
                        case CONST_INTEIRA:
                            result = String.valueOf(op1.getValorInteiro() * op2.getValorInteiro());
                            break;
                        case CONST_REAL:
                            result = String.valueOf(op1.getValorReal() * op2.getValorReal());
                            break;
                    }
                    break;
                case OP_DIV_INTEIRA:
                    if (op2.getValorInteiro() == 0){
                        System.err.println("Erro: Divisão por Zero");
                        return null;
                    }
                    result = String.valueOf(op1.getValorInteiro() / op2.getValorInteiro());
                    break;
                case OP_MOD:
                    if (op2.getValorInteiro() == 0){
                        System.err.println("Erro: Divisão por Zero");
                        return null;
                    }
                    result = String.valueOf(op1.getValorInteiro() % op2.getValorInteiro());
                    break;
                case OP_DIV_REAL:
                    if (op2.getValorReal() == 0.0){
                        System.err.println("Erro: Divisão por Zero");
                        return null;
                    }
                    result = String.valueOf(op1.getValorReal() / op2.getValorReal());
                    break;
            }
            if (result.isEmpty()){
                System.err.println("Erro ao calcular - sem resultado");
            } else {
                resultToken = new Token();
                resultToken.setLinha(operador.getLinha());
                resultToken.setColuna(operador.getColuna());
                resultToken.setOrdem(operador.getOrdem());
                resultToken.setPosicao(operador.getPosicao());
                resultToken.setPalavra(result);
                resultToken.setFuncaoToken(funcao);
            }
        }
        return resultToken;
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
            case OP_DIV_INTEIRA:
            case OP_MOD:
                if (op1.getTipo() == TipoVariavel.CARACTER || op2.getTipo() == TipoVariavel.CARACTER){
                    System.err.println("Operação " + operador.getFuncaoToken()+ " inválida para tipo de dado Caracter");
                    return false;
                } else {
                    if (op1.getTipo() == TipoVariavel.INTEIRO && op2.getTipo() == op1.getTipo()){
                        funcao = FuncaoToken.CONST_INTEIRA;
                    } else {
                        System.err.println("Operação " + operador.getFuncaoToken()+ " inválida para tipo de dado Real");
                        return false;
                    }
                }
                break;
            case OP_DIV_REAL:
                if (op1.getTipo() == TipoVariavel.CARACTER || op2.getTipo() == TipoVariavel.CARACTER){
                    System.err.println("Operação " + operador.getFuncaoToken()+ " inválida para tipo de dado Caracter");
                    return false;
                } else {
                    funcao = FuncaoToken.CONST_REAL;
                }
                break;
        }
        if (!op1.isInicializada()){
            System.err.println("Variável " + op1.getNome() + " não inicializada");
            return false;
        } 
        if (!op2.isInicializada()){
            System.err.println("Variável " + op2.getNome() + " não inicializada");
            return false;
        }
        return true;
    }
}
