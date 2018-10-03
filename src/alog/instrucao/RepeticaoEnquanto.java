/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.expressao.Expressao;
import alog.token.Token;

/**
 *
 * @author Caique
 */
public class RepeticaoEnquanto extends EstruturaControle {
    private Token tokenEnquanto;
    private Token tokenFaca;
    
    public RepeticaoEnquanto() {
        super();
        tipo = TipoInstrucao.REPETICAO_ENQUANTO;
    }

    public void setTokenEnquanto(Token tokenEnquanto) {
        this.tokenEnquanto = tokenEnquanto;
        super.addToken(tokenEnquanto);
    }

    public void setTokenFaca(Token tokenFaca) {
        this.tokenFaca = tokenFaca;
        super.addToken(tokenFaca);
    }

}
