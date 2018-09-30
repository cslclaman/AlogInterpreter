/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.expressao.Expressao;
import alog.token.Token;
import java.util.LinkedList;

/**
 *
 * @author Caique
 */
public class Atribuicao extends Instrucao {
    
    protected Token variavel;
    protected Expressao expressao;

    public Atribuicao() {
        super();
        tipo = TipoInstrucao.ATRIBUICAO;
    }
    
    public Token getVariavel() {
        return variavel;
    }

    public void setVariavel(Token variavel) {
        if (this.variavel == null){
            this.variavel = variavel;
            super.addToken(variavel);
        }
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        if (variavel != null) {
            this.expressao = expressao;
            if (!expressao.instrucaoValida()){
                invalidaInstrucao();
            }
            for (Token token : expressao.listaTokens()){
                super.addToken(token);
            }
        }
    }

    @Override
    public boolean instrucaoValida() {
        if (variavel == null || expressao == null){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }
    
}
