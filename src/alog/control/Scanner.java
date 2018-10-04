package alog.control;

import alog.analise.Erro;
import alog.analise.TipoErro;
import alog.token.CategoriaToken;
import alog.token.FuncaoToken;
import alog.token.TipoToken;
import alog.token.Token;
import java.util.LinkedList;
import java.util.List;

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
    
    private Token last;
    private LinkedList<Erro> erros;

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
        
        last = null;
        erros = new LinkedList<>();
        if (len <= 0){
            erros.add(new Erro(TipoErro.ALERTA, ' ', 0, 0, 0, "Nenhum token encontrado"));
        }
    }
    
    /**
     * Retorna se ainda há algum token a ser verificado no código.
     * @return True se ainda há tokens.
     */
    public boolean existeProximo(){
        return next;
    }
    
    /**
     * Retorna o próximo token encontrado, devidamente classificado.
     * Caso não haja próximo token, retorna um token com palavra vazia e posição igual ao tamanho da entrada.
     * @return {@link alog.token.Token}
     */
    public Token proximo(){
        int contLit = 0; // Contagem de caracteres dentro da string literal
        int contAlpha = 0; // Contagem de caracteres alfabéticos
        int contNum = 0; // Contagem de caracteres numéricos
        int contDelim = 0; // Contagem de caracteres símbolos delimitadores
        int contOper = 0; // Contagem de caracteres símbolos operadores
        
        boolean literal = false;
        boolean go = true;
        Token token = new Token(linha, coluna, posicao, ordem++);
        
        while (go && next && indice < len){
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
                } else if (isLetter(ch)){
                    if (contDelim > 0 || contOper > 0){
                        go = false;
                    } else {
                        token.atualizaPalavra(ch);
                        indice++;
                        posicao++;
                        coluna ++;
                        contAlpha ++;
                    }
                } else if (isNumeric(ch)){
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
                    erros.add(new Erro(
                            ch, linha, coluna, posicao, "inválido"));
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
            if (contAlpha > 0){
                if (contNum > 0) {
                    token.setTipoToken(TipoToken.ALFANUMERICO);
                } else {
                    token.setTipoToken(TipoToken.ALFABETICO);
                }
            } else {
                if (contNum > 0){
                    Scanner scannerInterno = new Scanner(new String(texto));
                    scannerInterno.indice = this.indice;
                    scannerInterno.posicao = this.posicao;
                    scannerInterno.linha = this.linha;
                    scannerInterno.coluna = this.coluna;
                    scannerInterno.ordem = this.ordem;
                    
                    Token prox1 = scannerInterno.proximo();
                    Token prox2 = scannerInterno.proximo();
                    if (prox1.getFuncaoToken() == FuncaoToken.DELIM_PONTO) {
                        token.atualizaPalavra(prox1.getPalavra());
                        token.atualizaPalavra(prox2.getPalavra());
                        this.indice = scannerInterno.indice;
                        this.posicao = scannerInterno.posicao;
                        this.linha = scannerInterno.linha;
                        this.coluna = scannerInterno.coluna;
                        this.ordem = scannerInterno.ordem;
                        if (prox2.getFuncaoToken() == FuncaoToken.CONST_INTEIRA){
                            token.setTipoToken(TipoToken.NUMERICO);
                        } else {
                            token.setTipoToken(TipoToken.INDEFINIDO);
                        }
                    } else {
                        token.setTipoToken(TipoToken.NUMERICO);
                    }
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
        
        switch (token.getFuncaoToken()){
            case OP_MAT_SUBTRACAO:
                if (last != null &&
                    last.getFuncaoToken().getCategoria() == CategoriaToken.OPERADOR) {
                    token.setFuncaoToken(FuncaoToken.OP_SIG_NEGATIVO);
                }
                break;
            case OP_MAT_SOMA:
                if (last != null &&
                    last.getFuncaoToken().getCategoria() == CategoriaToken.OPERADOR) {
                    token.setFuncaoToken(FuncaoToken.OP_SIG_POSITIVO);
                }
                break;
        }
        
        if (indice == len){
            next = false;
        }
        
        last = token;
        return token;
    }
    
    /**
     * Retorna todos os Tokens encontrados no texto.
     * Esse método cria uma lista e executa o método {@link #proximo() }
     * até que não haja mais tokens a serem lidos
     * (ou seja, quando o método {@link #existeProximo()} retornar {@code FALSE}.
     * @return ArrayList com tokens
     */
    public List<Token> listaTokens (){
        LinkedList<Token> list = new LinkedList<>();
        while (existeProximo()){
            Token proximo = proximo();
            list.add(proximo);
        }
        return list;
    }
    
    /**
     * Retorna número de erros de análise léxica.
     * @return 
     */
    public int getNumErros(){
        return erros.size();
    }
    
    /**
     * Retorna lista com mensagens de erro que ocorreram durante a análise.
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
    public List<String> getListaErros(){
        LinkedList<String> err = new LinkedList<>();
        for (Erro e : erros){
            err.add(e.toString());
        }
        return err;
    }
    
    /**
     * Retorna lista com mensagens de erro que ocorreram durante a análise.
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
    public String imprimeErros(){
        StringBuilder msg = new StringBuilder();
        for (Erro err : erros){
            if (msg.length() > 0){
                msg.append("\n");
            }
            msg.append(err.toString());
        }
        return msg.toString();
    }
    
    /**
     * Retorna lista com mensagens de erro que ocorreram durante a análise.
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
    public List<Token> retornaErros () {
        LinkedList<Token> tokens = new LinkedList<>();
        for (Erro err : erros){
            Token t = err.getToken();
            tokens.add(t);
        }
        return tokens;
    }
    
    /**
     * Retorna se um determinado caracter é alfabético (A-Z, a-z, '_').
     * @param ch Caracter a verificar
     * @return
     */
    private static boolean isLetter(char ch){
        return Character.isLetter(ch) || ch == '_';
    }
    
    /**
     * Retorna se um determinado caracter é numérico (0-9).
     * @param ch Caracter a verificar
     * @return
     */
    private static boolean isNumeric(char ch){
        return Character.isDigit(ch);
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
