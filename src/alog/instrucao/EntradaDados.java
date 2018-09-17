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
        texto = "";
        tipo = TipoInstrucao.ENTRADA_DE_DADOS;
        tokens = new LinkedList<>();
        parametros = new LinkedList<>();
    }
    
    public void setTokenNome(Token token){
        if (nome == null) {
            nome = token;
            tokens.add(token);
        }
    }
    
    public void addVariavel(Token token) {
        if (nome != null) {
            parametros.add(token);
            tokens.add(token);
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
