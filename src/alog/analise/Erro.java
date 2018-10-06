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
    private TipoErro status;
    private Token token;
    private String mensagem;

    public Erro(TipoErro status, Token token, String mensagem) {
        this.status = status;
        this.token = token;
        this.mensagem = mensagem;
    }

    public Erro(TipoErro status, char caracter, int linha, int coluna, int posicao, String mensagem) {
        this.status = status;
        this.token = new Token(linha, coluna, posicao, 0);
        token.atualizaPalavra(caracter);
        this.mensagem = mensagem;
    }

    public TipoErro getTipo() {
        return status;
    }
    
    public Token getToken() {
        return token;
    }

    public String getMensagem() {
        return mensagem;
    }
    
    @Override
    public String toString(){
        return String.format("%-6s - Linha %d, Coluna %d: %s",
                status.toString(), token.getLinha() + 1, token.getColuna() + 1, mensagem);
    }
}
