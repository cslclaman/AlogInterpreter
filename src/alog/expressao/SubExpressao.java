/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.expressao;

import alog.token.Token;

/**
 *
 * @author Caique Souza
 */
public class SubExpressao extends Expressao {
    
    private Expressao expressaoEsq;
    private Expressao expressaoDir;
    private Token operador;

    public SubExpressao() {
        super();
        expressaoEsq = null;
        expressaoDir = null;
    }

    public Expressao getExpressaoEsq() {
        return expressaoEsq;
    }

    public void setExpressaoEsq(Expressao expressaoEsq) {
        this.expressaoEsq = expressaoEsq;
    }

    public Expressao getExpressaoDir() {
        return expressaoDir;
    }

    public void setExpressaoDir(Expressao expressaoDir) {
        this.expressaoDir = expressaoDir;
    }

    public Token getOperador() {
        return operador;
    }

    public void setOperador(Token operador) {
        this.operador = operador;
        switch (operador.getFuncaoToken()) {
            case OP_MAT_SOMA:
            case OP_MAT_SUBTRACAO:
            case OP_MAT_MULTIPLICACAO:
            case OP_MAT_DIV_REAL:
            case OP_MAT_DIV_INTEIRA:
            case OP_MAT_MOD:
                tipoExpressao = TipoExpressao.ARITMETICA;
                break;
            case OP_REL_MAIOR:
            case OP_REL_MAIOR_IGUAL:
            case OP_REL_MENOR:
            case OP_REL_MENOR_IGUAL:
            case OP_REL_IGUAL:
            case OP_REL_DIFERENTE:
                tipoExpressao = TipoExpressao.RELACIONAL;
                break;
            case OP_LOG_E:
            case OP_LOG_OU:
                tipoExpressao = TipoExpressao.LOGICA;
                break;
            default:
                tipoExpressao = TipoExpressao._INDEFINIDO;
                break;
        }
    }

    @Override
    public String toString() {
        return expressaoEsq.toString() + " "
                + operador.getPalavra() + " "
                + expressaoDir.toString();
    }
    
}
