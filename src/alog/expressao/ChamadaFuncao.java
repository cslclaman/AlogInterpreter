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
    
    protected LinkedList<Expressao> parametros;
    
    public ChamadaFuncao() {
        super();
        tipoExpressao = TipoExpressao.OPERANDO_FUNCAO;
        parametros = new LinkedList<>();
    }

    public void setTokenNome(Token token){
        super.setOperando(token);
    }
    
    public Token getTokenNome() {
        return super.getOperando();
    }
    
    public void addParametro(Expressao expressao) {
        parametros.add(expressao);
        for (Token token : expressao.listaTokens()){
            super.addToken(token);
        }
    }
    
    @Override
    public boolean instrucaoValida() {
        if (operando == null || parametros.isEmpty()){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
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
    
}
