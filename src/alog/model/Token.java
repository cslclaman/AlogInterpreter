/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

/**
 *
 * @author Caique
 */
public class Token {
    private String word;
    private int order;
    private int pos;
    private int lin;
    private int col;
    private int length;
    private String type;
    
    public Token() {
        word = "";
        length = 0;
    }

    public void updateWord(char ch){
        word += ch;
        length ++;
    }
    
    public String getWord() {
        return word;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return word + " (pos " + (order + 1) + " at line " + (lin + 1) + ", column " + (col + 1) + " - char no. " + pos + ")";
    }
    
}
