package alog.instrucao;

import alog.expressao.Expressao;
import alog.token.Token;
import java.util.LinkedList;

/**
 *
 * @author Caique
 */
public class Condicional extends EstruturaControle {
    private Token tokenSe;
    private boolean composta;
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

    @Override
    public void setExpressao (Expressao condicional) {
        if (this.tokenSe != null){
            super.setExpressao(condicional);
        }
    }

    public void setInstrucaoSe(Instrucao instrucaoSe) {
        if (this.condicao != null){
            super.setInstrucao(instrucaoSe);
        }
    }

    public void setTokenSenao(Token tokenSenao) {
        if (this.instrucao != null){
            this.tokenSenao = tokenSenao;
            texto.append("\n    ");            
            super.addToken(tokenSenao);
            composta = true;
        }
    }

    public void setInstrucaoSenao(Instrucao instrucaoSenao) {
        if (composta){
            this.instrucaoSenao = instrucaoSenao;
            tokens.addAll(instrucaoSenao.listaTokens());
            texto.append("\n    ").append(instrucaoSenao.toString());
        }
    }

    @Override
    public boolean instrucaoValida() {
        if (instrucao == null || (composta && instrucaoSenao == null)){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }

    public boolean isComposta() {
        return composta;
    }

    public Instrucao getInstrucaoSe() {
        return instrucao;
    }

    public Instrucao getInstrucaoSenao() {
        return instrucaoSenao;
    }
    
}
