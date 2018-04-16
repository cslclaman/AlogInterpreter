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
    public static final int PRECEDENCIA_FUNCAO = 10;
    public static final int PRECEDENCIA_ARRAY = 10;
    public static final int PRECEDENCIA_OP_UNARIO = 9;
    public static final int PRECEDENCIA_OP_MULTIPLICACAO = 8;
    public static final int PRECEDENCIA_OP_SOMA = 7;
    public static final int PRECEDENCIA_OP_RELACIONAL_GRANDEZA = 6;
    public static final int PRECEDENCIA_OP_RELACIONAL_IGUALDADE = 5;
    public static final int PRECEDENCIA_OP_LOGICO_E = 4;
    public static final int PRECEDENCIA_OP_LOGICO_OU = 3;
    public static final int PRECEDENCIA_OP_ATRIBUICAO = 2;
    public static final int PRECEDENCIA_INDEFINIDA = 0;
    
    private String palavra;
    private int ordem;
    private int posicao;
    private int lin;
    private int col;
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
    }

    /**
     * Adiciona um novo caracter à palavra do token e aumenta seu tamanho.
     * @param ch Caracter a ser adicionado.
     */
    public void atualizaPalavra(char ch){
        palavra += ch;
        tamanho ++;
    }
    
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

    public int getPrecedencia(){
        return precedencia;
    }
    
    public TipoToken getTipoToken() {
        return tipoToken;
    }
    
    public void setTipoToken(TipoToken tipoToken) {
        this.tipoToken = tipoToken;
        
        switch(tipoToken){
            case ALFABETICO:
                switch (palavra.toLowerCase()){
                    case "inicio":
                    case "início":
                        funcaoToken = FuncaoToken.RES_BLOCO_INICIO;
                        break;
                    case "fim":
                        funcaoToken = FuncaoToken.RES_BLOCO_FIM;
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
                    case "leia":
                        funcaoToken = FuncaoToken.LIB_IO_LEIA;
                        precedencia = PRECEDENCIA_FUNCAO;
                        break;
                    case "escreva":
                        funcaoToken = FuncaoToken.LIB_IO_ESCREVA;
                        precedencia = PRECEDENCIA_FUNCAO;
                        break;
                    case "div":
                        funcaoToken = FuncaoToken.OP_DIV_INTEIRA;
                        precedencia = PRECEDENCIA_OP_MULTIPLICACAO;
                        break;
                    case "mod":
                        funcaoToken = FuncaoToken.OP_MOD;
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
                    case "e":
                        funcaoToken = FuncaoToken.OP_E;
                        precedencia = PRECEDENCIA_OP_LOGICO_E;
                        break;
                    case "ou":
                        funcaoToken = FuncaoToken.OP_OU;
                        precedencia = PRECEDENCIA_OP_LOGICO_OU;
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
                        funcaoToken = FuncaoToken.OP_SOMA;
                        precedencia = PRECEDENCIA_OP_SOMA;
                        break;
                    case "-":
                        funcaoToken = FuncaoToken.OP_SUBTRACAO;
                        precedencia = PRECEDENCIA_OP_SOMA;
                        break;
                    case "*":
                        funcaoToken = FuncaoToken.OP_MULTIPLICACAO;
                        precedencia = PRECEDENCIA_OP_MULTIPLICACAO;
                        break;
                    case "/":
                        funcaoToken = FuncaoToken.OP_DIV_REAL;
                        precedencia = PRECEDENCIA_OP_MULTIPLICACAO;
                        break;
                    case ">":
                        funcaoToken = FuncaoToken.OP_MAIOR;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_GRANDEZA;
                        break;
                    case "<":
                        funcaoToken = FuncaoToken.OP_MENOR;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_GRANDEZA;
                        break;
                    case ">=":
                        funcaoToken = FuncaoToken.OP_MAIOR_IGUAL;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_GRANDEZA;
                        break;
                    case "<=":
                        funcaoToken = FuncaoToken.OP_MENOR_IGUAL;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_GRANDEZA;
                        break;
                    case "<>":
                        funcaoToken = FuncaoToken.OP_DIFERENTE;
                        precedencia = PRECEDENCIA_OP_RELACIONAL_IGUALDADE;
                        break;
                    case "=":
                        funcaoToken = FuncaoToken.OP_IGUAL;
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
                    case ",":
                        funcaoToken = FuncaoToken.DELIM_VIRGULA;
                        break;
                    case ";":
                        funcaoToken = FuncaoToken.DELIM_PONTO_VIRGULA;
                        break;
                    case "(":
                        funcaoToken = FuncaoToken.DELIM_PARENTESES_ABRE;
                        break;
                    case ")":
                        funcaoToken = FuncaoToken.DELIM_PARENTESES_FECHA;
                        break;
                    case ".":
                        funcaoToken = FuncaoToken.DELIM_PONTO;
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
                funcaoToken = FuncaoToken._INDEF_NUMERICO;
                break;
            default:
                funcaoToken = FuncaoToken._INVALIDO;
                break;
        }
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
