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
        texto = "";
        tipo = TipoInstrucao.CONDICIONAL;
        tokens = new LinkedList<>();
        composta = false;
    }

    public void setTokenSe(Token tokenSe) {
        if (this.tokenSe == null){
            this.tokenSe = tokenSe;
            tokens.add(tokenSe);
        }
    }

    public void setExpressao (Expressao condicional) {
        if (this.tokenSe != null){
            this.condicional = condicional;
            for (Token token : condicional.listaTokens()){
                tokens.add(token);
            }
        }
    }

    public void setInstrucaoSe(Instrucao instrucaoSe) {
        if (this.condicional != null){
            this.instrucaoSe = instrucaoSe;
            for (Token token : instrucaoSe.listaTokens()){
                tokens.add(token);
            }
        }
    }

    public void setTokenSenao(Token tokenSenao) {
        if (this.instrucaoSe != null){
            this.tokenSenao = tokenSenao;
            tokens.add(tokenSenao);
            composta = true;
        }
    }

    public void setInstrucaoSenao(Instrucao instrucaoSenao) {
        if (composta){
            this.instrucaoSenao = instrucaoSenao;
            for (Token token : instrucaoSenao.listaTokens()){
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
