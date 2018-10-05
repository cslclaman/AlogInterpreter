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
public class OperacaoUnaria extends Expressao {
    
    private Expressao expressao;
    private Token operador;

    public OperacaoUnaria() {
        super();
        expressao = null;
        tipoExpressao = TipoExpressao.OPERACAO_UNARIA;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
        defineTexto();
    }

    public Token getOperador() {
        return operador;
    }

    public void setOperador(Token operador) {
        this.operador = operador;
        defineTexto();
        switch (operador.getFuncaoToken()) {
            case OP_SIG_NEGATIVO:
            case OP_SIG_POSITIVO:
                tipoExpressao = TipoExpressao.OPERACAO_ARITMETICA;
                break;
            case OP_LOG_NAO:
                tipoExpressao = TipoExpressao.OPERACAO_LOGICA;
                break;
            default:
                tipoExpressao = TipoExpressao._INDEFINIDO;
                break;
        }
    }
    
    private void defineTexto(){
        if (operador != null && expressao != null) {
            super.addToken(operador);
            for (Token t : expressao.listaTokens()) {
                super.addToken(t);
            }
        }
    }
    
    @Override
    public String imprimeExpressao (){
        return "< " + 
                (parentesesAbre == null ? "" : "( ") +
                operador.getPalavra() + " " +
                expressao.imprimeExpressao() + 
                (parentesesFecha == null ? "" : " )") +
                " >";
    }

}
