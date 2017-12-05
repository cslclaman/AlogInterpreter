/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Token;

/**
 *
 * @author Caique
 */
public class Scanner {
    
    
    private int pos;
    private int order;
    private int linha;
    private int coluna;
    private int len;
    private boolean next;
    private char[] texto;

    public Scanner(String texto) {
        this.texto = texto.toCharArray();
        len = texto.length();
        pos = linha = coluna = order = 0;
        next = len > 0;
    }
    
    public boolean hasNext(){
        return next;
    }
    
    public Token getNext(){
        int contLit = 0, contAlpha = 0, contNum = 0, contRes = 0, contOper = 0; 
        
        boolean literal = false;
        boolean go = true;
        Token token = new Token();
        token.setLine(linha);
        token.setColumn(coluna);
        token.setOrder(order++);
        
        while (go && next && pos < len){
            if (pos + 1 == len){
                next = false;
            }
            char ch = texto[pos];
            if (literal){
                if (ch == '"'){
                    literal = false;
                    go = false;
                }
                token.updateWord(ch);
                pos++;
                contLit++;
            } else {
                if (isBlank(ch)){
                    go = false;
                } else if (Character.isLetter(ch) || ch == '_'){
                    if (contRes > 0 || contOper > 0){
                        go = false;
                    } else {
                        token.updateWord(ch);
                        pos++;
                        coluna ++;
                        contAlpha ++;
                    }
                } else if (Character.isDigit(ch)){
                    if (contRes > 0 || contOper > 0){
                        go = false;
                    } else {
                        token.updateWord(ch);
                        pos++;
                        coluna ++;
                        contNum ++;
                    }
                } else if (isReserved(ch)){
                    if (token.getLength() == 0){
                        token.updateWord(ch);
                        pos++;
                        coluna ++;
                        contRes ++;
                    }
                    go = false;
                } else if (ch == '"'){
                    if (token.getLength() == 0){
                        literal = true;
                        token.updateWord(ch);
                        pos++;
                        coluna++;
                    } else {
                        go = false;
                    }
                } else if (isLogicalOperator(ch) || isArithmeticOperator(ch)){
                    if (contAlpha > 0 || contNum > 0){
                        go = false;
                    } else {
                        token.updateWord(ch);
                        pos++;
                        coluna++;
                        contOper++;
                    }
                } else {
                    System.out.println("Caractere não considerável: " + ch);
                    token.updateWord(ch);
                    go = false;
                    pos++;
                    coluna++;
                }
            }
        }
        go = true;
        while (go && pos < len){
            char ch = texto[pos];
            if (ch == '\r') {
                if (pos < len && texto[pos+1] == '\n'){
                    pos++;
                    continue;
                } else {
                    ch = '\n';
                }
            }
            switch (ch) {
                case '\n':
                    pos ++;
                    coluna = 0;
                    linha ++;
                    break;
                case ' ':
                    pos ++;
                    coluna ++;
                    break;
                case '\t':
                    pos ++;
                    coluna += 4;
                    break;
                default:
                    go = false;
                    break;
            }
        }
        if (contLit > 0){
            token.setType("Literal");
        } else {
            if (contNum > 0){
                if (contAlpha > 0){
                    token.setType("Alfanumérico");
                } else {
                    token.setType("Número");
                }
            } else {
                if (contAlpha > 0){
                    token.setType("Alfabético");
                } else {
                    if (contRes > 0){
                        token.setType("Delimitador");
                    } else {
                        if (contOper > 0){
                            token.setType("Operador");
                        } else {
                            token.setType("Indefinido");
                        }
                    }
                }
            }
        }
        
        if (pos == len){
            next = false;
        }
        
        return token;
    }
    
    private static boolean isBlank(char ch){
        char[] delimiters = {'\n','\r','\t',' '};
        for (char cb : delimiters){
            if (cb == ch){
                return true;
            }
        }
        return false;
    }
    
    private static boolean isReserved(char ch){
        char[] delimiters = {':',';','(',')','[',']',',','.'};
        for (char cd : delimiters){
            if (cd == ch){
                return true;
            }
        }
        return false;
    }
    
    private static boolean isArithmeticOperator(char ch){
        char[] arithmetic = {'+','-','*','/'};
        for (char ca : arithmetic){
            if (ca == ch){
                return true;
            }
        }
        return false;
    }
    
    private static boolean isLogicalOperator(char ch){
        char[] logical = {'>','<','=','!'};
        for (char cl : logical){
            if (cl == ch){
                return true;
            }
        }
        return false;
    }
}
