/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

/**
 * Classe que representa um token.
 * Um token é uma unidade de código (palavra, pontuação, número, símbolo, etc).
 * Essa unidade de código simboliza alguma coisa (operação, nome de variável, função, etc).
 * O texto dessa unidade de código, aqui, será chamado de Palavra.
 * Outros atributos, como linha e coluna onde aparece no código-fonte original, também estão presentes.
 * @author Caique
 */
public class Token {
    private String palavra;
    private int ordem;
    private int posicao;
    private int lin;
    private int col;
    private int tamanho;
    private TipoToken tipoToken;
    private FuncaoToken funcaoToken;
    
    /**
     * Constrói um novo Token vazio (ou seja, cujo atributo Word é vazio).
     */
    public Token() {
        palavra = "";
        tamanho = 0;
    }

    /**
     * Adiciona um novo caracter à palavra do token e aumenta seu tamanho.
     * @param ch Caracter a ser adicionado.
     */
    public void atualizaPalavra(char ch){
        palavra += ch;
        tamanho ++;
    }
    
    /**
     * Retorna a palavra (Word) do Token.
     * @return Ex. "Inteiro" (para o identificador de tipo).
     */
    public String getPalavra() {
        return palavra;
    }

    /**
     * 
     * @param palavra 
     */
    public void setPalavra(String palavra) {
        this.palavra = palavra;
        tamanho = palavra.length();
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getLinha() {
        return lin;
    }

    public void setLinha(int lin) {
        this.lin = lin;
    }

    public int getColuna() {
        return col;
    }

    public void setColuna(int col) {
        this.col = col;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public TipoToken getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(TipoToken tipoToken) {
        this.tipoToken = tipoToken;
    }

    public FuncaoToken getFuncaoToken(){
        return funcaoToken;
    }
    
    public void setFuncaoToken(FuncaoToken func){
        this.funcaoToken = func;
    }
    
    @Override
    public String toString(){
        return palavra + " (pos " + (ordem + 1) + " na linha " + (lin + 1) + ", coluna " + (col + 1) + " - carac. no. " + posicao + ")";
    }
    
}
