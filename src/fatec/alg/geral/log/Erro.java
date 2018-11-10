/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.geral.log;

import fatec.alg.geral.token.Token;

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
    
    public String getPosicao() {
        return String.format("Linha %d, Coluna %d",
                token.getLinha() + 1, token.getColuna() + 1);
    }
    
    @Override
    public String toString(){
        return String.format("%-6s - %s: %s",
                status.toString(), getPosicao() + 1, mensagem);
    }
}
