package alog.instrucao;

import alog.token.Token;

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
        this.tokenSe = tokenSe;
        super.addToken(tokenSe);
    }

    public void setInstrucaoSe(Instrucao instrucaoSe) {
        super.setInstrucao(instrucaoSe);
    }

    public void setTokenSenao(Token tokenSenao) {
        this.tokenSenao = tokenSenao;
        texto.append("\n    ");            
        super.addToken(tokenSenao);
        composta = true;
    }

    public void setInstrucaoSenao(Instrucao instrucaoSenao) {
        this.instrucaoSenao = instrucaoSenao;
        tokens.addAll(instrucaoSenao.listaTokens());
        texto.append("\n    ").append(instrucaoSenao.toString());
    }
    
    public boolean isComposta() {
        return composta;
    }

    public Token getTokenSe() {
        return tokenSe;
    }

    public Instrucao getInstrucaoSe() {
        return instrucao;
    }

    public Token getTokenSenao() {
        return tokenSenao;
    }

    public Instrucao getInstrucaoSenao() {
        return instrucaoSenao;
    }
    
    @Override
    public void finaliza() {
        if (valida) {
            boolean compostaValida = 
                (!composta) ||
                (composta && tokenSenao != null &&
                (instrucaoSenao != null && instrucaoSenao.isValida()));
        
            valida =
                tokenSe != null &&
                (condicao != null && condicao.isValida()) &&
                (instrucao != null && instrucao.isValida()) &&
                compostaValida;
        }
    }
    
}
