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
        pos = posnolf = linha = coluna = order = 0;
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
        Token token = new Token();
        token.setLinha(linha);
        token.setPosicao(posnolf);
        token.setColuna(coluna);
        token.setOrdem(order++);
        
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
                } else if (isLogicalOperator(ch) || isArithmeticOperator(ch)){
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
                    System.out.println("Caractere não considerável: " + ch);
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
    public ArrayList<Token> getAll(){
        ArrayList<Token> list = new ArrayList<>();
        while (hasNext()){
            list.add(getNext());
        }
        return list;
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
