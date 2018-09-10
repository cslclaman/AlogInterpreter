/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

/**
 *
 * @author Caique Souza
 */
public enum TipoVariavel {
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
    
    private TipoVariavel(String exibicao){
        this.exibicao = exibicao;
    }
    
    @Override
    public String toString() {
        return exibicao;
    }
}
