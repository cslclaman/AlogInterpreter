/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.token.FuncaoToken;
import alog.token.Token;

/**
 * Instrução que contém um único token {@link FuncaoToken#RES_BLOCO_FIM}
 * @author Caique Souza
 */
public class FimBloco extends Instrucao {
    private Token fim;
    
    public FimBloco() {
        super();
        tipo = TipoInstrucao.BLOCO;
    }

    public void setTokenFim(Token token) {
        this.fim = token;
        super.addToken(token);
    }
    
    @Override
    public void finaliza() {
        if (valida){
            valida = fim != null;
        }
    }

}
