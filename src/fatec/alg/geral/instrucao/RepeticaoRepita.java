/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.geral.instrucao;

import fatec.alg.geral.token.Token;

/**
 * Instrução de estrutura repetitiva "Repita" - repetição com número indefinido
 * de iterações (enquanto a condicional for falsa) e teste no final.
 * @author Caique Souza
 */
public class RepeticaoRepita extends EstruturaControle {
    private Token tokenRepita;
    private Token tokenAte;

    /**
     * Instancia uma instrução com tipo REPETICAO_REPITA
     */
    public RepeticaoRepita() {
        super();
        this.tipo = TipoInstrucao.REPETICAO_REPITA;
    }

    /**
     * Retorna o token reservado Repita
     * @return token
     */
    public Token getTokenRepita() {
        return tokenRepita;
    }

    /**
     * Define o token reservado Repita
     * @param tokenRepita token
     */
    public void setTokenRepita(Token tokenRepita) {
        this.tokenRepita = tokenRepita;
        super.addToken(tokenRepita);
    }

    /**
     * Retorna o token reservado Até
     * @return token
     */
    public Token getTokenAte() {
        return tokenAte;
    }

    /**
     * Define o token reservado Até
     * @param tokenAte token
     */
    public void setTokenAte(Token tokenAte) {
        this.tokenAte = tokenAte;
        texto.append("\n");
        super.addToken(tokenAte);
    }
    
    /**
     * Valida se o token Repita não é nulo e se a condição e a instrução são válidas.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            valida =
                tokenRepita != null &&
                (condicao != null && condicao.isValida()) &&
                (instrucao != null && instrucao.isValida());
        }
    }
}
