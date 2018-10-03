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
public class RepeticaoEnquanto extends Instrucao {
    private Token tokenEnquanto;
    private Token tokenFaca;
    private Expressao condicao;
    private Instrucao instrucao;

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

    public void setExpressao(Expressao condicao) {
        this.condicao = condicao;
        tokens.addAll(condicao.listaTokens());
        texto.append(condicao.toString());
        if (!condicao.instrucaoValida()){
            invalidaInstrucao();
        }
    }

    public void setInstrucao(Instrucao instrucao) {
        this.instrucao = instrucao;
        tokens.addAll(instrucao.listaTokens());
        texto.append("\n    ").append(instrucao.toString());
        if (!instrucao.instrucaoValida()){
            invalidaInstrucao();
        }
    }
    
    
    
}
