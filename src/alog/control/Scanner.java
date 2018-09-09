package alog.control;

import alog.model.TipoToken;
import alog.model.Token;
import java.util.ArrayList;

/**
 * Classe de scanner que realiza análise léxica em um código e retorna os tokens por meio de um iterador.
 * @author Caique
 */
public class Scanner {
    
    private int pos;
    private int posnolf;
    private int order;
    private int linha;
    private int coluna;
    private int len;
    private boolean next;
    private char[] texto;

    /**
     * Constrói uma nova instância do Scanner a partir de uma String de código-fonte.
     * @param texto Código a ser escaneado
     */
    public Scanner(String texto) {
        this.texto = texto.toCharArray();
        len = texto.length();
        pos = 0;
        posnolf = 0;
        linha = 0;
        coluna = 0;
        order = 0;
        next = len > 0;
    }
    
    /**
     * Retorna se ainda há algum token a ser verificado no código.
     * @return True se ainda há tokens.
     */
    public boolean hasNext(){
        return next;
    }
    
    /**
     * Retorna o próximo token encontrado, devidamente classificado.
     * @return {@link alog.model.Token}
     */
    public Token getNext(){
        int contLit = 0, contAlpha = 0, contNum = 0, contRes = 0, contOper = 0; 
        
        boolean literal = false;
        boolean go = true;
        Token token = new Token(linha, coluna, posnolf, order++);
        
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
                token.atualizaPalavra(ch);
                pos++;
                posnolf++;
                contLit++;
            } else {
                if (isBlank(ch)){
                    go = false;
                } else if (Character.isLetter(ch) || ch == '_'){
                    if (contRes > 0 || contOper > 0){
                        go = false;
                    } else {
                        token.atualizaPalavra(ch);
                        pos++;
                        posnolf++;
                        coluna ++;
                        contAlpha ++;
                    }
                } else if (Character.isDigit(ch)){
                    if (contRes > 0 || contOper > 0){
                        go = false;
                    } else {
                        token.atualizaPalavra(ch);
                        pos++;
                        posnolf++;
                        coluna ++;
                        contNum ++;
                    }
                } else if (isReserved(ch)){
                    if (token.getTamanho() == 0){
                        token.atualizaPalavra(ch);
                        pos++;
                        posnolf++;
                        coluna ++;
                        contRes ++;
                    }
                    go = false;
                } else if (ch == '"'){
                    if (token.getTamanho() == 0){
                        literal = true;
                        token.atualizaPalavra(ch);
                        pos++;
                        posnolf++;
                        coluna++;
                    } else {
                        go = false;
                    }
                } else if (isRelationalOperator(ch) || isArithmeticOperator(ch)){
                    if (contAlpha > 0 || contNum > 0){
                        go = false;
                    } else {
                        token.atualizaPalavra(ch);
                        pos++;
                        coluna++;
                        posnolf++;
                        contOper++;
                    }
                } else {
                    System.out.println("Caractere não considerável: "
                            + ch + " (" + (int)ch + ")");
                    token.atualizaPalavra(ch);
                    go = false;
                    pos++;
                    posnolf++;
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
                    posnolf++;
                    break;
                case ' ':
                    pos ++;
                    posnolf++;
                    coluna ++;
                    break;
                case '\t':
                    pos ++;
                    posnolf += 4;
                    coluna += 4;
                    break;
                default:
                    go = false;
                    break;
            }
        }
        if (contLit > 0){
            token.setTipoToken(TipoToken.LITERAL);
        } else {
            if (contNum > 0){
                if (contAlpha > 0){
                    token.setTipoToken(TipoToken.ALFANUMERICO);
                } else {
                    token.setTipoToken(TipoToken.NUMERICO);
                }
            } else {
                if (contAlpha > 0){
                    token.setTipoToken(TipoToken.ALFABETICO);
                } else {
                    if (contRes > 0){
                        token.setTipoToken(TipoToken.DELIMITADOR);
                    } else {
                        if (contOper > 0){
                            token.setTipoToken(TipoToken.OPERADOR);
                        } else {
                            token.setTipoToken(TipoToken.INDEFINIDO);
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
    
    /**
     * Retorna todos os Tokens encontrados no texto.
     * @return ArrayList com tokens
     */
    public ArrayList<Token> getAllTokens(){
        ArrayList<Token> list = new ArrayList<>();
        while (hasNext()){
            list.add(getNext());
        }
        return list;
    }
    
    /**
     * Retorna se um determinado caracter é "em branco" (espaço, quebra de linha, tabulação).
     * @param ch Caracter a verificar
     * @return
     */
    private static boolean isBlank(char ch){
        char[] delimiters = {'\n','\r','\t',' '};
        return isListed(ch, delimiters);
    }
    
    /**
     * Retorna se um determinado caracter é um símbolo reservado (pontuação e delimitadores).
     * @param ch Caracter a verificar
     * @return
     */
    private static boolean isReserved(char ch){
        char[] reserved = {':',';','(',')','[',']',',','.'};
        return isListed(ch, reserved);
    }
    
    /**
     * Retorna se um determinado caracter é um operador aritmético.
     * @param ch Caracter a verificar
     * @return
     */
    private static boolean isArithmeticOperator(char ch){
        char[] arithmetic = {'+','-','*','/'};
        return isListed(ch, arithmetic);
    }
    
    /**
     * Retorna se um determinado caracter é um operador relacional
     * @param ch Caracter a verificar
     * @return
     */
    private static boolean isRelationalOperator(char ch){
        char[] relational = {'>','<','=','!'};
        return isListed(ch, relational);
    }
    
    /**
     * Retorna se um determinado caracter está contido em um array de caracteres
     * @param ch Caracter a verificar
     * @param list Array a ser verificado
     * @return
     */
    private static boolean isListed(char ch, char[] list){
        for (char c : list){
            if (c == ch){
                return true;
            }
        }
        return false;
    }
}
