/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

import alog.token.Token;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Uma instrução é uma sequência de Tokens que pode ser executada.
 * Por padrão, a instrução possui tipo {@link TipoInstrucao#_INDEFINIDO }
 * e mantém uma lista de Tokens interna para as operações básicas.
 * Esse comportamento padrão <b>DEVE</b> ser sobrescrito.
 * @author Caique
 */
public abstract class Instrucao {
    protected String texto;
    protected TipoInstrucao tipo;
    private List<Token> tokens;
    
    public Instrucao(){
        tipo = TipoInstrucao._INDEFINIDO;
        texto = "";
        tokens = new LinkedList<>();
    }
    
    /**
     * Retorna expressão completa, aproximadamente como foi escrita. Ex.: {@code "Var <- Op1 + ( Op2 - Op3 ) ;" }
     * @return {@code "Var <- Op1 + ( Op2 - Op3 ) ;" }
     */
    public String getTexto() {
        return texto;
    }
    
    /**
     * Retorna o tipo da instrução.
     * @return {@link TipoInstrucao}
     */
    public TipoInstrucao getTipo() {
        return tipo;
    }

    /**
     * Retorna o número de tokens da instrução.
     * @return 
     */
    public int getNumTokens(){
        return tokens.size();
    }
    
    /**
     * Adiciona um token na instrução.
     * @param token O token a ser inserido.
     * @return TRUE se o Token é válido e foi inserido sem invalidar a instrução, senão FALSE.
     */
    public boolean insereToken(Token token){
        return tokens.add(token);
    }
    
    /**
     * Retorna uma lista com todos os Tokens que a Instrução armazena.
     * @return 
     */
    public List<Token> listaTokens() {
        return tokens;
    }
    
    /**
     * Retorna se a instrução é válida e executável.
     * Note que validade se refere à análise sintática, mas não previne erros em tempo de execução.
     * @return 
     */
    public abstract boolean instrucaoValida();

    /**
     * Retorna descrição da expressão e o texto armazenado.
     * @return {@code "ENTRADA DE DADOS - Leia Op1 Op2 Op3" }
     */
    @Override
    public String toString() {
        return tipo + " - " + texto;
    }
    
}
