package fatec.alg.geral.expressao;

import fatec.alg.geral.token.Token;

/**
 * Expressão de operações binárias.
 * @author Caique Souza
 */
public class Operacao extends Expressao {
    
    private Expressao expressaoEsq;
    private Expressao expressaoDir;
    private Token operador;

    /**
     * Instancia uma expressão sem filhos e tipo indefinido.
     * O tipo será definido de acordo com o operador.
     */
    public Operacao() {
        super();
        expressaoEsq = null;
        expressaoDir = null;
    }

    /**
     * Retorna a expressão à esquerda do operador.
     * @return expressão esquerda
     */
    public Expressao getExpressaoEsq() {
        return expressaoEsq;
    }

    /**
     * Define a expressão à esquerda do operador
     * @param expressaoEsq expressão
     */
    public void setExpressaoEsq(Expressao expressaoEsq) {
        this.expressaoEsq = expressaoEsq;
        defineTexto();
    }

    /**
     * Retorna a expressão à direita do operador.
     * @return expressão direita
     */
    public Expressao getExpressaoDir() {
        return expressaoDir;
    }

    /**
     * Define a expressão à direita do operador
     * @param expressaoDir expressão
     */
    public void setExpressaoDir(Expressao expressaoDir) {
        this.expressaoDir = expressaoDir;
        defineTexto();
    }
    
    /**
     * Atualiza a expressão à esquerda do operador
     * @param expressao expressão
     */
    public void atualizaExpressaoEsq(Expressao expressao) {
        this.expressaoEsq = expressao;
    }

    /**
     * Atualiza a expressão à direita do operador
     * @param expressao expressão
     */
    public void atualizaExpressaoDir(Expressao expressao) {
        this.expressaoDir = expressao;
    }
    
    /**
     * Retorna o token que identifica o operador da instrução
     * @return token
     */
    public Token getOperador() {
        return operador;
    }

    /**
     * Define o operador da expressão e define o tipo da expressão
     * (aritmética, relacional ou lógica)
     * @param operador token
     */
    public void setOperador(Token operador) {
        this.operador = operador;
        defineTexto();
        switch (operador.getFuncaoToken()) {
            case OP_MAT_SOMA:
            case OP_MAT_SUBTRACAO:
            case OP_MAT_MULTIPLICACAO:
            case OP_MAT_DIV_REAL:
            case OP_MAT_DIV_INTEIRA:
            case OP_MAT_MOD:
                tipoExpressao = TipoExpressao.OPERACAO_ARITMETICA;
                break;
            case OP_REL_MAIOR:
            case OP_REL_MAIOR_IGUAL:
            case OP_REL_MENOR:
            case OP_REL_MENOR_IGUAL:
            case OP_REL_IGUAL:
            case OP_REL_DIFERENTE:
                tipoExpressao = TipoExpressao.OPERACAO_RELACIONAL;
                break;
            case OP_LOG_E:
            case OP_LOG_OU:
                tipoExpressao = TipoExpressao.OPERACAO_LOGICA;
                break;
            default:
                tipoExpressao = TipoExpressao._INDEFINIDO;
                break;
        }
    }
    
    private void defineTexto(){
        if (expressaoEsq != null && operador != null && expressaoDir != null) {
            for (Token t : expressaoEsq.listaTokens()) {
                super.addToken(t);
            }
            super.addToken(operador);
            for (Token t : expressaoDir.listaTokens()) {
                super.addToken(t);
            }
        }
    }
    
    /**
     * Imprime a expressão recursivamente.
     * @return "&lt;Expressão Esquerda&gt; &lt;Operador&gt; &lt;Expressão Direita&gt;"
     */
    @Override
    public String imprimeExpressao (){
        return "< " + 
                (parentesesAbre == null ? "" : "( ") +
                expressaoEsq.imprimeExpressao() + 
                " " + operador.getPalavra() + " " +
                expressaoDir.imprimeExpressao() + 
                (parentesesFecha == null ? "" : " )") +
                " >";
    }

    @Override
    public void redefine() {
        super.redefine();
        expressaoEsq.redefine();
        expressaoDir.redefine();
    }
}
