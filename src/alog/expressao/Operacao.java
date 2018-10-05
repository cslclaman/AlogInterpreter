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
public class Operacao extends Expressao {
    
    private Expressao expressaoEsq;
    private Expressao expressaoDir;
    private Token operador;

    public Operacao() {
        super();
        expressaoEsq = null;
        expressaoDir = null;
    }

    public Expressao getExpressaoEsq() {
        return expressaoEsq;
    }

    public void setExpressaoEsq(Expressao expressaoEsq) {
        this.expressaoEsq = expressaoEsq;
        defineTexto();
    }

    public Expressao getExpressaoDir() {
        return expressaoDir;
    }

    public void setExpressaoDir(Expressao expressaoDir) {
        this.expressaoDir = expressaoDir;
        defineTexto();
    }

    public Token getOperador() {
        return operador;
    }

    public void setOperador(Token operador) {
        this.operador = operador;
        defineTexto();
        switch (operador.getFuncaoToken()) {
            case OP_MAT_SOMA:
            case OP_MAT_SUBTRACAO:
            case OP_MAT_MULTIPLICACAO:
            case OP_MAT_DIV_REAL:
            case OP_MAT_DIV_INTEIRA:
            case OP_MAT_MOD:
                tipoExpressao = TipoExpressao.OPERACAO_ARITMETICA;
                break;
            case OP_REL_MAIOR:
            case OP_REL_MAIOR_IGUAL:
            case OP_REL_MENOR:
            case OP_REL_MENOR_IGUAL:
            case OP_REL_IGUAL:
            case OP_REL_DIFERENTE:
                tipoExpressao = TipoExpressao.OPERACAO_RELACIONAL;
                break;
            case OP_LOG_E:
            case OP_LOG_OU:
                tipoExpressao = TipoExpressao.OPERACAO_LOGICA;
                break;
            default:
                tipoExpressao = TipoExpressao._INDEFINIDO;
                break;
        }
    }
    
    private void defineTexto(){
        if (expressaoEsq != null && operador != null && expressaoDir != null) {
            for (Token t : expressaoEsq.listaTokens()) {
                super.addToken(t);
            }
            super.addToken(operador);
            for (Token t : expressaoDir.listaTokens()) {
                super.addToken(t);
            }
        }
    }
    
    @Override
    public String imprimeExpressao (){
        return "< " + 
                (parentesesAbre == null ? "" : "( ") +
                expressaoEsq.imprimeExpressao() + 
                " " + operador.getPalavra() + " " +
                expressaoDir.imprimeExpressao() + 
                (parentesesFecha == null ? "" : " )") +
                " >";
    }

}
