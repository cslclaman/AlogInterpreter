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
 * @author Caique Souza
 */
public abstract class EstruturaControle extends Instrucao {
    
    protected Expressao condicao;
    protected Instrucao instrucao;
    
    public void setExpressao (Expressao condicao) {
        this.condicao = condicao;
        tokens.addAll(condicao.listaTokens());
        texto.append(condicao.toString()).append(" ");
        if (!condicao.instrucaoValida()){
            invalidaInstrucao();
        }
    }
    
    public void setInstrucao(Instrucao instrucao) {
        this.instrucao = instrucao;
        tokens.addAll(instrucao.listaTokens());
        texto.append("\n    ").append(instrucao.toString()).append(" ");
        if (!instrucao.instrucaoValida()){
            invalidaInstrucao();
        }
    }
    
    @Override
    public boolean instrucaoValida() {
        if (condicao == null || instrucao == null){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }

    public Expressao getCondicao() {
        return condicao;
    }

    public Instrucao getInstrucao() {
        return instrucao;
    }
    
}
