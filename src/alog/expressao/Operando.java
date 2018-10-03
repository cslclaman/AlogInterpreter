/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.expressao;

import alog.model.TipoVariavel;
import alog.token.Token;

/**
 *
 * @author Caique Souza
 */
public class Operando extends Expressao {

    protected Token operando;
    
    public Operando() {
        super();
        tipoExpressao = TipoExpressao.OPERANDO;
    }
    
    public void setOperando(Token token) {
        this.operando = token;
        this.texto.append(operando.getPalavra());
        this.tokens.add(token);
        
        switch (token.getFuncaoToken()) {
            case CONST_CARACTER:
                tipoResultado = TipoVariavel.CARACTER;
                resultado = token.getPalavra();
                break;
            case CONST_INTEIRA:
                tipoResultado = TipoVariavel.INTEIRO;
                resultado = token.getPalavra();
                break;
            case CONST_REAL:
                tipoResultado = TipoVariavel.REAL;
                resultado = token.getPalavra();
                break;
        }
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
