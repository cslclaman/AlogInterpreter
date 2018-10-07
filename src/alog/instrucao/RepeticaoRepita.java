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
public class RepeticaoRepita extends EstruturaControle {
    private Token tokenRepita;
    private Token tokenAte;

    public RepeticaoRepita() {
        super();
        this.tipo = TipoInstrucao.REPETICAO_REPITA;
    }

    public Token getTokenRepita() {
        return tokenRepita;
    }

    public void setTokenRepita(Token tokenRepita) {
        this.tokenRepita = tokenRepita;
        super.addToken(tokenRepita);
    }

    public Token getTokenAte() {
        return tokenAte;
    }

    public void setTokenAte(Token tokenAte) {
        this.tokenAte = tokenAte;
        texto.append("\n");
        super.addToken(tokenAte);
    }
    
    @Override
    public void finaliza() {
        if (valida) {
            valida =
                tokenRepita != null &&
                (condicao != null && condicao.isValida()) &&
                (instrucao != null && instrucao.isValida());
        }
    }
}
