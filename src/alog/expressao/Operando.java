/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.expressao;

import alog.model.TipoDado;
import alog.token.Token;

/**
 *
 * @author Caique Souza
 */
public class Operando extends Expressao {

    protected Token operando;
    
    public Operando() {
        super();
    }
    
    public void setOperando(Token token) {
        this.operando = token;
        this.texto.append(operando.getPalavra());
        this.tokens.add(token);
        
        switch (token.getFuncaoToken()) {
            case CONST_CARACTER:
                tipoExpressao = TipoExpressao.OPERANDO_CONSTANTE;
                tipoResultado = TipoDado.CARACTER;
                resultado = token.getPalavra();
                break;
            case CONST_INTEIRA:
                tipoExpressao = TipoExpressao.OPERANDO_CONSTANTE;
                tipoResultado = TipoDado.INTEIRO;
                resultado = token.getPalavra();
                break;
            case CONST_REAL:
                tipoExpressao = TipoExpressao.OPERANDO_CONSTANTE;
                tipoResultado = TipoDado.REAL;
                resultado = token.getPalavra();
                break;
            case IDENT_NOME_VARIAVEL:
                tipoExpressao = TipoExpressao.OPERANDO_VARIAVEL;
                break;
        }
    }

    public Token getOperando() {
        return operando;
    }
    
    @Override
    public String imprimeExpressao (){
        return "< " + 
                (parentesesAbre == null ? "" : "( ") +
                operando.getPalavra() + 
                (parentesesFecha == null ? "" : ")") +
                " >";
    }
    
}
