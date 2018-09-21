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
    private char caracter;
    private int linha;
    private int coluna;
    private int posicao;
    private String erro;

    public Erro(Token token, String erro) {
        this.status = TipoErro.INFO;
        this.token = token;
        this.caracter = token.getPalavra().charAt(0);
        this.linha = token.getLinha();
        this.coluna = token.getColuna();
        this.posicao = token.getPosicao();
        this.erro = erro;
    }
    
    public Erro(TipoErro status, Token token, String erro) {
        this.status = status;
        this.token = token;
        this.caracter = token.getPalavra().charAt(0);
        this.linha = token.getLinha();
        this.coluna = token.getColuna();
        this.posicao = token.getPosicao();
        this.erro = erro;
    }

    public Erro(char caracter, int linha, int coluna, int posicao, String erro) {
        this.status = TipoErro.INFO;
        this.caracter = caracter;
        this.linha = linha;
        this.coluna = coluna;
        this.posicao = posicao;
        this.token = new Token(linha, coluna, posicao, 0);
        token.atualizaPalavra(caracter);
        this.erro = erro;
    }
    
    public Erro(TipoErro status, char caracter, int linha, int coluna, int posicao, String erro) {
        this.status = status;
        this.caracter = caracter;
        this.linha = linha;
        this.coluna = coluna;
        this.posicao = posicao;
        this.token = new Token(linha, coluna, posicao, 0);
        token.atualizaPalavra(caracter);
        this.erro = erro;
    }

    public String getStatus(){
        return status.toString();
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
                "%-6s - Linha %d, Coluna %d: %s",
                getStatus(), token.getLinha() + 1, token.getColuna() + 1, erro);
    }
}
