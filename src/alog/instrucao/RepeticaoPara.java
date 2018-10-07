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
    private Token variavelCont;
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

    public void setVariavelCont(Token variavelCont) {
        if (variavelCont.getFuncaoToken() == FuncaoToken.IDENT_NOME_VARIAVEL) {
            this.variavelCont = variavelCont;
        } 
        super.addToken(variavelCont);
    }

    public void setExpressaoDe(Expressao expressaoDe) {
        this.expressaoDe = expressaoDe;
        tokens.addAll(expressaoDe.listaTokens());
        texto.append(expressaoDe.toString()).append(" ");
    }
    
    public void setExpressaoAte(Expressao expressaoAte) {
        this.expressaoAte = expressaoAte;
        tokens.addAll(expressaoAte.listaTokens());
        texto.append(expressaoAte.toString()).append(" ");
    }

    public Token getTokenPara() {
        return tokenPara;
    }

    public Token getVariavelCont() {
        return variavelCont;
    }

    public Expressao getExpressaoDe() {
        return expressaoDe;
    }

    public Expressao getExpressaoAte() {
        return expressaoAte;
    }
    
    @Override
    public void finaliza() {
        if (valida) {
            valida =
                tokenPara != null &&
                variavelCont != null &&
                (expressaoDe != null && expressaoDe.isValida()) &&
                (expressaoAte != null && expressaoAte.isValida()) &&
                (instrucao != null && instrucao.isValida());
        }
    }
    
}
