/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.token.Token;
import java.util.LinkedList;

/**
 *
 * @author Caique
 */
public class EntradaDados extends Instrucao {

    protected Token nome;
    protected LinkedList<Token> parametros;
    
    public EntradaDados() {
        super();
        tipo = TipoInstrucao.ENTRADA_DE_DADOS;
        parametros = new LinkedList<>();
    }
    
    public void setTokenNome(Token token){
        if (nome == null) {
            nome = token;
            super.addToken(token);
        }
    }
    
    public void addVariavel(Token token) {
        if (nome != null) {
            parametros.add(token);
            super.addToken(token);
        }
    }

    @Override
    public boolean instrucaoValida() {
        if (nome == null || parametros.isEmpty()){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }

    public Token getNome() {
        return nome;
    }

    public LinkedList<Token> getParametros() {
        return parametros;
    }
}
