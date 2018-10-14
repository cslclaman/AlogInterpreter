/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.expressao.Expressao;
import alog.token.Token;
import java.util.LinkedList;

/**
 *
 * @author Caique
 */
public class ChamadaRotina extends Instrucao {

    protected Token nome;
    protected LinkedList<Expressao> parametros;
    protected int numParametros;
    
    public ChamadaRotina() {
        super();
        tipo = TipoInstrucao.CHAMADA_ROTINA;
        parametros = new LinkedList<>();
        numParametros = 0;
    }

    public void setTokenNome(Token token){
        nome = token;
        super.addToken(token);
    }
    
    public void addParametro(Expressao expressao) {
        parametros.add(expressao);
        numParametros ++;
        for (Token token : expressao.listaTokens()){
            super.addToken(token);
        }
    }

    public Token getTokenNome() {
        return nome;
    }
    
    public LinkedList<Expressao> getParametros() {
        return parametros;
    }
    
    public int getNumParametros() {
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
                    nome != null &&
                    numValidos == numParametros;
        }
    }

}
