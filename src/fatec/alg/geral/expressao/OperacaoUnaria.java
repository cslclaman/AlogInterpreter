/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.geral.expressao;

import fatec.alg.geral.token.Token;

/**
 *
 * @author Caique Souza
 */
public class OperacaoUnaria extends Expressao {
    
    private Expressao expressao;
    private Token operador;

    public OperacaoUnaria() {
        super();
        expressao = null;
        tipoExpressao = TipoExpressao.OPERACAO_UNARIA;
    }

    public Expressao getExpressao() {
        return expressao;
    }

    public void setExpressao(Expressao expressao) {
        this.expressao = expressao;
        defineTexto();
    }
    
    public void atualizaExpressao(Expressao expressao) {
        this.expressao = expressao;
    }

    public Token getOperador() {
        return operador;
    }

    public void setOperador(Token operador) {
        this.operador = operador;
        defineTexto();
    }
    
    private void defineTexto(){
        if (operador != null && expressao != null) {
            super.addToken(operador);
            for (Token t : expressao.listaTokens()) {
                super.addToken(t);
            }
        }
    }
    
    @Override
    public String imprimeExpressao (){
        return "< " + 
                (parentesesAbre == null ? "" : "( ") +
                operador.getPalavra() + " " +
                expressao.imprimeExpressao() + 
                (parentesesFecha == null ? "" : " )") +
                " >";
    }

}
