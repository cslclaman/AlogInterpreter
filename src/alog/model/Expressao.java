/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

import java.util.ArrayList;

/**
 * Uma expressão aqui é uma sequência de Tokens que pode ser executada.
 * @author Caique
 */
public class Expressao {
    protected String texto;
    protected TipoExpressao tipo;
    protected ArrayList<Token> tokens;
    protected int indice;
    protected int linha;
    
    public Expressao(){
        tokens = new ArrayList<>();
        tipo = TipoExpressao._INDEFINIDO;
        indice = 0;
        linha = 0;
        texto = "";
    }
    
    public int getLinha(){
        return linha;
    }
    
    public void setLinha(int linha){
        this.linha = linha;
    }

    /**
     * Retorna expressão completa, aproximadamente como foi escrita. Ex.: {@code "Var <- Op1 + ( Op2 - Op3 ) ;" }
     * <br>Note que esse retorno não é necessariamente igual aos tokens realmente armazenados - 
     * o Parser já pode ter removido alguns delimitadores e símbolos para facilitar a execução.
     * @return {@code "Var <- Op1 + ( Op2 - Op3 ) ;" }
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Retorna expressão otimizada para execução. Ex.: {@code Escreva "Idade = " VarIdade }
     * <br>Note que esse retorno representa os tokens contidos na expressão,
     * com alguns delimitadores e símbolos removidos pelo parser para facilitar a execução.
     * @return {@code Escreva "Idade = " VarIdade }
     */
    public String printTokens() {
        StringBuilder str = new StringBuilder();
        for (Token t : tokens){
            str.append(" ").append(t.getPalavra());
        }
        return str.toString().trim();
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public void atualizaTexto(String sub){
        if (texto.isEmpty()){
            texto = sub;
        } else {
            texto += " " + sub;
        }
    }
    
    public boolean hasNextToken(){
        return indice < tokens.size();
    }
    
    public Token getNextToken(){
        if (hasNextToken()){
            return tokens.get(indice++);
        } else {
            return null;
        }
    }
    
    public int getIndice(){
        return indice;
    }
    
    public void setIndice(int indice){
        this.indice = indice;
    }
    
    public int getNumTokens(){
        return tokens.size();
    }
    
    public void addToken(Token token){
        tokens.add(token);
    }
    
    public Token getTokenAt(int i){
        return tokens.get(i);
    }
    
    public void setTokenAt(int i, Token token){
        tokens.set(i, token);
    }
    
    public void removeToken(Token token){
        tokens.remove(token);
    }

    public ArrayList<Token> listTokens(){
        return tokens;
    }
    
    public TipoExpressao getTipo() {
        return tipo;
    }

    public void setTipo(TipoExpressao tipo) {
        this.tipo = tipo;
    }

    /**
     * Retorna descrição da expressão e seu conteúdo já tratado (sem ponto e vírgula, sem parênteses em alguns casos. Ex.: {@code "Leia Op1 Op2 Op3" }
     * @return {@code "ENTRADA DE DADOS - Leia Op1 Op2 Op3" }
     */
    @Override
    public String toString() {
        return tipo + " - " + printTokens();
    }
    
    
}
