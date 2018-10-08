/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

import alog.token.FuncaoToken;
import alog.token.TipoToken;
import alog.token.Token;

/**
 *
 * @author Caique
 */
public class ModuloPrincipal extends Bloco {
    protected Token tipoModulo;
    protected Token nome;
    
    public ModuloPrincipal() {
        super();
        tipo = TipoInstrucao.MODULO_PRINCIPAL;
    }
    
    public static ModuloPrincipal moduloImplicito() {
        ModuloPrincipal modulo = new ModuloPrincipal();
        
        Token tipoModulo = new Token(0, 0, 0, 1);
        tipoModulo.setPalavra("Algoritmo");
        tipoModulo.setTipoToken(TipoToken.ALFABETICO);
        tipoModulo.setFuncaoToken(FuncaoToken.RES_ALGORITMO);
        modulo.setTipoModulo(tipoModulo);
        
        Token nome = new Token(0, 9, 9, 2);
        nome.setPalavra("$Principal");
        nome.setTipoToken(TipoToken.ALFABETICO);
        nome.setFuncaoToken(FuncaoToken.IDENT_NOME_ALGORITMO);
        modulo.setNome(nome);
        
        return modulo;
    }
    
    public boolean isDeclarado() {
        return nome == null || !nome.getPalavra().equals("$Principal");
    }
    
    public void setTipoModulo(Token tipoModulo) {
        this.tipoModulo = tipoModulo;
        addToken(tipoModulo);
    }

    public void setNome(Token nome) {
        this.nome = nome;
        addToken(nome);
        texto.append("\n");
    }

    public Token getTipoModulo() {
        return tipoModulo;
    }

    public Token getNome() {
        return nome;
    }

}
