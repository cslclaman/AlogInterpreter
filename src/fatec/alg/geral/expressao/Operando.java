/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.geral.expressao;

import fatec.alg.geral.tipo.TipoDado;
import fatec.alg.geral.token.Token;

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
        this.texto.append(token.getPalavra());
        this.tokens.add(token);
        
        switch (token.getFuncaoToken()) {
            case CONST_CARACTER:
            case CONST_INTEIRA:
            case CONST_REAL:
                tipoExpressao = TipoExpressao.OPERANDO_CONSTANTE;
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
