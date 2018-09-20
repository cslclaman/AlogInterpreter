/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.analise;

import alog.token.Token;

/**
 *
 * @author Caique
 */
public class Erro {
    public static final int LOG = 0;
    public static final int INFO = 1;
    public static final int ALERTA = 2;
    public static final int ERRO = 3;
    
    private Integer status;
    private Token token;
    private String erro;

    public Erro(Token token, String erro) {
        this.status = LOG;
        this.token = token;
        this.erro = erro;
    }
    
    public Erro(int status, Token token, String erro) {
        this.status = status;
        this.token = token;
        this.erro = erro;
    }

    public String getStatus(){
        switch (status){
            case INFO: return "INFO";
            case ALERTA: return "ALERTA";
            case ERRO: return "ERRO";
            default: return "";
        }
    }

    public Token getToken() {
        return token;
    }

    public String getErro() {
        return erro;
    }
    
    
    
    @Override
    public String toString(){
        return String.format(
                "%6s - Linha %d, Coluna %d: %s",
                getStatus(), token.getLinha() + 1, token.getColuna() + 1, erro);
    }
}
