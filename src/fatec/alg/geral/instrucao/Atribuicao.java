package fatec.alg.geral.instrucao;

import fatec.alg.geral.expressao.Expressao;
import fatec.alg.geral.token.Token;

/**
 * Instrução que atribui a uma variável o resultado de uma expressão.
 * @author Caique
 */
public class Atribuicao extends Instrucao {
    
    private Token variavel;
    private Expressao expressao;

    /**
     * Instancia uma nova instrução com tipo Atribuição.
     */
    public Atribuicao() {
        super();
        tipo = TipoInstrucao.ATRIBUICAO;
    }
    
    /**
     * Retorna a variável da atribuição.
     * @return Token Identificador de Variável
     */
    public Token getVariavel() {
        return variavel;
    }

    /**
     * Define o token que identifica a variável da atribuição.
     * @param variavel Token Identificador de Variável
     */
    public void setVariavel(Token variavel) {
        this.variavel = variavel;
        super.addToken(variavel);
    }

    /**
     * Retorna a expressão cujo valor será atribuído à variável.
     * @return Expressão (constante, variável ou operação)
     */
    public Expressao getExpressao() {
        return expressao;
    }

    /**
     * Define a expressão cujo valor será atribuído à variável.
     * @param expressao Expressão (constante, variável ou operação)
     */
    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
        for (Token token : expressao.listaTokens()){
            super.addToken(token);
        }
    }

    /**
     * Valida a atribuição: verifica se a variável não é nula e se a expressão
     * é uma expressão válida.
     */
    @Override
    public void finaliza() {
        if (valida) {
            valida = 
                variavel != null &&
                expressao != null &&
                expressao.isValida();
        }
    }
    
}
