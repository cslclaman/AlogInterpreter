package fatec.alg.geral.instrucao;

import fatec.alg.geral.token.Token;

/**
 * Instrução de estrutura repetitiva "Faça" - repetição com número indefinido
 * de iterações (enquanto a condicional for verdadeira) e teste no final.
 * @author Caique Souza
 */
public class RepeticaoFaca extends EstruturaControle {
    private Token tokenFaca;
    private Token tokenEnquanto;

    /**
     * Instancia uma instrução com tipo REPETICAO_FACA
     */
    public RepeticaoFaca() {
        super();
        this.tipo = TipoInstrucao.REPETICAO_FACA;
    }

    /**
     * Retorna o token reservado Faça
     * @return token
     */
    public Token getTokenFaca() {
        return tokenFaca;
    }

    /**
     * Define o token reservado Faça
     * @param tokenFaca token
     */
    public void setTokenFaca(Token tokenFaca) {
        this.tokenFaca = tokenFaca;
        super.addToken(tokenFaca);
    }

    /**
     * Retorna o token reservado Enquanto
     * @return token
     */
    public Token getTokenEnquanto() {
        return tokenEnquanto;
    }

    /**
     * Define o token reservado Enquanto
     * @param tokenEnquanto token
     */
    public void setTokenEnquanto(Token tokenEnquanto) {
        this.tokenEnquanto = tokenEnquanto;
        texto.append("\n");
        super.addToken(tokenEnquanto);
    }
    
    /**
     * Valida se o token Faça não é nulo e se a condição e a instrução são válidas.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            valida =
                tokenFaca != null &&
                (condicao != null && condicao.isValida()) &&
                (instrucao != null && instrucao.isValida());
        }
    }
}
