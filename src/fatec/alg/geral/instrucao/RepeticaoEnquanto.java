package fatec.alg.geral.instrucao;

import fatec.alg.geral.token.Token;

/**
 * Instrução de estrutura repetitiva "Enquanto" - repetição com número indefinido
 * de iterações (enquanto a condicional for verdadeira) e teste no início.
 * @author Caique Souza
 */
public class RepeticaoEnquanto extends EstruturaControle {
    private Token tokenEnquanto;
    private Token tokenFaca;
    
    /**
     * Instancia uma instrução com tipo REPETICAO_ENQUANTO
     */
    public RepeticaoEnquanto() {
        super();
        tipo = TipoInstrucao.REPETICAO_ENQUANTO;
    }

    /**
     * Define o token reservado Enquanto
     * @param tokenEnquanto  token
     */
    public void setTokenEnquanto(Token tokenEnquanto) {
        this.tokenEnquanto = tokenEnquanto;
        super.addToken(tokenEnquanto);
    }

    /**
     * Retorna o token reservado Enquanto
     * @return token
     */
    public Token getTokenEnquanto() {
        return tokenEnquanto;
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
     * Retorna o token reservado Faça
     * @return token
     */
    public Token getTokenFaca() {
        return tokenFaca;
    }
    
    /**
     * Valida se o token Enquanto não é nulo e se a condição e a instrução são válidas.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            valida =
                tokenEnquanto != null &&
                (condicao != null && condicao.isValida()) &&
                (instrucao != null && instrucao.isValida());
        }
    }
}
