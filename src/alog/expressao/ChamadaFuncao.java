/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.expressao;

import alog.token.Token;
import java.util.LinkedList;

/**
 *
 * @author Caique Souza
 */
public class ChamadaFuncao extends Operando {
    
    private LinkedList<Expressao> parametros;
    private int numParametros;
    
    public ChamadaFuncao() {
        super();
        tipoExpressao = TipoExpressao.OPERANDO_FUNCAO;
        parametros = new LinkedList<>();
        numParametros = 0;
    }

    public void setTokenNome(Token token){
        super.setOperando(token);
    }
    
    public Token getTokenNome() {
        return super.getOperando();
    }
    
    public void addParametro(Expressao expressao) {
        parametros.add(expressao);
        numParametros ++;
        for (Token token : expressao.listaTokens()){
            super.addToken(token);
        }
    }
    
    @Override
    public String imprimeExpressao (){
        String params = "";
        for (Expressao ex : parametros) {
            if (!params.isEmpty()) params += " , ";
            params += ex.imprimeExpressao();
        }
        return "< " + 
                (parentesesAbre == null ? "" : "( ") +
                operando.getPalavra() + " ( " + params + ")" +
                (parentesesFecha == null ? "" : " )") +
                " >";
    }

    public LinkedList<Expressao> getParametros() {
        return parametros;
    }
    
    public void atualizaParametro(int pos, Expressao expressao) {
        parametros.set(pos, expressao);
    }
    
    public int getNumParametros () {
        return numParametros;
    }
    
    @Override
    public void finaliza() {
        if (valida) {
            int numValidos = 0;
            for (Expressao expressao : parametros) {
                if (expressao.isValida()){
                    numValidos ++;
                }
            }

            valida = 
                    operando != null &&
                    numValidos == numParametros;
        }
    }
}
