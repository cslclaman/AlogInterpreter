/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.token.Token;

/**
 *
 * @author Caique
 */
public class RepeticaoEnquanto extends EstruturaControle {
    private Token tokenEnquanto;
    
    public RepeticaoEnquanto() {
        super();
        tipo = TipoInstrucao.REPETICAO_ENQUANTO;
    }

    public void setTokenEnquanto(Token tokenEnquanto) {
        this.tokenEnquanto = tokenEnquanto;
        super.addToken(tokenEnquanto);
    }

    public Token getTokenEnquanto() {
        return tokenEnquanto;
    }
    
    @Override
    public void finaliza() {
        if (valida) {
            valida =
                tokenEnquanto != null &&
                (condicao != null && condicao.isValida()) &&
                (instrucao != null && instrucao.isValida());
        }
    }
}
