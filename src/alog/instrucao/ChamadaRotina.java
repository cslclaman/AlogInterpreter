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
    
    public ChamadaRotina() {
        super();
        tipo = TipoInstrucao.CHAMADA_ROTINA;
        parametros = new LinkedList<>();
    }

    public void setTokenNome(Token token){
        if (nome == null) {
            nome = token;
            super.addToken(token);
        }
    }
    
    public void addParametro(Expressao expressao) {
        if (nome != null) {
            parametros.add(expressao);
            for (Token token : expressao.listaTokens()){
                super.addToken(token);
            }
        }
    }
    
    @Override
    public boolean instrucaoValida() {
        if (nome == null || parametros.isEmpty()){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }
    
}
