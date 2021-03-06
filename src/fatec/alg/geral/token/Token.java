/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.geral.token;

import java.util.List;

/**
 * Classe que representa um token.
 * Um token é uma unidade de código (palavra, pontuação, número, símbolo, etc).
 * Essa unidade de código simboliza alguma coisa (operação, nome de variável, função, etc).
 * O texto dessa unidade de código, aqui, será chamado de Palavra.
 * Outros atributos, como linha e coluna onde aparece no código-fonte original, também estão presentes.
 * @author Caique
 */
public class Token {
    /**
     * Precedência de operação para quando o Token é um identificador de nome de função (construída ou interna do sistema)
     */
    public static final int PRECEDENCIA_FUNCAO = 10;
    /**
     * Precedência de operação para quando o Token identifica acesso a Array (não implementado ainda)
     */
    public static final int PRECEDENCIA_ARRAY = 10;
    /**
     * Precedência de operação para quando o Token é um operador unário, como - ou +
     */
    public static final int PRECEDENCIA_OP_UNARIO = 9;
    /**
     * Precedência de operação para quando o Token é um operador de negação lógica ("não")
     */
    public static final int PRECEDENCIA_OP_LOGICO_NAO = 9;
    /**
     * Precedência de operação para quando o Token é um operador aritmético de multiplicação (* / div mod)
     */
    public static final int PRECEDENCIA_OP_MULTIPLICACAO = 8;
    /**
     * Precedência de operação para quando o Token é um operador aritmético de soma (+ -)
     */
    public static final int PRECEDENCIA_OP_SOMA = 7;
    /**
     * Precedência de operação para quando o Token é um operador relacional de grandeza ({@code > >= < <=})
     */
    public static final int PRECEDENCIA_OP_RELACIONAL_GRANDEZA = 6;
    /**
     * Precedência de operação para quando o Token é um operador relacional de igualdade ({@code =  <>})
     */
    public static final int PRECEDENCIA_OP_RELACIONAL_IGUALDADE = 5;
    /**
     * Precedência de operação para quando o Token é o operador lógico E
     */
    public static final int PRECEDENCIA_OP_LOGICO_E = 4;
    /**
     * Precedência de operação para quando o Token é o operador lógico OU
     */
    public static final int PRECEDENCIA_OP_LOGICO_OU = 3;
    /**
     * Precedência de operação para quando o Token é o operador de atribuição ({@code <-})
     */
    public static final int PRECEDENCIA_OP_ATRIBUICAO = 2;
    /**
     * Precedência para Tokens que não são operadores ou não tem precedência calculada (variáveis, por exemplo)
     */
    public static final int PRECEDENCIA_INDEFINIDA = 0;
    
    private String palavra;
    private int ordem;
    private int posicao;
    private int linha;
    private int coluna;
    private int tamanho;
    private TipoToken tipoToken;
    private FuncaoToken funcaoToken;
    private int precedencia;
    
    /**
     * Constrói um novo Token vazio (ou seja, cujo atributo Word é vazio).
     */
    public Token() {
        palavra = "";
        tamanho = 0;
        precedencia = PRECEDENCIA_INDEFINIDA;
        funcaoToken = FuncaoToken._INVALIDO;
    }
    
    /**
     * Constrói um novo Token vazio (ou seja, cujo atributo Word é vazio).
     * @param linha Número da linha onde o Token começa
     * @param coluna Número da coluna (caracter) da linha onde o Token começa
     * @param posicao Número da posição no código em que o Token começa
     * @param ordem Número sequencial do Token no código
     */
    public Token(int linha, int coluna, int posicao, int ordem) {
        this.linha = linha;
        this.coluna = coluna;
        this.posicao = posicao;
        this.ordem = ordem;
        
        palavra = "";
        tamanho = 0;
        precedencia = PRECEDENCIA_INDEFINIDA;
        funcaoToken = FuncaoToken._INVALIDO;
    }
    
    /**
     * Constrói um novo Token <b><i><u>inválido</u></i></b> a partir de uma lista de tokens.
     * O conteúdo desse novo Token é a concatenação de todos os itens da lista.
     * <br>O token gerado é para ser usado apenas em <u>exibição</u> de passos ao executar
     * uma instrução ou expressão.
     * <br><b>NÃO EXECUTE ESSE TOKEN.</b> É como usar drogas: você não ganha nada com isso
     * e todo mundo ao seu redor (nesse caso, o programa) só sai perdendo.
     * @param tokens lista de tokens a serem concatenados.
     */
    public Token(List<Token> tokens) {
        precedencia = PRECEDENCIA_INDEFINIDA;
        tipoToken = TipoToken.INDEFINIDO;
        funcaoToken = FuncaoToken._INVALIDO;
        palavra = "";
        tamanho = 0;
        if (!tokens.isEmpty()) {
            Token t = tokens.get(0);
            linha = t.linha;
            coluna = t.coluna;
            posicao = t.posicao;
            ordem = t.ordem;
            tamanho = 0;
            for (Token token : tokens) {
                for (int p = posicao + tamanho; p < token.posicao; p++) {
                    palavra += " ";
                    tamanho ++;
                }
                palavra += token.palavra;
                tamanho += token.palavra.length();
            }
        }
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
     * Concatena uma string à palavra do token e aumenta seu tamanho.
     * @param sub String a concatenar
     */
    public void atualizaPalavra(String sub){
        palavra += sub;
        tamanho += sub.length();
    }
    
    /**
     * Retorna a palavra (Word) do Token.
     * @return Ex. "Inteiro" (para o identificador de tipo).
     */
    public String getPalavra() {
        return palavra;
    }

    /**
     * Redefine a palavra do Token e seu tamanho. O tamanho é obtido pelo método {@link java.lang.String#length() }.
     * @param palavra Texto a redefinir a palavra do token.
     */
    public void setPalavra(String palavra) {
        this.palavra = palavra;
        tamanho = palavra.length();
    }

    /**
     * Retorna a ordem em que o Token está na lista de tokens do programa, iniciada em 0 (zero).
     * Exemplo: Num programa {@code Leia(X); } a ordem é a seguinte:
     *  <table summary="Lista de tokens em ordem">
     *  <tr>
     *      <th>Token</th><th>Tipo</th><th>Ordem</th>
     *  </tr>
     *  <tr><td>Leia</td><td>LIB_IO_LEIA</td><td>0</td></tr>
     *  <tr><td>(</td><td>DELIM_PARENTESES_ABRE</td><td>1</td></tr>
     *  <tr><td>X</td><td>IDENT_NOME_VARIAVEL</td><td>2</td></tr>
     *  <tr><td>)</td><td>DELIM_PARENTESES_FECHA</td><td>3</td></tr>
     *  <tr><td>;</td><td>DELIM_PONTO_VIRGULA</td><td>4</td></tr>
     *  </table> 
     * @return ordem
     */
    public int getOrdem() {
        return ordem;
    }

    /**
     * Define a ordem do token num determinado programa. 
     * Em geral, esse método só é (ou deveria ser somente) usado pelo {@link alog.control.Scanner }.
     * @param ordem Ordem a definir
     */
    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    /**
     * Retorna a linha do programa em que o token está.
     * @return linha
     */
    public int getLinha() {
        return linha;
    }

    /**
     * Define a linha em que o token está no programa. 
     * Em geral, esse método só é (ou deveria ser somente) usado pelo {@link alog.control.Scanner }.
     * @param lin Número da linha
     */
    public void setLinha(int lin) {
        this.linha = lin;
    }

    /**
     * Retorna a coluna (tabulação/espaçamento) do programa onde a palavra do token começa.
     * @return coluna
     */
    public int getColuna() {
        return coluna;
    }

    /**
     * Define a coluna do programa onde a palavra do token começa. 
     * Em geral, esse método só é (ou deveria ser somente) usado pelo {@link alog.control.Scanner }.
     * @param col Número da coluna
     */
    public void setColuna(int col) {
        this.coluna = col;
    }

    /**
     * Retorna a posição (índice de caracter) no programa (como array de char) onde a palavra do token começa.
     * Por exemplo, no programa {@code "Leia (Var);" }, o token "Var" inicia no índice 6.
     * Em geral, esse método só é (ou deveria ser somente) usado pelo {@link alog.control.Parser }.
     * @return Posição.
     */
    public int getPosicao() {
        return posicao;
    }

    /**
     * Define a posição (índice de caracter) no programa (como array de char) onde a palavra do token começa.
     * Em geral, esse método só é (ou deveria ser somente) usado pelo {@link alog.control.Scanner }.
     * @param posicao Posição a definir
     */
    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    /**
     * Retorna o tamanho da palavra do token (em nº de caracteres)
     * @return número de caracteres da palavra do token.
     */
    public int getTamanho() {
        return tamanho;
    }

    /**
     * Retorna a precedência de operação do Token (ou {@link #PRECEDENCIA_INDEFINIDA} caso não seja necessário).
     * <table summary="Lista de precedências de tokens e sua ordem" border="1">
     * <tr><th>Tipo</th><th>Precedência</th><th>Tokens</th><th>Observações</th></tr>
     * <tr><td>{@link #PRECEDENCIA_FUNCAO}</td><td>10</td><td>Leia Escreva Pot Raiz</td><td>Também usado para identificadores de funções/rotinas declaradas</td></tr>
     * <tr><td>{@link #PRECEDENCIA_ARRAY}</td><td>10</td><td>[]</td><td>Acesso a um item de um array</td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_UNARIO}</td><td>9</td><td>- +</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_LOGICO_NAO}</td><td>9</td><td>NÃO</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_MULTIPLICACAO}</td><td>8</td><td>* / div mod</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_SOMA}</td><td>7</td><td>+ -</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_RELACIONAL_GRANDEZA}</td><td>6</td><td>{@code > >= < <= }</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_RELACIONAL_IGUALDADE}</td><td>5</td><td>{@code = <>}</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_LOGICO_E}</td><td>4</td><td>E</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_LOGICO_OU}</td><td>3</td><td>OU</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_OP_ATRIBUICAO}</td><td>2</td><td>{@code <-}</td><td></td></tr>
     * <tr><td>{@link #PRECEDENCIA_INDEFINIDA}</td><td>0</td><td></td><td>Outros identificadores que não são operadores</td></tr>
     * </table> 
     * @return precedência
     */
    public int getPrecedencia(){
        return precedencia;
    }
    
    /**
     * Retorna o {@link TipoToken} desse Token.
     * @return Tipo do token.
     */
    public TipoToken getTipoToken() {
        return tipoToken;
    }
    
    /**
     * Define o tipo do token.
     * Também define a {@link FuncaoToken} ao verificar se a palavra do Token é uma palavra reservada ou operador.
     * @param tipoToken Tipo do Token ao definir.
     */
    public void setTipoToken(TipoToken tipoToken) {
        this.tipoToken = tipoToken;
        
        switch(tipoToken){
            case ALFABETICO:
                switch (palavra.toLowerCase()){
                    case "algoritmo":
                        funcaoToken = FuncaoToken.RES_ALGORITMO;
                        break;
                    case "função":
                    case "funçao":
                    case "funcão":
                    case "funcao":
                        funcaoToken = FuncaoToken.RES_MOD_FUNCAO;
                        precedencia = PRECEDENCIA_FUNCAO;
                        break;
                    case "rotina":
                        funcaoToken = FuncaoToken.RES_MOD_ROTINA;
                        precedencia = PRECEDENCIA_FUNCAO;
                        break;
                    case "retorna":
                        funcaoToken = FuncaoToken.RES_MOD_RETORNA;
                        break;
                    case "inicio":
                    case "início":
                        funcaoToken = FuncaoToken.RES_BLOCO_INICIO;
                        break;
                    case "fim":
                        funcaoToken = FuncaoToken.RES_BLOCO_FIM;
                        break;
                    case "matriz":
                        funcaoToken = FuncaoToken.RES_ED_MATRIZ;
                        break;
                    case "caracter":
                        funcaoToken = FuncaoToken.RES_TIPO_CARACTER;
                        break;
                    case "inteiro":
                        funcaoToken = FuncaoToken.RES_TIPO_INTEIRO;
                        break;
                    case "real":
                        funcaoToken = FuncaoToken.RES_TIPO_REAL;
                        break;
                    case "logico":
                    case "lógico":
                        funcaoToken = FuncaoToken.RES_TIPO_LOGICO;
                        break;
                    case "leia":
                        funcaoToken = FuncaoToken.LIB_IO_LEIA;
                        precedencia = PRECEDENCIA_FUNCAO;
                        break;
                    case "escreva":
                        funcaoToken = FuncaoToken.LIB_IO_ESCREVA;
                        precedencia = PRECEDENCIA_FUNCAO;
                        break;
                    case "div":
                        funcaoToken = FuncaoToken.OP_MAT_DIV_INTEIRA;
                        precedencia = PRECEDENCIA_OP_MULTIPLICACAO;
                        break;
                    case "mod":
                        funcaoToken = FuncaoToken.OP_MAT_MOD;
                        precedencia = PRECEDENCIA_OP_MULTIPLICACAO;
                        break;
                    case "pot":
                        funcaoToken = FuncaoToken.LIB_MATH_POT;
                        precedencia = PRECEDENCIA_FUNCAO;
                        break;
                    case "raiz":
                        funcaoToken = FuncaoToken.LIB_MATH_RAIZ;
                        precedencia = PRECEDENCIA_FUNCAO;
                        break;
                    case "se":
                        funcaoToken = FuncaoToken.RES_COND_SE;
                        break;
                    case "entao":
                    case "então":
                        funcaoToken = FuncaoToken.RES_COND_ENTAO;
                        break;
                    case "senao":
                    case "senão":
                        funcaoToken = FuncaoToken.RES_COND_SENAO;
                        break;
                    case "para":
                        funcaoToken = FuncaoToken.RES_REP_PARA;
                        break;
                    case "de":
                        funcaoToken = FuncaoToken._INDEFINIDO_RES_DE;
                        break;
                    case "ate":
                    case "até":
                        funcaoToken = FuncaoToken.RES_REP_ATE;
                        break;
                    case "passo":
                        funcaoToken = FuncaoToken.RES_REP_PASSO;
                        break;
                    case "faca":
                    case "faça":
                        funcaoToken = FuncaoToken.RES_REP_FACA;
                        break;
                    case "repita":
                        funcaoToken = FuncaoToken.RES_REP_REPITA;
                        break;
                    case "enquanto":
                        funcaoToken = FuncaoToken.RES_REP_ENQUANTO;
                        break;
                    case "e":
                        funcaoToken = FuncaoToken.OP_LOG_E;
                        precedencia = PRECEDENCIA_OP_LOGICO_E;
                        break;
                    case "ou":
                        funcaoToken = FuncaoToken.OP_LOG_OU;
                        precedencia = PRECEDENCIA_OP_LOGICO_OU;
                        break;
                    case "nao":
                    case "não":
                        funcaoToken = FuncaoToken.OP_LOG_NAO;
                        precedencia = PRECEDENCIA_OP_LOGICO_NAO;
                        break;
                    case "verdadeiro":
                    case "falso":
                        funcaoToken = FuncaoToken.CONST_LOGICA;
                        break;
                    default:
                        funcaoToken = FuncaoToken._INDEF_ALFABETICO;
                        break;
                }
                break;
            case OPERADOR:
                switch (palavra){
                    case "<-":
                        funcaoToken = FuncaoToken.OP_ATRIBUICAO;
                        precedencia = PRECEDENCIA_OP_ATRIBUICAO;
                        break;
                    case "+":
                        funcaoToken = FuncaoToken.OP_MAT_SOMA;
                        precedencia = PRECEDENCIA_OP_SOMA;
                        break;
                    case "-":
                        funcaoToken = FuncaoToken.OP_MAT_SUBTRACAO;
                        precedencia = PRECEDENCIA_OP_SOMA;
                        break;
                    case "*":
                        funcaoToken = FuncaoToken.OP_MAT_MULTIPLICACAO;
                        precedencia = PRECEDENCIA_OP_MULTIPLICACAO;
                        break;
                    case "/":
                        funcaoToken = FuncaoToken.OP_MAT_DIV_REAL;
                        precedencia = PRECEDENCIA_OP_MULTIPLICACAO;
                        break;
                    case ">":
                        funcaoToken = FuncaoToken.OP_REL_MAIOR;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_GRANDEZA;
                        break;
                    case "<":
                        funcaoToken = FuncaoToken.OP_REL_MENOR;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_GRANDEZA;
                        break;
                    case ">=":
                        funcaoToken = FuncaoToken.OP_REL_MAIOR_IGUAL;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_GRANDEZA;
                        break;
                    case "<=":
                        funcaoToken = FuncaoToken.OP_REL_MENOR_IGUAL;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_GRANDEZA;
                        break;
                    case "=":
                        funcaoToken = FuncaoToken.OP_REL_IGUAL;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_IGUALDADE;
                        break;
                    case "<>":
                        funcaoToken = FuncaoToken.OP_REL_DIFERENTE;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_IGUALDADE;
                        break;
                    default:
                        funcaoToken = FuncaoToken._INVALIDO;
                        break;
                }
                break;
            case DELIMITADOR:
                switch (palavra){
                    case ":":
                        funcaoToken = FuncaoToken.DELIM_DOIS_PONTOS;
                        break;
                    case ";":
                        funcaoToken = FuncaoToken.DELIM_PONTO_VIRGULA;
                        break;
                    case ".":
                        funcaoToken = FuncaoToken.DELIM_PONTO;
                        break;
                    case ",":
                        funcaoToken = FuncaoToken.DELIM_VIRGULA;
                        break;
                    case "(":
                        funcaoToken = FuncaoToken.DELIM_PARENTESES_ABRE;
                        break;
                    case ")":
                        funcaoToken = FuncaoToken.DELIM_PARENTESES_FECHA;
                        break;
                    case "[":
                        funcaoToken = FuncaoToken.DELIM_COLCHETES_ABRE;
                        break;
                    case "]":
                        funcaoToken = FuncaoToken.DELIM_COLCHETES_FECHA;
                        break;
                    default:
                        funcaoToken = FuncaoToken._INVALIDO;
                        break;
                }
                break;
            case LITERAL:
                funcaoToken = FuncaoToken.CONST_CARACTER;
                break;
            case ALFANUMERICO:
                funcaoToken = FuncaoToken._INDEF_ALFANUMERICO;
                break;
            case NUMERICO:
                int ponto = palavra.indexOf(".");
                if (ponto > 0 && ponto < palavra.length() - 1) {
                    funcaoToken = FuncaoToken.CONST_REAL;
                } else {
                    funcaoToken = FuncaoToken.CONST_INTEIRA;
                }
                break;
            default:
                funcaoToken = FuncaoToken._INVALIDO;
                break;
        }
    }

    /**
     * Retorna a função do Token.
     * @return função
     */
    public FuncaoToken getFuncaoToken(){
        return funcaoToken;
    }
    
    /**
     * Define manualmente a função do token. 
     * @param func A função do token a definir.
     */
    public void setFuncaoToken(FuncaoToken func){
        this.funcaoToken = func;
        switch (func) {
            case IDENT_NOME_FUNCAO:
            case IDENT_NOME_ROTINA:
                precedencia = PRECEDENCIA_FUNCAO;
                break;
            case OP_SIG_NEGATIVO:
            case OP_SIG_POSITIVO:
                precedencia = PRECEDENCIA_OP_UNARIO;
                break;
        }
    }
    
    public String nome(){
        return palavra.toLowerCase();
    }
    
    /**
     * Imprime a função do token seguida da palavra
     * @return "DELIM_BLOCO_INICIO: Início"
     */
    @Override
    public String toString(){
        return funcaoToken + ": " + palavra;
    }
    
    /**
     * Descreve o token e sua posição.
     * @return "Leia (pos 7 na linha 3, coluna 5 - carac. no. 20)"
     */
    public String getDescricao(){
        return palavra + " (pos " + (ordem + 1) + " na linha " + (linha + 1) + ", coluna " + (coluna + 1) + " - carac. no. " + posicao + ")";
    }
}
