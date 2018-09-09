package alog.control;

import alog.token.TipoToken;
import alog.token.Token;
import java.util.ArrayList;

/**
 * Classe de scanner que realiza análise léxica em um código e retorna os tokens por meio de um iterador.
 * @author Caique
 */
public class Scanner {
    
    private int indice;
    private int posicao;
    private int ordem;
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
        indice = 0;
        posicao = 0;
        linha = 0;
        coluna = 0;
        ordem = 0;
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
     * @return {@link alog.token.Token}
     */
    public Token getNext(){
        int contLit = 0, contAlpha = 0, contNum = 0, contDelim = 0, contOper = 0; 
        
        boolean literal = false;
        boolean go = true;
        Token token = new Token(linha, coluna, posicao, ordem++);
        
        while (go && next && indice < len){
            if (indice + 1 == len){
                next = false;
            }
            char ch = texto[indice];
            if (literal){
                if (ch == '"'){
                    literal = false;
                    go = false;
                }
                token.atualizaPalavra(ch);
                indice++;
                posicao++;
                contLit++;
            } else {
                if (isBlank(ch)){
                    go = false;
                } else if (Character.isLetter(ch) || ch == '_'){
                    if (contDelim > 0 || contOper > 0){
                        go = false;
                    } else {
                        token.atualizaPalavra(ch);
                        indice++;
                        posicao++;
                        coluna ++;
                        contAlpha ++;
                    }
                } else if (Character.isDigit(ch)){
                    if (contDelim > 0 || contOper > 0){
                        go = false;
                    } else {
                        token.atualizaPalavra(ch);
                        indice++;
                        posicao++;
                        coluna ++;
                        contNum ++;
                    }
                } else if (isDelimiter(ch)){
                    if (token.getTamanho() == 0){
                        token.atualizaPalavra(ch);
                        indice++;
                        posicao++;
                        coluna ++;
                        contDelim ++;
                    }
                    go = false;
                } else if (ch == '"'){
                    if (token.getTamanho() == 0){
                        literal = true;
                        token.atualizaPalavra(ch);
                        indice++;
                        posicao++;
                        coluna++;
                    } else {
                        go = false;
                    }
                } else if (isOperator(ch)){
                    if (contAlpha > 0 || contNum > 0){
                        go = false;
                    } else {
                        token.atualizaPalavra(ch);
                        indice++;
                        coluna++;
                        posicao++;
                        contOper++;
                    }
                } else {
                    System.out.println("Caractere não considerável: "
                            + ch + " (" + (int)ch + ")");
                    token.atualizaPalavra(ch);
                    go = false;
                    indice++;
                    posicao++;
                    coluna++;
                }
            }
        }
        go = true;
        while (go && indice < len){
            char ch = texto[indice];
            if (ch == '\r') {
                if (indice < len && texto[indice+1] == '\n'){
                    indice++;
                    continue;
                } else {
                    ch = '\n';
                }
            }
            switch (ch) {
                case '\n':
                    indice ++;
                    coluna = 0;
                    linha ++;
                    posicao++;
                    break;
                case ' ':
                    indice ++;
                    posicao++;
                    coluna ++;
                    break;
                case '\t':
                    indice ++;
                    posicao += 4;
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
                    if (contDelim > 0){
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
        
        if (indice == len){
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
    private static boolean isDelimiter(char ch){
        char[] delimiter = {':',';','(',')','[',']',',','.'};
        return isListed(ch, delimiter);
    }
    
    /**
     * Retorna se um determinado caracter é um operador (aritmético ou relacional).
     * @param ch Caracter a verificar
     * @return
     */
    private static boolean isOperator(char ch){
        char[] operators = {'+','-','*','/','>','<','='};
        return isListed(ch, operators);
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
