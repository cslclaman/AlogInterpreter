/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.expressao;

import alog.instrucao.TipoInstrucao;
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
        tipo = TipoInstrucao.CHAMADA_ROTINA;
        tipoExpressao = TipoExpressao.OPERANDO;
        parametros = new LinkedList<>();
    }

    public void setTokenNome(Token token){
        super.setOperando(token);
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
    public String toString () {
        return texto.toString();
    }
}
