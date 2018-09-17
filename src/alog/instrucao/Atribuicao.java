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
        texto = "";
        tipo = TipoInstrucao.ATRIBUICAO;
        tokens = new LinkedList<>();
    }
    
    public Token getVariavel() {
        return variavel;
    }

    public void setVariavel(Token variavel) {
        if (this.variavel == null){
            this.variavel = variavel;
            tokens.add(variavel);
        }
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        if (variavel != null) {
            this.expressao = expressao;
            for (Token token : expressao.listaTokens()){
                tokens.add(token);
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
