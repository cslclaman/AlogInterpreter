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
    protected int numParametros;
    
    public EntradaDados() {
        super();
        tipo = TipoInstrucao.ENTRADA_DE_DADOS;
        parametros = new LinkedList<>();
        numParametros = 0;
    }
    
    public void setTokenNome(Token token){
        nome = token;
        super.addToken(token);
    }
    
    public void addVariavel(Token token) {
        parametros.add(token);
        numParametros ++;
        super.addToken(token);
    }

    public Token getNome() {
        return nome;
    }

    public LinkedList<Token> getParametros() {
        return parametros;
    }
    
    public Token getParametroAt(int index) {
        if (parametros.isEmpty()) {
            return null;
        } else {
            if (index >= 0 && index < numParametros) {
                return parametros.get(index);
            } else {
                return null;
            } 
        }
    }
    
    public int getNumParametros() {
        return numParametros;
    }
    
    @Override
    public void finaliza() {
        if (valida) {
            valida = 
                nome != null &&
                !parametros.isEmpty();
        }
    }
}
