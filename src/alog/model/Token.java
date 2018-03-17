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
 * O texto dessa unidade de código, aqui, será chamado de Word.
 * Outros atributos, como linha e coluna onde aparece no código-fonte original, também estão presentes.
 * @author Caique
 */
public class Token {
    private String word;
    private int order;
    private int pos;
    private int lin;
    private int col;
    private int length;
    private TipoToken type;
    
    /**
     * Constrói um novo Token vazio (ou seja, cujo atributo Word é vazio).
     */
    public Token() {
        word = "";
        length = 0;
    }

    /**
     * Adiciona um novo caracter à palavra (Word) do token e aumenta seu tamanho.
     * @param ch Caracter a ser adicionado.
     */
    public void updateWord(char ch){
        word += ch;
        length ++;
    }
    
    /**
     * Retorna a palavra (Word) do Token.
     * @return Ex. "Inteiro" (para o identificador de tipo).
     */
    public String getWord() {
        return word;
    }

    /**
     * 
     * @param word 
     */
    public void setWord(String word) {
        this.word = word;
        length = word.length();
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getLine() {
        return lin;
    }

    public void setLine(int lin) {
        this.lin = lin;
    }

    public int getColumn() {
        return col;
    }

    public void setColumn(int col) {
        this.col = col;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public TipoToken getType() {
        return type;
    }

    public void setType(TipoToken type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return word + " (pos " + (order + 1) + " at line " + (lin + 1) + ", column " + (col + 1) + " - char no. " + pos + ")";
    }
    
}
