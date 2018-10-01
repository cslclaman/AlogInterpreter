package alog.instrucao;

import alog.expressao.Expressao;
import alog.token.Token;
import java.util.LinkedList;

/**
 *
 * @author Caique
 */
public class Condicional extends Instrucao {
    private Token tokenSe;
    private Expressao condicional;
    private boolean composta;
    private Instrucao instrucaoSe;
    private Token tokenSenao;
    private Instrucao instrucaoSenao;

    public Condicional() {
        super();
        tipo = TipoInstrucao.CONDICIONAL;
        composta = false;
    }

    public void setTokenSe(Token tokenSe) {
        if (this.tokenSe == null){
            this.tokenSe = tokenSe;
            super.addToken(tokenSe);
        }
    }

    public void setExpressao (Expressao condicional) {
        if (this.tokenSe != null){
            this.condicional = condicional;
            for (Token token : condicional.listaTokens()){
                super.addToken(token);
            }
            if (!condicional.instrucaoValida()){
                invalidaInstrucao();
            }
        }
    }

    public void setInstrucaoSe(Instrucao instrucaoSe) {
        if (this.condicional != null){
            this.instrucaoSe = instrucaoSe;
            texto.append("\n        ").append(instrucaoSe.toString());
            for (Token token : instrucaoSe.listaTokens()){
                //super.addToken(token);
                tokens.add(token);
            }
            
        }
    }

    public void setTokenSenao(Token tokenSenao) {
        if (this.instrucaoSe != null){
            this.tokenSenao = tokenSenao;
            texto.append("\n    ");            
            super.addToken(tokenSenao);
            composta = true;
        }
    }

    public void setInstrucaoSenao(Instrucao instrucaoSenao) {
        if (composta){
            this.instrucaoSenao = instrucaoSenao;
            texto.append("\n        ").append(instrucaoSenao.toString());
            for (Token token : instrucaoSenao.listaTokens()){
                //super.addToken(token);
                tokens.add(token);
            }
        }
    }

    @Override
    public boolean instrucaoValida() {
        if (instrucaoSe == null || (composta && instrucaoSenao == null)){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }

    public Expressao getCondicional() {
        return condicional;
    }

    public boolean isComposta() {
        return composta;
    }

    public Instrucao getInstrucaoSe() {
        return instrucaoSe;
    }

    public Instrucao getInstrucaoSenao() {
        return instrucaoSenao;
    }
    
}
