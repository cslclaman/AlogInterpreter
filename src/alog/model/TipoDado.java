/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

import alog.token.Token;

/**
 *
 * @author Caique Souza
 */
public enum TipoDado {
    /**
     * Tipo numérico Inteiro. Números positivos e negativos sem ponto decimal.
     * Mapeado internamente para o tipo {@link Long}
     */
    INTEIRO ("Inteiro"),
    
    /**
     * Tipo numérico Real. Números positivos e negativos com ponto decimal.
     * Mapeado internamente para o tipo {@link Double}
     */
    REAL ("Real"),
    
    /**
     * Tipo Caracter. String de caracteres.
     * Mapeado internamente para o tipo {@link String}
     */
    CARACTER ("Caracter"),
    
    /**
     * Tipo booleano Lógico. Bit verdadeiro ou falso.
     * Mapeado internamente para o tipo {@link String} como String "verdadeiro" ou "falso".
     */
    LOGICO ("Lógico"),
    ;
    
    private final String exibicao;
    
    private TipoDado(String exibicao){
        this.exibicao = exibicao;
    }
    
    @Override
    public String toString() {
        return exibicao;
    }
    
    public static TipoDado mapTokenToVariavel (Token token){
        switch (token.getFuncaoToken()){
            case RES_TIPO_CARACTER: return CARACTER;
            case RES_TIPO_INTEIRO:  return INTEIRO;
            case RES_TIPO_REAL:     return REAL;
            case RES_TIPO_LOGICO:   return LOGICO;
            default:                return null;
        }
    }
}
