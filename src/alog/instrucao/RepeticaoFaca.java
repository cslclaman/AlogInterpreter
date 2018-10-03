/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.token.Token;

/**
 *
 * @author Caique Souza
 */
public class RepeticaoFaca extends EstruturaControle {
    private Token tokenFaca;
    private Token tokenEnquanto;

    public RepeticaoFaca() {
        super();
        this.tipo = TipoInstrucao.REPETICAO_FACA;
    }

    public Token getTokenFaca() {
        return tokenFaca;
    }

    public void setTokenFaca(Token tokenFaca) {
        this.tokenFaca = tokenFaca;
        super.addToken(tokenFaca);
    }

    public Token getTokenEnquanto() {
        return tokenEnquanto;
    }

    public void setTokenEnquanto(Token tokenEnquanto) {
        this.tokenEnquanto = tokenEnquanto;
        texto.append("\n");
        super.addToken(tokenEnquanto);
    }
    
}
