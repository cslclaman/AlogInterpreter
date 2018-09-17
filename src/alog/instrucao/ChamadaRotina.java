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
        texto = "";
        tipo = TipoInstrucao.CHAMADA_ROTINA;
        tokens = new LinkedList<>();
        parametros = new LinkedList<>();
    }

    public void setTokenNome(Token token){
        if (nome == null) {
            nome = token;
            tokens.add(token);
        }
    }
    
    public void addParametro(Expressao expressao) {
        if (nome != null) {
            parametros.add(expressao);
            for (Token token : expressao.listaTokens()){
                tokens.add(token);
            }
        }
    }
    
    @Override
    public boolean instrucaoValida() {
        if (nome != null && !parametros.isEmpty()){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }
    
}
