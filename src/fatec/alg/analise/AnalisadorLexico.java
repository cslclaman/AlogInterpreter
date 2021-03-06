package fatec.alg.analise;

import fatec.alg.geral.log.TipoErro;
import fatec.alg.geral.log.Erro;
import fatec.alg.geral.token.TipoToken;
import fatec.alg.geral.token.FuncaoToken;
import fatec.alg.geral.token.CategoriaToken;
import fatec.alg.geral.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe de scanner que realiza análise léxica em um código
 * e retorna os tokens por meio de um iterador.
 * @author Caique
 */
public class AnalisadorLexico extends Verificador {
    
    private int indice;
    private int posicao;
    private int ordem;
    private int linha;
    private int coluna;
    private int tamanho;
    private boolean next;
    private char[] texto;
    
    private Token last;

    /**
     * Constrói uma nova instância do Scanner a partir de uma String de código-fonte.
     * @param texto Código a ser escaneado
     */
    public AnalisadorLexico(String texto) {
        super();
        this.texto = texto.toCharArray();
        tamanho = texto.length();
        indice = 0;
        posicao = 0;
        linha = 0;
        coluna = 0;
        ordem = 0;
        next = tamanho > 0;
        
        last = null;
        if (tamanho <= 0){
            erros.add(new Erro(TipoErro.ERRO, ' ', 0, 0, 0, "Nenhum token encontrado"));
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
        int contInv = 0; // Contagem de caracteres inválidos
        
        boolean literal = false;
        boolean go = true;
        Token token = new Token(linha, coluna, posicao, ordem++);
        
        while (go && next && indice < tamanho){
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
                    erros.add(new Erro(TipoErro.ALERTA, ch, linha, coluna, posicao, 
                            String.format("Caracter inválido: %c (%d)", ch, (int)ch)));
                    token.atualizaPalavra(ch);
                    go = false;
                    indice++;
                    posicao++;
                    coluna++;
                    contInv ++;
                }
            }
        }
        go = true;
        while (go && indice < tamanho){
            char ch = texto[indice];
            if (ch == '\r') {
                if (indice < tamanho && texto[indice+1] == '\n'){
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
        if (contInv > 0) {
            token.setTipoToken(TipoToken.INDEFINIDO);
        } else {
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
                        AnalisadorLexico scannerInterno = new AnalisadorLexico(new String(texto));
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
        } 
        
        switch (token.getFuncaoToken()){
            case OP_MAT_SUBTRACAO:
                if (last != null && (
                    last.getFuncaoToken().getCategoria() == CategoriaToken.PALAVRA_RESERVADA ||
                    last.getFuncaoToken().getCategoria() == CategoriaToken.OPERADOR ||
                    last.getFuncaoToken() == FuncaoToken.DELIM_COLCHETES_ABRE ||
                    last.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE ||
                    last.getFuncaoToken() == FuncaoToken.DELIM_VIRGULA )
                ) {
                    token.setFuncaoToken(FuncaoToken.OP_SIG_NEGATIVO);
                }
                break;
            case OP_MAT_SOMA:
                if (last != null && (
                    last.getFuncaoToken().getCategoria() == CategoriaToken.PALAVRA_RESERVADA ||
                    last.getFuncaoToken().getCategoria() == CategoriaToken.OPERADOR ||
                    last.getFuncaoToken() == FuncaoToken.DELIM_COLCHETES_ABRE ||
                    last.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE ||
                    last.getFuncaoToken() == FuncaoToken.DELIM_VIRGULA )
                ) {
                    token.setFuncaoToken(FuncaoToken.OP_SIG_POSITIVO);
                }
                break;
        }
        
        if (indice == tamanho){
            next = false;
        }
        
        last = token;
        return token;
    }
    
    /**
     * Retorna todos os Tokens encontrados no texto.
     * Esse método cria uma lista e executa o método {@link #proximo() }
     * até que não haja mais tokens a serem lidos
     * (ou seja, quando o método {@link #existeProximo()} retornar {@code FALSE}).
     * @return lista com tokens
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
