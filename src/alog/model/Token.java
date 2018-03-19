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
    private String palavra;
    private int ordem;
    private int posicao;
    private int lin;
    private int col;
    private int tamanho;
    private TipoToken tipoToken;
    private FuncaoToken funcaoToken;
    
    /**
     * Constrói um novo Token vazio (ou seja, cujo atributo Word é vazio).
     */
    public Token() {
        palavra = "";
        tamanho = 0;
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
                        funcaoToken = FuncaoToken.IDENT_TIPO_CARACTER;
                        break;
                    case "inteiro":
                        funcaoToken = FuncaoToken.IDENT_TIPO_INTEIRO;
                        break;
                    case "real":
                        funcaoToken = FuncaoToken.IDENT_TIPO_REAL;
                        break;
                    case "leia":
                        funcaoToken = FuncaoToken.LIB_IO_LEIA;
                        break;
                    case "escreva":
                        funcaoToken = FuncaoToken.LIB_IO_ESCREVA;
                        break;
                    case "div":
                        funcaoToken = FuncaoToken.OP_DIV_INTEIRA;
                        break;
                    case "mod":
                        funcaoToken = FuncaoToken.OP_MOD;
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
                        break;
                    case "+":
                        funcaoToken = FuncaoToken.OP_SOMA;
                        break;
                    case "-":
                        funcaoToken = FuncaoToken.OP_SUBTRACAO;
                        break;
                    case "*":
                        funcaoToken = FuncaoToken.OP_MULTIPLICACAO;
                        break;
                    case "/":
                        funcaoToken = FuncaoToken.OP_DIV_REAL;
                        break;
                    case ">":
                        funcaoToken = FuncaoToken.OP_MAIOR;
                        break;
                    case "<":
                        funcaoToken = FuncaoToken.OP_MENOR;
                        break;
                    case ">=":
                        funcaoToken = FuncaoToken.OP_MAIOR_IGUAL;
                        break;
                    case "<=":
                        funcaoToken = FuncaoToken.OP_MENOR_IGUAL;
                        break;
                    case "<>":
                        funcaoToken = FuncaoToken.OP_DIFERENTE;
                        break;
                    case "=":
                        funcaoToken = FuncaoToken.OP_IGUAL;
                        break;
                    default:
                        funcaoToken = FuncaoToken._INDEFINIDO;
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
                        funcaoToken = FuncaoToken._INDEFINIDO;
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
                funcaoToken = FuncaoToken._INDEFINIDO;
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
