/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.expressao.Expressao;
import alog.token.FuncaoToken;
import alog.token.Token;

/**
 *
 * @author Caique Souza
 */
public class RepeticaoPara extends EstruturaControle {
    private Token tokenPara;
    private Token contador;
    private Expressao expressaoDe;
    private Expressao expressaoAte;
    
    public RepeticaoPara() {
        super();
        tipo = TipoInstrucao.REPETICAO_PARA;
    }

    public void setTokenPara(Token tokenPara) {
        this.tokenPara = tokenPara;
        super.addToken(tokenPara);
    }

    public void setContador(Token contador) {
        if (contador.getFuncaoToken() == FuncaoToken.IDENT_NOME_VARIAVEL) {
            this.contador = contador;
        } else {
            invalidaInstrucao();
        }
        super.addToken(contador);
    }

    public void setExpressaoDe(Expressao expressaoDe) {
        this.expressaoDe = expressaoDe;
        tokens.addAll(expressaoDe.listaTokens());
        texto.append(expressaoDe.toString()).append(" ");
        if (!expressaoDe.instrucaoValida()){
            invalidaInstrucao();
        }
    }
    
    public void setExpressaoAte(Expressao expressaoAte) {
        this.expressaoAte = expressaoAte;
        tokens.addAll(expressaoAte.listaTokens());
        texto.append(expressaoAte.toString()).append(" ");
        if (!expressaoAte.instrucaoValida()){
            invalidaInstrucao();
        }
    }
}
