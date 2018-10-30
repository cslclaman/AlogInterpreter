/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.geral.expressao;

import fatec.alg.geral.token.Token;

/**
 *
 * @author Caique
 */
public class TokenDelimitador extends Expressao {
    private Token delimitador;
    
    public TokenDelimitador() {
        super();
    }

    public Token getDelimitador() {
        return delimitador;
    }

    public void setDelimitador(Token delimitador) {
        this.delimitador = delimitador;
        super.addToken(delimitador);
    }

    @Override
    public String imprimeExpressao() {
        return delimitador.getPalavra();
    }
    
}
